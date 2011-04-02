package pl.edu.agh.semsimmon.common.api.remote;

import pl.edu.agh.semsimmon.common.api.resource.ResourceEvent;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote wrapper for core's notification listeners: ResourceEventListener and CapabilityValueListener
 *
 * @author tkozak
 *         Created at 11:17 14-08-2010
 */
public interface RemoteEventsListener extends  Remote {


  /**
   * Wrapper for CapabilityValueListener's newCapabilityValues
   * @see pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener#newCapabilityValues(java.util.List)
   * @param values values
   * @throws java.rmi.RemoteException on remote error
   */
  void newCapabilityValues(List<CapabilityValue> values) throws RemoteException;

  /**
   * Wrapper for ResourceEventsListener's processEvent.
   * @see pl.edu.agh.semsimmon.common.api.resource.ResourceEventsListener#processEvent(pl.edu.agh.semsimmon.common.api.resource.ResourceEvent)
   * @param events events
   * @throws RemoteException on transport error
   */
  void processEvent(List<ResourceEvent> events) throws RemoteException;
}
