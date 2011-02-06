package pl.edu.agh.semmon.common.api.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.resource.IResourceDiscoveryEvent;
import pl.edu.agh.semmon.common.api.resource.IResourceDiscoveryListener;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.common.vo.core.resource.ResourceDiscoveryEvent;

import java.util.*;


public abstract class AbstractTransportProxy implements TransportProxy {

  private final Logger log = LoggerFactory.getLogger(AbstractTransportProxy.class);

  protected List<IResourceDiscoveryListener> registryListeners = new ArrayList<IResourceDiscoveryListener>();

  @Override
  public void addResourceDiscoveryListener(final IResourceDiscoveryListener listener) {
    if (!registryListeners.contains(listener)) {
      registryListeners.add(listener);
    }
  }

  @Override
  public void removeTransportProxyListener(IResourceDiscoveryListener listener) {
    registryListeners.remove(listener);
  }

  @Override
  public Map<String, CapabilityValue> getCapabilityValues(Resource resource, List<String> capabilityUris)
      throws TransportException {
    log.debug("Getting multiple capability values from resource: {}. Amount of capabilites to fetch: {}",
        new Object[]{resource, capabilityUris.size()});
    final Map<String, CapabilityValue> values = new HashMap<String, CapabilityValue>();
    for (final String uri : capabilityUris) {
      values.put(uri, getCapabilityValue(resource, uri));
    }
    return values;
  }

  @Override
  public void stopResource(Resource resource) throws TransportException {
    throw new TransportException("This transport exception cannot stop given resource");
  }

  @Override
  public void pauseResource(Resource resource) throws TransportException {
    throw new TransportException("This transport exception cannot stop given resource");
  }

  @Override
  public void resumeResource(Resource resource) throws TransportException {
    throw new TransportException("This transport exception cannot stop given resource");
  }

  protected void fireEvent(IResourceDiscoveryEvent event) {
    for (IResourceDiscoveryListener listener : registryListeners) {
      listener.processEvent(event);
    }
  }

  protected void fireNewResourcesEvent(Resource parent,
                                       List<Resource> children) {
    IResourceDiscoveryEvent event = new ResourceDiscoveryEvent(parent, children,
        IResourceDiscoveryEvent.EventType.NEW_RESOURCES_DISCOVERED);
    fireEvent(event);
  }

  protected void fireResourceRemovedEvent(Resource resource) {
    fireEvent(new ResourceDiscoveryEvent(Arrays.asList(resource), IResourceDiscoveryEvent.EventType.RESOURCES_REMOVED));
  }
}
