package pl.edu.agh.semsimmon.gui.logic.connection;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import pl.edu.agh.semsimmon.common.api.CoreServiceFacade;
import pl.edu.agh.semsimmon.common.api.remote.RemoteEventsListener;
import pl.edu.agh.semsimmon.common.consts.RemotingConsts;

import java.io.IOException;

/**
 * Represents connection to externally started core.
 *
 * @author tkozak
 *         Created at 14:26 06-06-2010
 */
public class ExternalCoreConnection extends AbstractCoreConnection {

  private String serviceHost;

  protected ExternalCoreConnection(ConnectionType connectionType) {
    super(connectionType);
  }


  public ExternalCoreConnection(String serviceHost) {
    super(ConnectionType.EXTERNAL);
    this.serviceHost = serviceHost;
  }

  public void connect() throws IOException {
    RmiProxyFactoryBean proxyFactoryBean = new RmiProxyFactoryBean();
    proxyFactoryBean.setCacheStub(true);
    proxyFactoryBean.setRefreshStubOnConnectFailure(true);
    proxyFactoryBean.setServiceInterface(CoreServiceFacade.class);
    proxyFactoryBean.setServiceUrl(createServiceUrl());
    proxyFactoryBean.afterPropertiesSet();
    coreServiceFacade = (CoreServiceFacade) proxyFactoryBean.getObject();

  }

  public void disconnect() {
    coreServiceFacade = null;
  }


  private String createServiceUrl() {
    return RemotingConsts.RMI_URI_PREFIX + serviceHost + ":" +
        RemotingConsts.CORE_RMI_REGISTRY_PORT + "/" +
        RemotingConsts.CORE_SERVICE_FACADE_NAME;
  }

  protected void setServiceHost(String serviceHost) {
    this.serviceHost = serviceHost;
  }
}
