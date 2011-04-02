package pl.edu.agh.semsimmon.core.remote;

import pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semsimmon.common.api.remote.RemoteClientsService;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEventsListener;

/**
 * Core's remote service. Acts as events multiplexer - listens on various local events, multiplexes them and
 * dispatches to remote clients 
 * @author tkozak
 *         Created at 11:29 14-08-2010
 */
public interface CoreRemoteService extends RemoteClientsService, ResourceEventsListener, CapabilityValueListener {
}
