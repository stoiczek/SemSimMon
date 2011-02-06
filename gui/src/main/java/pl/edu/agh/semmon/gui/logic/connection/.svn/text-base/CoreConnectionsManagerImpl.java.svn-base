package pl.edu.agh.semmon.gui.logic.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semmon.common.api.remote.RemoteEventsListener;
import pl.edu.agh.semmon.common.api.resource.ResourceEvent;
import pl.edu.agh.semmon.common.api.resource.ResourceEventsListener;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation of CoreConnectionsManager.
 *
 * @author tkozak
 *         Created at 14:45 06-06-2010
 */
public class CoreConnectionsManagerImpl implements CoreConnectionsManager, ResourcesListener, CapabilityValueListener {

  private static final Logger log = LoggerFactory.getLogger(CoreConnectionsManagerImpl.class.getName());

  private static final String RESOURCES_CONNECTION_ID = CoreConnectionsManagerImpl.class.getName().hashCode() + ".connection.id";

  /**
   * Gui's embedded core instance.
   */
  private EmbeddedCoreConnection embeddedCoreConnection;

  /**
   * Connections map. Points from connectionId -> AbstractCoreConnection object
   */
  private Map<String, CoreConnection> connections = new HashMap<String, CoreConnection>();

  private Map<String, RemoteEventsListener> remoteEventsListeners = new HashMap<String, RemoteEventsListener>();

  private List<ResourceEventsListener> resourceListeners;

  private List<CapabilityValueListener> measurementValuesListeners;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CoreConnection> getCoreConnections() {
    log.debug("Returning list of all current core connections");
    return new ArrayList<CoreConnection>(connections.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CoreConnection connectToExternalCore(String serviceHost) throws IOException {
    log.debug("Connecting to external core service with url: {}", serviceHost);
    final ExternalCoreConnection connection = new ExternalCoreConnection(serviceHost);
    connection.connect();
    postProcessExternalConnection(connection);
    return connection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CoreConnection startExternalCoreProcess(String connectionString, String password,
                                                 ExternalCoreFeedbackSink feedbackSink) throws IOException {
    log.debug("Starting external core instance process. Connection string: ", connectionString);
    ExternalProcessCoreConnection connection =
        new ExternalProcessCoreConnection(connectionString, password, feedbackSink);
    connection.startCore();
    connection.connect();
    postProcessExternalConnection(connection);
    log.debug("External process started");
    return connection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopExternalCoreProcess(String connectionId) {
    log.debug("Stopping external core process with id: {}", connectionId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disconnectFromExternalCore(String connectionId) throws IOException {
    log.debug("Disconnecting from external core instance with id: {}", connectionId);
    connections.remove(connectionId);
    try {
      UnicastRemoteObject.unexportObject(remoteEventsListeners.get(connectionId), false);
    } catch (NoSuchObjectException e) {
      throw new IOException("Error unexporting listener", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CoreConnection getConnectionById(String connectionId) {
    return connections.get(connectionId);
  }

  @Override
  public CoreConnection getConnectionOfResource(Resource resource) {
    if (!resource.hasProperty(RESOURCES_CONNECTION_ID)) {
      throw new IllegalArgumentException("Got invalid resource - missing connection id parameter");
    }
    return getConnectionById(resource.getProperty(RESOURCES_CONNECTION_ID).toString());
  }

  @Override
  public void identifyResource(Resource resource, String connectionId) {
    resource.setProperty(RESOURCES_CONNECTION_ID, connectionId);
  }


  /*
  =================================================================================
  ResourceEventsListener implementation
  =================================================================================
  */

  @Override
  public void processEvent(String connId, ResourceEvent event) {
    for (Resource resource : event.getResources()) {
      resource.setProperty(RESOURCES_CONNECTION_ID, connId);
    }
    for (ResourceEventsListener listener : resourceListeners) {
      try {
        listener.processEvent(event);
      } catch (Exception e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
    }
  }

  @Override
  public void newCapabilityValues(List<CapabilityValue> values) {
    for (CapabilityValueListener listener : measurementValuesListeners) {
      listener.newCapabilityValues(values);
    }
  }


  private void postProcessConnection(AbstractCoreConnection connection) {
    connection.setResourcesListener(this);
    connections.put(connection.getId(), connection);
  }

  private void postProcessExternalConnection(CoreConnection connection) throws IOException {
    RemoteEventsListener listener = new RemoteEventsListenerImpl(this, this, connection.getId());
    try {
      UnicastRemoteObject.exportObject(listener, 0);
    } catch (RemoteException e) {
      throw new IOException("Error exporting listener", e);
    }
    connection.getCoreServiceFacade().addRemoteEventsListener(listener);
    connections.put(connection.getId(), connection);
    remoteEventsListeners.put(connection.getId(), listener);
  }
  /*
=================================================================================
Initialization
=================================================================================
*/

  public void setEmbeddedCoreConnection(EmbeddedCoreConnection embeddedCoreConnection) {
    this.embeddedCoreConnection = embeddedCoreConnection;
  }

  @PostConstruct
  public void initialize() {
    embeddedCoreConnection.setResourcesListener(this);
    embeddedCoreConnection.getCoreServiceFacade().addCapabilityValueListener(this);
    connections.put(embeddedCoreConnection.getId(), embeddedCoreConnection);
  }

  @PreDestroy
  public void tearDownAllConnections() {
    log.debug("Closing all active connections");
    for(CoreConnection connection : getCoreConnections()) {
      switch (connection.getConnectionType()) {
        case EXTERNAL:
          ExternalCoreConnection externalCoreConnection = (ExternalCoreConnection) connection;
          externalCoreConnection.disconnect();
          break;
        case EXTERNAL_PROCESS:
          ExternalProcessCoreConnection externalProcessCoreConnection = (ExternalProcessCoreConnection) connection;
          externalProcessCoreConnection.disconnect();
          try {
            externalProcessCoreConnection.stopCore();
          } catch (IOException e) {
            log.error("Error stopping external core");
          }
          break;
        default:
          log.debug("Got embedded connection");
      }
    }

  }

  public void setResourceListeners(List<ResourceEventsListener> resourceListeners) {
    this.resourceListeners = resourceListeners;
  }

  public void setMeasurementValuesListeners(List<CapabilityValueListener> measurementValuesListeners) {
    this.measurementValuesListeners = measurementValuesListeners;
  }
}
