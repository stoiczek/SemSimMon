package pl.edu.agh.semmon.registries.ocmg;

import org.balticgrid.ocmg.objects.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.api.transport.AbstractTransportProxy;
import pl.edu.agh.semmon.common.api.transport.TransportException;
import pl.edu.agh.semmon.common.consts.OcmgRegistryConsts;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.probe.CapabilityProbe;
import pl.edu.agh.semmon.registries.ocmg.resource.ResourceAgent;

import java.util.*;

/**
 * @author tkozak
 *         Created at 47:12 18-05-2010
 *         Transport proxy capabile communicating with OCMG and J-OCMG backends.
 */
public class OcmgTransportProxyImpl extends AbstractTransportProxy {

  private static final Logger log = LoggerFactory.getLogger(OcmgTransportProxyImpl.class.getName());

  /**
   * Prefix of URI containing
   */
  public static final String SEMMON_URI_SCHEME = "semmon";

  /**
   *
   */
  private Map<String, OcmgConnection> activeConnections = new HashMap<String, OcmgConnection>();


  /**
   *
   */
  private Map<String, Application> applicationCache = new HashMap<String, Application>();

  /**
   *
   */
  private Map<String, Map<String, CapabilityProbe>> probes = new HashMap<String, Map<String, CapabilityProbe>>();


  /**
   *
   */
  private Map<String, ResourceAgent> resourceAgents = new HashMap<String, ResourceAgent>();

