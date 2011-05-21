package pl.edu.agh.semsimmon.gui.logic.connection;

import pl.edu.agh.semsimmon.common.api.CoreServiceFacade;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEvent;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEventsListener;

import java.util.UUID;

/**
 * Reflects connection to core module.
 *
 * @author tkozak
 *         Created at 14:12 06-06-2010
 */
public class AbstractCoreConnection implements CoreConnection, ResourceEventsListener {

  private ResourcesListener resourcesListener;

  protected AbstractCoreConnection(ConnectionType connectionType) {
    this.connectionType = connectionType;
    connectionId = UUID.randomUUID().toString();
  }

  /**
   * Id of this connection - might be use to localize connection in maps. JVM-scoped uniqueness is guaranteed.
   */
  protected final String connectionId;

  /**
   * Reference to remote core service facade.
   */
  protected CoreServiceFacade coreServiceFacade;


  /**
   * User-friendly label of this connection - used by any UI widget.
   */
  protected String label;

  /**
   * MemoryProbeType of this connection.
   */
  final protected ConnectionType connectionType;

  /**
   * Returns current connection id.
   *
   * @return current connection id.
   */
  @Override
  public String getId() {
    return connectionId;
  }

  @Override
  public void processEvent(ResourceEvent event) throws Exception {
    resourcesListener.processEvent(connectionId, event);
  }

  /**
   * Returns facade to core service to which this connection is connected.
   *
   * @return facade to core service to which this connection is connected.
   */
  @Override
  public CoreServiceFacade getCoreServiceFacade() {
    return coreServiceFacade;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public ConnectionType getConnectionType() {
    return connectionType;
  }

  public void setResourcesListener(ResourcesListener resourcesListener) {
    this.resourcesListener = resourcesListener;
  }
}
