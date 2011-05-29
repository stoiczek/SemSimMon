package pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add;

import pl.edu.agh.semsimmon.gui.controllers.wizard.BaseWizardController;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnection;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnectionsManager;
import pl.edu.agh.semsimmon.gui.logic.connection.ExternalCoreConnection;
import pl.edu.agh.semsimmon.gui.logic.connection.ExternalProcessCoreConnection;
import pl.edu.agh.semsimmon.gui.logic.resource.ResourcesService;

import java.io.IOException;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 12:22 29-08-2010
 */
public abstract class BaseAddResourceWizardCtrl extends BaseWizardController {
  /**
   *
   */
  private CoreConnectionsManager coreConnectionsManager;
  /**
   *
   */
  protected ResourcesService resourcesService;
  protected String connectionId;

  protected String initConnection(ConfigMonitorPage monitorPage) throws IOException {
    CoreConnection connection;
    switch (monitorPage.getConnectionType()) {
      case START_NEW_MONITOR:
        connection = monitorPage.getExternalConnection();
        break;
      case USE_EXTERNAL:
        connection = monitorPage.getExternalConnection();
        break;
      case USE_EXISTING:
        connection = coreConnectionsManager.getConnectionById(monitorPage.getExistingConnectionId());
        break;
      default:
        throw new IllegalArgumentException("Unknown connection type: " + monitorPage.getConnectionType());
    }
    connectionId = connection.getId();
    return connectionId;
  }

  @Override
  protected void wizardCancelled() throws IOException {
    final ConfigMonitorPage monitorPage = (ConfigMonitorPage) pages.get(0);
    switch (monitorPage.getConnectionType()) {
      case USE_EXTERNAL:
        ExternalCoreConnection conn = (ExternalCoreConnection) monitorPage.getExternalConnection();
        conn.disconnect();
        break;
      case START_NEW_MONITOR:
        ExternalProcessCoreConnection procConn = (ExternalProcessCoreConnection) monitorPage.getExternalConnection();
        procConn.disconnect();
        procConn.stopCore();
        break;

    }
  }

  public void setCoreConnectionsManager(CoreConnectionsManager coreConnectionsManager) {
    this.coreConnectionsManager = coreConnectionsManager;
  }

  public void setResourcesService(ResourcesService resourcesService) {
    this.resourcesService = resourcesService;
  }
}
