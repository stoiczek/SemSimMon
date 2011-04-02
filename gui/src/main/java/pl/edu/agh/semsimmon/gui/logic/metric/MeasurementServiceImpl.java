package pl.edu.agh.semsimmon.gui.logic.metric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.CoreServiceFacade;
import pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semsimmon.common.exception.MeasurementException;
import pl.edu.agh.semsimmon.common.exception.ResourceNotRegisteredException;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnection;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnectionsManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation of CoreMeasurementService - nothing fancy.
 *
 * @author tkozak
 *         Created at 20:04 14-07-2010
 */
public class MeasurementServiceImpl implements MeasurementService, CapabilityValueListener {

  private static final Logger log = LoggerFactory.getLogger(MeasurementServiceImpl.class);

  private CoreConnectionsManager coreConnectionsManager;

  private Map<String, Measurement> measurements = new HashMap<String, Measurement>();

  private List<MeasurementsListener> measurementsListeners;

  /**
   * {@inheritDoc}
   */
  @Override
  public Measurement addMeasurement(Measurement measurement)
      throws MeasurementException {
    Resource resource = measurement.getResource();
    log.debug("Adding new measurement for resource: {}.", resource);
    final CoreConnection coreConnection = coreConnectionsManager.getConnectionOfResource(resource);
    measurement.setResourceUri(resource.getUri());
    String measurementId = coreConnection.getCoreServiceFacade().createMeasurement(measurement.getMeasurementDefinition());
    measurement.setId(measurementId);
    measurements.put(measurementId, measurement);
    dispatchMeasurementCreatedEvent(measurement);
    log.debug("Measurement created");
    return measurement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeMeasurement(String measurementId) throws MeasurementException {
    log.debug("Removing measurement with id: {}", measurementId);
    if (!measurements.containsKey(measurementId)) {
      throw new IllegalArgumentException("Measurement with given id doesn't exist.");
    }
    final Measurement measurement = measurements.get(measurementId);
    final Resource meResource = measurement.getResource();
    final CoreConnection connection = coreConnectionsManager.getConnectionOfResource(meResource);
    connection.getCoreServiceFacade().removeMeasurement(measurementId);
    measurements.remove(measurementId);
    dispatchMeasurementRemovedEvent(measurement);
    log.debug("Measurement removed");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getAllCapabilities(Resource resource)
      throws MeasurementException {
    log.debug("Getting all capabilities value of resource: {}", resource);
    try {
      final CoreConnection connection = coreConnectionsManager.getConnectionOfResource(resource);
      final CoreServiceFacade facade = connection.getCoreServiceFacade();
      final List<String> capabilities;
      capabilities = facade.getResourceCapabilities(resource);
      final Map<String, CapabilityValue> valuesNumeric = facade.getAllCapabilitiesValues(resource, capabilities);
      final Map<String, String> values = new HashMap<String, String>(valuesNumeric.size());
      for (Map.Entry<String, CapabilityValue> valueEntry : valuesNumeric.entrySet()) {
        values.put(valueEntry.getKey(), valueEntry.getValue().getNumericValue().toString());
      }
      log.debug("Values gathered. Returning {} entries.", values.size());
      return values;
    } catch (ResourceNotRegisteredException e) {
      throw new MeasurementException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getAllCapabilitiesNames(Resource resource) {
    log.debug("Getting all capabilities names for resource: {}", resource);
    final CoreConnection coreConnection = coreConnectionsManager.getConnectionOfResource(resource);
    try {
      final List<String> capabilities = coreConnection.getCoreServiceFacade().getResourceCapabilities(resource);
      log.debug("Returning list containing {} entries.", capabilities.size());
      return capabilities;
    } catch (ResourceNotRegisteredException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Measurement> getAllMeasurements() {
    log.debug("Returning all measurement types");
    return new LinkedList<Measurement>(measurements.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Measurement> getAllMeasurements(String capabilityType) {
    log.debug("Getting all measurements measuring capabilities of type: {}", capabilityType);
    List<Measurement> measurementsList = new LinkedList<Measurement>();
    for (Measurement measurement : measurements.values()) {
      if (measurement.getCapabilityUri().equals(capabilityType)) {
        measurementsList.add(measurement);
      }
    }
    return measurementsList;
  }

  @Override
  public void pauseAllMeasurements() throws MeasurementException {
    log.debug("Pausing all measurements");
    for (String measurementId : measurements.keySet()) {
      pauseMeasurement(measurementId);
    }
  }

  @Override
  public void resumeAllMeasurements() throws MeasurementException {
    log.debug("Resuming all measurements");
    for (String measurementId : measurements.keySet()) {
      resumeMeasurement(measurementId);
    }
  }

  @Override
  public void removeAllMeasurements() throws MeasurementException {
     log.debug("Removing all measurements");
    List<String > measurements = new LinkedList<String>(this.measurements.keySet());
    for (String measurementId : measurements) {
      removeMeasurement(measurementId);
    }
  }


  @Override
  public void pauseMeasurement(String measurementId) throws MeasurementException {
    log.debug("Pausing measurement with id: {}", measurementId);
    if (!measurements.containsKey(measurementId)) {
      throw new IllegalArgumentException("Measurement with given id doesn't exist.");
    }
    final Measurement measurement = measurements.get(measurementId);
    if(!measurement.isActive()) {
      log.warn("Trying to pause already paused resource");
      return;
    }
    final Resource meResource = measurement.getResource();
    final CoreConnection connection = coreConnectionsManager.getConnectionOfResource(meResource);
    connection.getCoreServiceFacade().pauseMeasurement(measurementId);
    measurement.setActive(false);
    log.debug("Measurement paused");
  }

  @Override
  public void resumeMeasurement(String measurementId) throws MeasurementException {
    log.debug("Pausing measurement with id: {}", measurementId);
    if (!measurements.containsKey(measurementId)) {
      throw new IllegalArgumentException("Measurement with given id doesn't exist.");
    }
    final Measurement measurement = measurements.get(measurementId);
    if(measurement.isActive()) {
      log.warn("Trying to resume already active measurement");
      return;
    }
    final Resource meResource = measurement.getResource();
    final CoreConnection connection = coreConnectionsManager.getConnectionOfResource(meResource);
    connection.getCoreServiceFacade().resumeMeasurement(measurementId);
    measurement.setActive(true);
    log.debug("Measurement paused");
  }

  /*
  * ===================================================================================================================
  * CapabilityValueListener implementation
  * ===================================================================================================================
  */

  /**
   * {@inheritDoc}
   */
  @Override
  public void newCapabilityValues(List<CapabilityValue> values) {
    if (values.isEmpty()) {
      log.warn("Got empty capability values list");
      return;
    }
    List<CapabilityValue> localList = new LinkedList<CapabilityValue>();
    String previousMetricsId = values.get(0).getMetricsId();
    for (CapabilityValue value : values) {
      if (previousMetricsId.equals(value.getMetricsId())) {
        localList.add(value);
      } else {
        Measurement measurement = measurements.get(previousMetricsId);
        measurement.newCapabilityValues(localList);
        localList.clear();
        previousMetricsId = value.getMetricsId();
        localList.add(value);
      }
    }
    if (!localList.isEmpty()) {
      measurements.get(previousMetricsId).newCapabilityValues(localList);
    }
  }

  /*
  * ===================================================================================================================
  * Private utilities, spring init.
  * ===================================================================================================================
  */


  private void dispatchMeasurementRemovedEvent(Measurement measurement) {
    log.debug("Dispatching measurement removed event to ({}) listeners. Removed measurement id: {}",
        new Object[]{measurementsListeners.size(), measurement.getId()});
    for (MeasurementsListener listener : measurementsListeners) {
      listener.measurementRemoved(measurement);
    }
  }

  private void dispatchMeasurementCreatedEvent(Measurement measurement) {
    log.debug("Dispatching measurement created event to ({}) listeners. Created measurement id: {}",
        new Object[]{measurementsListeners.size(), measurement.getId()});
    for (MeasurementsListener listener : measurementsListeners) {
      listener.measurementCreated(measurement);
    }
  }

  public void setCoreConnectionsManager(CoreConnectionsManager coreConnectionsManager) {
    this.coreConnectionsManager = coreConnectionsManager;
  }

  public void setMeasurementsListeners(List<MeasurementsListener> measurementsListeners) {
    this.measurementsListeners = measurementsListeners;
  }
}
