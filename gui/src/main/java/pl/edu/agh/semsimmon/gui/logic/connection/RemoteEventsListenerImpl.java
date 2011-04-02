package pl.edu.agh.semsimmon.gui.logic.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semsimmon.common.api.remote.RemoteEventsListener;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEvent;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;

import java.rmi.RemoteException;
import java.util.List;

/**
 * @author tkozak
 *         Created at 19:58 11-09-2010
 */
public class RemoteEventsListenerImpl implements RemoteEventsListener {

  private static final Logger log = LoggerFactory.getLogger(RemoteEventsListenerImpl.class);

  CapabilityValueListener capabilityValueListener;
  ResourcesListener resourceEventsListener;

  String connectionId;

  public RemoteEventsListenerImpl(CapabilityValueListener capabilityValueListener, ResourcesListener resourceEventsListener, String connectionId) {
    this.capabilityValueListener = capabilityValueListener;
    this.resourceEventsListener = resourceEventsListener;
    this.connectionId = connectionId;
  }

  @Override
  public void newCapabilityValues(List<CapabilityValue> values) throws RemoteException {

    capabilityValueListener.newCapabilityValues(values);

  }

  @Override
  public void processEvent(List<ResourceEvent> events) throws RemoteException {
    for (ResourceEvent event : events) {
      try {
        resourceEventsListener.processEvent(connectionId, event);
      } catch (Exception e) {
        log.error("Error handling event", e);
      }
    }
  }
}
