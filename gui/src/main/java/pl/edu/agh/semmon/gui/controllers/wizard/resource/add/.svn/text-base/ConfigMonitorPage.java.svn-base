package pl.edu.agh.semmon.gui.controllers.wizard.resource.add;

import org.apache.pivot.wtk.*;
import org.apache.pivot.wtkx.WTKX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import pl.edu.agh.semmon.gui.controllers.wizard.BaseWizardPageController;
import pl.edu.agh.semmon.gui.controllers.wizard.resource.add.jmx.JmxConnectionType;
import pl.edu.agh.semmon.gui.logic.connection.ConnectionType;
import pl.edu.agh.semmon.gui.logic.connection.CoreConnection;
import pl.edu.agh.semmon.gui.logic.connection.CoreConnectionsManager;
import pl.edu.agh.semmon.gui.logic.connection.ExternalCoreFeedbackSink;
import pl.edu.agh.semmon.gui.util.ButtonDataContainer;
import pl.edu.agh.semmon.gui.util.ListItemDataContainer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:52 05-06-2010
 */
public class ConfigMonitorPage extends BaseWizardPageController<BoxPane> implements ExternalCoreFeedbackSink {

  private static final Logger log = LoggerFactory.getLogger(ConfigMonitorPage.class.getName());

  @WTKX
  private ButtonGroup connectionTypeButtonGroup;

  @WTKX
  private TablePane existingMonitorPane;

  @WTKX
  private TablePane externalMonitorPane;

  @WTKX
  private BoxPane startNewMonitorPane;

  @WTKX
  private TextInput sshUserAndHostTextInput;

  @WTKX
  private TextInput passwordTextInput;

  @WTKX
  private TextInput externalMonitorHostTextInput;


  @WTKX
  private ListButton monitorsList;

  @WTKX
  private Meter uploadProgressMeter;

  @WTKX
  private BoxPane sshExternalLogsSink;


  private CoreConnectionsManager coreConnectionsManager;

  private Map<ConnectionType, Resource> connectionTypeIcons;

  private String sshConnectionString;
  private String password;
  private String externalMonitorHost;
  private String existingConnectionId;


  private JmxConnectionType connectionType;

  private CoreConnection externalConnection;

  private long bytesToUpload;

  @Override
  public void pageShowing() {

  }

  @Override
  public void pageHiding() {
    try {
      log.debug("Config monitor page is hiding - setting up core");
      connectionType = getSelectedConnectionType();
      switch (connectionType) {
        case START_NEW_MONITOR:
          sshConnectionString = sshUserAndHostTextInput.getText();
          password = passwordTextInput.getText();
          externalConnection = coreConnectionsManager.startExternalCoreProcess(sshConnectionString, password, this);
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            log.error("Impossible", e);
          }
          break;
        case USE_EXISTING:
          existingConnectionId = ((ListItemDataContainer) monitorsList.getSelectedItem()).getId();
          break;
        case USE_EXTERNAL:
          externalMonitorHost = externalMonitorHostTextInput.getText();
          externalConnection = coreConnectionsManager.connectToExternalCore(externalMonitorHost);
      }
      log.debug("Core initialized.");
    } catch (IOException e) {
      throw new RuntimeException("Error connecting to given core", e);
    }
  }


  private void monitorTypeChanged() {
    final JmxConnectionType type = getSelectedConnectionType();
    startNewMonitorPane.setVisible(false);
    existingMonitorPane.setVisible(false);
    externalMonitorPane.setVisible(false);
    switch (type) {
      case START_NEW_MONITOR:
        startNewMonitorPane.setVisible(true);
        break;
      case USE_EXISTING:
        existingMonitorPane.setVisible(true);
        break;
      case USE_EXTERNAL:
        externalMonitorPane.setVisible(true);
        break;
      default:
        final String msg = "Got unsupported JMX connection type: " + type;
        log.error(msg);
        throw new IllegalArgumentException(msg);
    }
  }

  private JmxConnectionType getSelectedConnectionType() {
    ButtonDataContainer container = (ButtonDataContainer) connectionTypeButtonGroup.getSelection().getButtonData();
    return (JmxConnectionType) container.getEnumValue();
  }


  @Override
  protected Class getBindableClass() {
    return ConfigMonitorPage.class;
  }


  @Override
  protected void postBinding() throws IOException {
    final List<CoreConnection> currentConnections = coreConnectionsManager.getCoreConnections();
    org.apache.pivot.collections.LinkedList<ListItemDataContainer> listItems = new org.apache.pivot.collections.LinkedList<ListItemDataContainer>();
    for (CoreConnection connection : currentConnections) {
      final ListItemDataContainer listItemDataContainer = new ListItemDataContainer();
      listItemDataContainer.setText(connection.getLabel());
      listItemDataContainer.setId(connection.getId());
      listItemDataContainer.setIcon(connectionTypeIcons.get(connection.getConnectionType()).getURL());
      listItems.add(listItemDataContainer);
    }
    monitorsList.setListData(listItems);
    if (!listItems.isEmpty()) {
      monitorsList.setSelectedIndex(0);
    }
    monitorsList.getListButtonSelectionListeners().add(new ListButtonSelectionListener() {
      @Override
      public void selectedIndexChanged(ListButton listButton, int previousSelectedIndex) {
        log.debug("Selection changed");
      }
    });
    connectionTypeButtonGroup.getButtonGroupListeners().add(new ButtonGroupListener.Adapter() {

      @Override
      public void selectionChanged(ButtonGroup buttonGroup, Button previousSelection) {
        monitorTypeChanged();
      }
    });

  }

  public String getSshConnectionString() {
    return sshConnectionString;
  }

  public String getPassword() {
    return password;
  }

  public String getExternalMonitorHost() {
    return externalMonitorHost;
  }

  public String getExistingConnectionId() {
    return existingConnectionId;
  }


  public JmxConnectionType getConnectionType() {
    return connectionType;
  }

  public CoreConnection getExternalConnection() {
    return externalConnection;
  }

  @Override
  public void appendLogStatus(String msg, String... args) {
    log.debug("Appending log from connection");
    sshExternalLogsSink.add(new Label(resources.getString(msg, args)));
    sshExternalLogsSink.getParent().repaint();
  }

  @Override
  public void setBytesToUpload(long bytesToUpload) {
    this.bytesToUpload = bytesToUpload;
  }

  @Override
  public void setBytesUploaded(long bytesUploaded) {
    uploadProgressMeter.setPercentage((float) bytesUploaded / bytesToUpload);
  }

  public void setCoreConnectionsManager(CoreConnectionsManager coreConnectionsManager) {
    this.coreConnectionsManager = coreConnectionsManager;
  }

  public void setConnectionTypeIcons(Map<ConnectionType, Resource> connectionTypeIcons) {
    this.connectionTypeIcons = connectionTypeIcons;
  }
}
