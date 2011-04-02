package pl.edu.agh.semsimmon.core.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.transport.TransportProxy;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Basic implementation for TransportProxiesManager
 *
 * @author tkozak
 *         Created at 11:41 07-08-2010
 */
public class TransportProxiesManagerImpl implements TransportProxiesManager {

  private static final Logger log = LoggerFactory.getLogger(TransportProxiesManagerImpl.class);

  private static final String TRANSPORT_PROPERTY_NAME = TransportProxiesManagerImpl.class.getName().hashCode() +
      ".transport.UUID";

  private Map<String, TransportProxy> transportProxies = new HashMap<String, TransportProxy>();

  private List<TransportProxy> transportProxiesList;


  /**
   * {@inheritDoc}
   */
  @Override
  public TransportProxy findProxyForResource(Resource resource) {
    log.debug("Getting transport proxy for resource: {}", resource);
    if(resource.hasProperty(TRANSPORT_PROPERTY_NAME)) {
      final String proxyUUID = resource.getProperty(TRANSPORT_PROPERTY_NAME).toString();
      if(transportProxies.containsKey(proxyUUID)) {
        log.debug("Resource already known to - returning cached instance");
        return transportProxies.get(proxyUUID);
      }
    }
    log.debug("Searching for transport proxy for given resource");
    Map.Entry<String, TransportProxy> resourceProxyEntry = null;
    for (Map.Entry<String, TransportProxy> proxy : transportProxies.entrySet()) {
      if (proxy.getValue().isResourceSupported(resource)) {
        resourceProxyEntry = proxy;
        break;
      }
    }
    if (resourceProxyEntry == null) {
      throw new RuntimeException("Cannot find proxy for given resource");
    }
    resource.setProperty(TRANSPORT_PROPERTY_NAME, resourceProxyEntry.getKey());
    return resourceProxyEntry.getValue();
  }

  @Override
  public List<TransportProxy> getAllProxies() {
    return transportProxiesList;
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public void setTransportProxies(List<TransportProxy> transportProxies) {
    this.transportProxiesList = transportProxies;

  }

  @SuppressWarnings({"UnusedDeclaration"})
  @PostConstruct
  public void initDiscoveryListening() {
    if (transportProxiesList == null) {
      log.warn("There are no transport proxies defined");
      return;
    }
    for (TransportProxy proxy : transportProxiesList) {
      transportProxies.put(UUID.randomUUID().toString(), proxy);
    }
  }

}
