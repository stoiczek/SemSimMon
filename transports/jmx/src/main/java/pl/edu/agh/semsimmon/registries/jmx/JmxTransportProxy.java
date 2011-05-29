package pl.edu.agh.semsimmon.registries.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryEvent;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.api.transport.AbstractTransportProxy;
import pl.edu.agh.semsimmon.common.api.transport.TransportException;
import pl.edu.agh.semsimmon.common.consts.JmxRegistryConsts;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.common.vo.core.resource.ResourceDiscoveryEvent;
import pl.edu.agh.semsimmon.registries.jmx.discovery.DiscoveryAgent;
import pl.edu.agh.semsimmon.registries.jmx.probe.CapabilityProbe;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.rmi.ConnectException;
import java.util.*;

/**
 * Transport proxy implementation which uses JMX as back-end
 *
 * @author tkozak
 *         Created at 11:38 11-07-2010
 */
public class JmxTransportProxy extends AbstractTransportProxy {

  private static final Logger log = LoggerFactory.getLogger(JmxTransportProxy.class);
  /**
   *
   */
  private Map<String, Map<String, DiscoveryAgent>> discoveryAgents;

  /**
   *
   */
  private final Map<String, MBeanServerConnection> connections = new HashMap<String, MBeanServerConnection>();


