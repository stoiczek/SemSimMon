package pl.edu.agh.semsimmon.common.api.remote;

/**
 * Provides core's services to remote clients.
 *
 * @author tkozak
 *         Created at 11:27 14-08-2010
 */
public interface RemoteClientsService {

  /**
   * Adds given remote event listener.
   * @param listener listener to add
   */
  void addRemoteEventsListener(RemoteEventsListener listener);

}
