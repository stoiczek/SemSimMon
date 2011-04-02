package pl.edu.agh.semsimmon.gui.logic.connection;

import pl.edu.agh.semsimmon.common.api.resource.ResourceEvent;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 19:58 31-07-2010
 */
public interface ResourcesListener {


  /**
   * Used to notify about resources event.
   * @param connectionId id of connection which generated event to be processed
   * @param event resources event to process
   *
   */
  public void processEvent(String connectionId, ResourceEvent event);
}