  private Map<String, Map<String, CapabilityProbe>> capabilityProbes = new HashMap<String, Map<String, CapabilityProbe>>();

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityType) throws TransportException {
    log.debug("Getting capability value from resource: {}. Capability URI: {}", new Object[]{resource, capabilityType});
    if (!hasCapability(resource, capabilityType)) {
      log.warn("Cannot gather capability value of resource: {} and capability uri: {}",
          new Object[]{resource, capabilityType});
      return new CapabilityValue(Double.NaN);
    }
    final CapabilityProbe probe = capabilityProbes.get(resource.getTypeUri()).get(capabilityType);
    try {
      return probe.getCapabilityValue(resource, capabilityType, connections.get(
          resource.getProperty(JmxRegistryConsts.SERVICE_URL_PROPERTY)));
    } catch (IOException e) {
      if (e instanceof ConnectException) {
        log.warn("Process has died");
        ResourceDiscoveryEvent resourceRemovedEvent = new ResourceDiscoveryEvent(Arrays.asList(resource),
            IResourceDiscoveryEvent.EventType.RESOURCES_REMOVED);
        fireEvent(resourceRemovedEvent);
      }
    } catch (ItemRemovedException e) {
      log.warn("Thread exited");
      ResourceDiscoveryEvent resourceRemovedEvent = new ResourceDiscoveryEvent(Arrays.asList(resource),
          IResourceDiscoveryEvent.EventType.RESOURCES_REMOVED);
      fireEvent(resourceRemovedEvent);
    } catch (RuntimeException e) {
      log.error("Caught exception", e);
    }
    return new CapabilityValue(Double.NaN);
  }

  @Override
  public boolean hasCapability(Resource resource, String capabilityType) throws TransportException {
    log.debug("Checking whether proxy can gather value of capability of type: {}, from resource: {}",
        new Object[]{capabilityType, resource});
    if (!resource.getProperties().containsKey(JmxRegistryConsts.SERVICE_URL_PROPERTY)) {
      return false;
    }
    if (!capabilityProbes.containsKey(resource.getTypeUri())) {
      return false;
    }
    final Map<String, CapabilityProbe> probes = capabilityProbes.get(resource.getTypeUri());
    return probes.containsKey(capabilityType);
  }

  @Override
  public boolean unregisterResource(Resource resource) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new TransportException("Cannot unregister resource that isn't supported by this proxy");
    }
    if (resource.getTypeUri().equals(KnowledgeConstants.NODE_URI)) {
      String transportURI = (String) resource.getProperty(JmxRegistryConsts.SERVICE_URL_PROPERTY);
      connections.remove(transportURI);
      fireResourceRemovedEvent(resource);
      // don't need to drop children
      return true;
    }
    //  application or cluster is being removed - need to drop children
    return false;
  }

  @Override
  public void registerResource(Resource resource) throws TransportException {
    log.debug("Registering URI: " + resource.getUri() + " " + resource.getProperties().values().toString());
    String transportUri = resource.getProperty(JmxRegistryConsts.SERVICE_URL_PROPERTY).toString();
    if (!connections.containsKey(transportUri)) {
      JMXServiceURL url;
      try {
        url = new JMXServiceURL(transportUri);
        final JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        final MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        connections.put(transportUri, mbsc);
        mbsc.createMBean("org.crossgrid.wp3.monitoring.jims.mbeans.Linux.SystemInformation", new ObjectName(
            "linuxMonitoringExtension:type=SystemInformation"));
      } catch (InstanceAlreadyExistsException e) {
        // fall-through - if this mbean already exists, everything is ok
      } catch (ReflectionException e) {
        log.warn("Error creating jims mBean, some information will be unavailable");
      } catch (Exception e) {
        throw new TransportException(e);
      }
    }
    // Hack to allow removal of JMX node resources
    if (resource.getTypeUri().compareTo(KnowledgeConstants.NODE_URI) == 0) {
      resource.setProperty(ResourcePropertyNames.RESOURCE_REMOVABLE, true);
    }
  }

  @Override
  public boolean isResourceSupported(Resource resource) {
    return resource.hasProperty(JmxRegistryConsts.SERVICE_URL_PROPERTY);
  }


  @Override
  public void discoverChildren(Resource resource, List<String> types) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new TransportException("Given resource is unsupported with this proxy");
    }
    if (!discoveryAgents.containsKey(resource.getTypeUri())) {
      log.error("Cannot discover child resources from given parent: " + resource.getUri() + "(" + resource.getTypeUri() + ")");
      return;
    }
    Map<String, DiscoveryAgent> agents = discoveryAgents.get(resource.getTypeUri());
    final List<Resource> children = new LinkedList<Resource>();
    final String connectionUri = resource.getProperty(JmxRegistryConsts.SERVICE_URL_PROPERTY).toString();
    final MBeanServerConnection connection = connections.get(connectionUri);
    for (final String type : types) {
      if (agents.containsKey(type)) {
        final DiscoveryAgent agent = agents.get(type);
        List<Resource> childResources = null;
        try {
          childResources = agent.discoveryChildren(connection, resource, type);
        } catch (java.io.IOException e) {
          throw new TransportException(e);
        }
        for (final Resource childResource : childResources) {
          childResource.setProperty(JmxRegistryConsts.SERVICE_URL_PROPERTY, connectionUri);
          children.add(childResource);
        }
      } else {
        log.error("Cannot discover child resources from given parent: " + resource.getUri() + "(" + resource.getTypeUri() +
            ") of given type: " + type);
      }
    }
    fireNewResourcesEvent(resource, children);
  }

  @Override
  public List<Resource> discoverDirectChildren(Resource resource, String type) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new TransportException("Given resource is not usupported by this proxy. Check first!");
    }
    registerResource(resource);
    final String transportUri = (String) resource.getProperty(JmxRegistryConsts.SERVICE_URL_PROPERTY);
    DiscoveryAgent agent = discoveryAgents.get(resource.getTypeUri()).get(type);
    try {
      return agent.discoveryChildren(connections.get(transportUri), resource, type);
    } catch (IOException e) {
      throw new TransportException(e);
    }
  }

  public void setDiscoveryAgents(Map<String, Map<String, DiscoveryAgent>> discoveryAgents) {
    this.discoveryAgents = discoveryAgents;
  }

  public void setCapabilityProbes(Map<String, Map<String, CapabilityProbe>> capabilityProbes) {
    this.capabilityProbes = capabilityProbes;
  }
}