  /**
   * @{inheritDoc }
   */
  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityType) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new TransportException("Got getCapabilityValue request with unsupported resource: " + resource.getUri());
    }
    if (!hasCapability(resource, capabilityType)) {
      log.warn("This transport proxy cannot acquire value of given capability: {0} and resource: {1}",
          resource.getUri(), capabilityType);
      return new CapabilityValue(Double.NaN);
    }

    final CapabilityProbe probe = probes.get(resource.getTypeUri()).get(capabilityType);
    final Application app = getApplication(resource);
    try {
      return probe.getCapabilityValue(resource, app);
    } catch (OcmgException e) {
      throw new TransportException(e);
    }
  }

  /**
   * @{inheritDoc }
   */
  @Override
  public boolean hasCapability(Resource resource, String capabilityType) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new TransportException("Got hasCapability check request with unsupported resource: " + resource.getUri());
    }
    final String resourceType = resource.getTypeUri();
    if (!probes.containsKey(resourceType)) {
      return false;
    }
    Map<String, CapabilityProbe> resourceProbes = probes.get(resourceType);
    return resourceProbes.containsKey(capabilityType);
  }


  /**
   * @{inheritDoc }
   */
  @Override
  public void registerResource(Resource resource) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new IllegalArgumentException("Given resource cannot be registered with this Transport proxy");
    }
    final String connectionString = resource.getProperty(OcmgRegistryConsts.MAIN_SM_CONNECTION_STRING).toString();
    try {
      if (!activeConnections.containsKey(connectionString)) {
        OcmgConnection connection = new OcmgConnection(resource);
        connection.connect();
        activeConnections.put(connectionString, connection);
      }

      final OcmgConnection connection = activeConnections.get(connectionString);
      if (resource.getTypeUri().equals(KnowledgeConstants.APPLICATION_URI)) {
        Application application = connection.attachToApplication(resource);
        applicationCache.put(resource.getUri(), application);
      }
      Application application = getApplication(resource);

    } catch (OcmgException e) {
      throw new TransportException(e);
    }
  }


  /**
   * @{inheritDoc }
   */
  @Override
  public void unregisterResource(Resource resource) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new TransportException("Given resource cannot be registered with this Transport proxy");
    }
    if (!applicationCache.containsKey(resource.getUri())) {
      throw new TransportException("Given resource haven't been registered with this Transport proxy");
    }
    applicationCache.remove(resource.getUri());
    final String connectionString = getConnectionPart(resource.getUri());
    final OcmgConnection conn = activeConnections.get(connectionString);
    try {
      conn.detachFromApplication(resource);
    } catch (OcmgException e) {
      throw new TransportException(e);
    }
    fireResourceRemovedEvent(resource);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isResourceSupported(final Resource resource) {
    return resource.hasProperty(OcmgRegistryConsts.MAIN_SM_CONNECTION_STRING);
  }


  @Override
  public void discoverChildren(Resource resource, List<String> types) throws TransportException {
    verifyResourceSupported(resource);
    if (!resourceAgents.containsKey(resource.getTypeUri())) {
      log.error("Cannot discover child resources from given parent: " + resource.getUri() + "(" + resource.getTypeUri() + ")");
      return;
    }
    final List<Resource> children = new LinkedList<Resource>();
    final Application app = getApplication(resource);
    for (final String type : types) {
      if (resourceAgents.containsKey(type)) {
        ResourceAgent agent = resourceAgents.get(type);
        final List<Resource> childResources;
        try {
          childResources = agent.discoverChildResources(app, resource, type);
          for (final Resource childResource : childResources) {
            children.add(childResource);
          }
        } catch (OcmgException e) {
          log.error("Got error when discovering children", e);
        }
      } else {
        log.error("Cannot discover child resources from given parent: " + resource.getUri() +
            "(" + resource.getTypeUri() + ") of given type: " + type);
      }
    }
    fireNewResourcesEvent(resource, children);
  }

  @Override
  public List<Resource> discoverDirectChildren(Resource resource, String type) throws TransportException {
    verifyResourceSupported(resource);
    if (!resourceAgents.containsKey(type)) {
      return Collections.emptyList();
    }
    try {
      ResourceAgent agent = resourceAgents.get(type);
      return agent.discoverChildResources(getApplication(resource), resource, type);
    } catch (OcmgException e) {
      throw new TransportException(e);
    }
  }

  @Override
  public void stopResource(Resource resource) throws TransportException {
    verifyResourceSupported(resource);
    if (!resourceAgents.containsKey(resource.getTypeUri())) {
      throw new TransportException("Given resource is unsupported");
    }
    final Application app = getApplication(resource);
    final ResourceAgent agent = resourceAgents.get(resource.getTypeUri());
    try {
      agent.stop(app, resource);
    } catch (OcmgException e) {
      throw new TransportException(e);
    }
  }

  @Override
  public void pauseResource(Resource resource) throws TransportException {
    verifyResourceSupported(resource);
    if (!resourceAgents.containsKey(resource.getTypeUri())) {
      throw new TransportException("Given resource is unsupported");
    }
    final Application app = getApplication(resource);
    final ResourceAgent agent = resourceAgents.get(resource.getTypeUri());
    try {
      agent.pause(app, resource);
    } catch (OcmgException e) {
      throw new TransportException(e);
    }
  }

  @Override
  public void resumeResource(Resource resource) throws TransportException {
    verifyResourceSupported(resource);
    if (!resourceAgents.containsKey(resource.getTypeUri())) {
      throw new TransportException("Given resource is unsupported");
    }
    final Application app = getApplication(resource);
    final ResourceAgent agent = resourceAgents.get(resource.getTypeUri());
    try {
      agent.resume(app, resource);
    } catch (OcmgException e) {
      throw new TransportException(e);
    }
  }


  private void verifyResourceSupported(Resource resource) throws TransportException {
    if (!isResourceSupported(resource)) {
      throw new TransportException("Given resource is unsupported with this proxy");
    }
  }

  private String getConnectionPart(String uri) {
    uri = uri.substring(SEMMON_URI_SCHEME.length() + 3);
    return uri.split("/")[0];
  }

  private Application getApplication(Resource resource) {
    String resourceUri = resource.getUri();
    int firstSlash = resourceUri.indexOf("/", SEMMON_URI_SCHEME.length() + 3);
    int secondSlash = resourceUri.indexOf("/", firstSlash + 1);
    String applicationUri;
    if (secondSlash == -1 || secondSlash == resourceUri.length()) {
      applicationUri = resourceUri;
    } else {
      applicationUri = resourceUri.substring(0, secondSlash);
    }
    return applicationCache.get(applicationUri);
  }


  public void setProbes(Map<String, Map<String, CapabilityProbe>> probes) {
    this.probes = probes;
  }

  public void setResourceAgents(Map<String, ResourceAgent> resourceAgents) {
    this.resourceAgents = resourceAgents;
  }
}
