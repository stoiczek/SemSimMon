package pl.edu.agh.semmon.core.transport;

import pl.edu.agh.semmon.common.api.transport.TransportProxy;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.util.List;

/**
 * Manages all transport proxies registered with this core instance.
 *
 * @author tkozak
 *         Created at 11:39 07-08-2010
 */
public interface TransportProxiesManager {

  /**
   * Finds transport proxy for given resource. If this resource is newly created one, this method tries to
   * lookup for proper transport proxy. On the other hand - if this resource has been known to instance of this
   * proxies manager - it just returns cached TransportProxy instance.
   * @param resource resource to get transport proxy for
   * @return transport proxy which can be used to manage given resource
   */
  TransportProxy findProxyForResource(Resource resource);

  /**
   * Returns list containing all resource proxies defined for this core instance.
   * @return list containing all resource proxies defined for this core instance
   */
  List<TransportProxy> getAllProxies();

}
