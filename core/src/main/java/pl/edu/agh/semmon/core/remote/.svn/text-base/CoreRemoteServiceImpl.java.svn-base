package pl.edu.agh.semmon.core.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.remote.RemoteEventsListener;
import pl.edu.agh.semmon.common.api.resource.ResourceEvent;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;

import javax.annotation.PreDestroy;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of CoreRemoteService
 *
 * @author tkozak
 *         Created at 11:31 14-08-2010
 */
public class CoreRemoteServiceImpl implements CoreRemoteService, Runnable {

  private static final Logger log = LoggerFactory.getLogger(CoreRemoteServiceImpl.class);

  private static final int DISPATCH_INTERVAL = 2000;

  private List<RemoteEventsListener> listeners = new LinkedList<RemoteEventsListener>();

  private Thread eventsDispatcher;

  private final LinkedList<CapabilityValue> capabilitiesQueue = new LinkedList<CapabilityValue>();

  private final LinkedList<ResourceEvent> resourceEventsQueue = new LinkedList<ResourceEvent>();

  private AtomicBoolean active;

  @Override
  public void addRemoteEventsListener(RemoteEventsListener listener) {
    log.debug("Adding remote events listener");
    listeners.add(listener);
  }

  @Override
  public void newCapabilityValues(List<CapabilityValue> values) {
    log.debug("Got new capability values");
    if (listeners.isEmpty()) {
      log.warn("There are noe listeners to dispatch values to. Skipping.");
      return;
    }
    synchronized (capabilitiesQueue) {
      capabilitiesQueue.addAll(values);
    }
  }

  @Override
  public void processEvent(ResourceEvent event) throws Exception {
    log.debug("Got new resource event");
    if (listeners.isEmpty()) {
      log.warn("There are no listeners to dispatch event to. Skipping.");
      return;
    }
    synchronized (resourceEventsQueue) {
      resourceEventsQueue.add(event);
    }
  }

  public void initEventsDispatcher() {
    active = new AtomicBoolean(true);
    eventsDispatcher = new Thread(this);
    eventsDispatcher.start();
  }

  @PreDestroy
  public void tearDownEventsDispatcher() throws InterruptedException {
    active.set(false);
    eventsDispatcher.join();
  }

  @Override
  public void run() {
    while (active.get()) {
      log.debug("Dispatching events to remote listeners");
      try {
        dispatchResourcesEvents();
        dispatchCapabilityEvents();

        Thread.sleep(DISPATCH_INTERVAL);
      } catch (InterruptedException e) {
        log.warn("Events dispatching thread interrupted");
      } catch (Exception e) {
        log.warn("Exception during events dispatching",e);
      }
    }

  }

  private void dispatchCapabilityEvents() {
    List<CapabilityValue> values;
    synchronized (capabilitiesQueue) {
      if (capabilitiesQueue.isEmpty()) {
        return;
      }
      values = new LinkedList<CapabilityValue>(capabilitiesQueue);
      capabilitiesQueue.clear();
    }
    log.debug("Dispatching capability value events ({})", values.size());
    final List<RemoteEventsListener> badListeners = new LinkedList<RemoteEventsListener>();
    for (RemoteEventsListener listener : listeners) {
      try {
        listener.newCapabilityValues(values);
      } catch (RemoteException e) {
        log.error("Error sending events to listener: {}", listener);
        log.error("Cause: ", e);
        badListeners.add(listener);
      }
    }
    listeners.removeAll(badListeners);
  }

  private void dispatchResourcesEvents() {
    List<ResourceEvent> events;
    synchronized (resourceEventsQueue) {
      if (resourceEventsQueue.isEmpty()) {
        log.debug("There are no resource events to dispatch. Skipping");
        return;
      }
      events = new LinkedList<ResourceEvent>(resourceEventsQueue);
      resourceEventsQueue.clear();
    }
    log.debug("Dispatching resource events ({})", events.size());
    final List<RemoteEventsListener> badListeners = new LinkedList<RemoteEventsListener>();
    for (RemoteEventsListener listener : listeners) {
      try {
        listener.processEvent(events);
      } catch (RemoteException e) {
        log.error("Error sending events to listener: {}", listener);
        log.error("Cause: ", e);
        badListeners.add(listener);
      }
    }
    listeners.removeAll(badListeners);
  }
}
