package pl.edu.agh.semmon.core.measurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semmon.common.api.resource.CoreResourcesService;
import pl.edu.agh.semmon.common.api.transport.TransportException;
import pl.edu.agh.semmon.common.api.transport.TransportProxy;
import pl.edu.agh.semmon.common.exception.MeasurementException;
import pl.edu.agh.semmon.common.exception.ResourceNotRegisteredException;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.core.remote.CoreRemoteService;
import pl.edu.agh.semmon.core.transport.TransportProxiesManager;

import java.util.*;

/**
 * Basic implementation of CoreMeasurementService
 *
 * @author tkozak
 *         Created at 12:07 07-08-2010
 */
public class CoreMeasurementServiceImpl implements CoreMeasurementService, MeasurementListener {

  private static final Logger log = LoggerFactory.getLogger(CoreMeasurementServiceImpl.class);

  /**
   * To get capability values per request
   */
  private TransportProxiesManager transportProxiesManager;


  /**
   * To notify about measurement's capability values
   */
  private List<CapabilityValueListener> capabilityValueListeners = new LinkedList<CapabilityValueListener>();

  /**
   * Measurements cache
   */
  private Map<String, MeasurementDefinition> measurements = new HashMap<String, MeasurementDefinition>();


  private Map<String, List<String>> resourceUri2Measurements = new HashMap<String, List<String>>();
  /**
   * To register measurement's scheduled polls.
   */
  private CapabilityValuePollManager pollManager;

  private CoreResourcesService coreResourcesService;

  /*
  * ===================================================================================================================
  * CoreMeasurementService implementation
  * ===================================================================================================================
  */

  /**
   * {@inheritDoc}
   */
  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri) throws MeasurementException {
    log.debug("Getting capability value for resource: {}. Capability uri: {}", new Object[]{resource, capabilityUri});
    final TransportProxy transportProxy = transportProxiesManager.findProxyForResource(resource);
    try {
      return transportProxy.getCapabilityValue(resource, capabilityUri);
    } catch (TransportException e) {
      throw new MeasurementException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, CapabilityValue> getAllCapabilitiesValues(Resource resource, List<String> capabilityUris)
      throws MeasurementException {
    log.debug("Getting multiple capability values for resource: {}. Capability uri: {}",
        new Object[]{resource, capabilityUris.size()});
    final TransportProxy transportProxy = transportProxiesManager.findProxyForResource(resource);
    try {
      return transportProxy.getCapabilityValues(resource, capabilityUris);
    } catch (TransportException e) {
      throw new MeasurementException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CapabilityValue getCapabilityValue(String resourceUri, String capabilityUri) throws MeasurementException {
    try {
      return getCapabilityValue(coreResourcesService.getResourceForURI(resourceUri), capabilityUri);
    } catch (ResourceNotRegisteredException e) {
      throw new MeasurementException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, CapabilityValue> getAllCapabilitiesValues(String resourceUri, List<String> capabilityUris)
      throws MeasurementException {
    try {
      return getAllCapabilitiesValues(coreResourcesService.getResourceForURI(resourceUri), capabilityUris);
    } catch (ResourceNotRegisteredException e) {
      throw new MeasurementException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addCapabilityValueListener(CapabilityValueListener listener) {
    capabilityValueListeners.add(listener);
  }

  @Override
  public String createMeasurement(MeasurementDefinition measurementDefinition) throws MeasurementException {
    log.debug("Creating measurement with definition: {}", measurementDefinition);
    final String measurementUUID = UUID.randomUUID().toString();
    measurementDefinition.setId(measurementUUID);
    measurements.put(measurementUUID, measurementDefinition);
    pollManager.submitPollJob(measurementDefinition, this);
    String resourceUri = measurementDefinition.getResourceUri();
    if(!resourceUri2Measurements.containsKey(resourceUri)) {
      resourceUri2Measurements.put(resourceUri, new LinkedList<String>());
    }
    resourceUri2Measurements.get(resourceUri).add(measurementUUID);
    log.debug("Measurement created. ID: {}", measurementUUID);
    return measurementUUID;
  }

  @Override
  public void updateMeasurement(MeasurementDefinition measurementDefinition) throws MeasurementException {
    log.debug("Updating measurement. New definition: {}", measurementDefinition);
    measurements.put(measurementDefinition.getId(), measurementDefinition);
    pollManager.updatePollJob(measurementDefinition);
    log.debug("Measurement updated");
  }

  @Override
  public void removeMeasurement(String measurementId) throws MeasurementException {
    log.debug("Removing measurement with id: {}", measurementId);
    verifyMeasurementExists(measurementId);
    final MeasurementDefinition definition = measurements.get(measurementId);
    measurements.remove(measurementId);
    resourceUri2Measurements.get(definition.getResourceUri()).remove(measurementId);
    pollManager.cancelPollJob(definition);
    log.debug("Measurement removed");
  }

  @Override
  public void removeMeasurementsOfResource(Resource resource) throws MeasurementException {
    if(!resourceUri2Measurements.containsKey(resource.getUri())) {
      return;
    }
    final String resourceUri = resource.getUri();
    // need to clone, to avoid concurrent modification exception.
    List<String> resourceMeasurements = new LinkedList<String>(resourceUri2Measurements.get(resourceUri));
    for(String measurementUUID : resourceMeasurements) {
      removeMeasurement(measurementUUID);
    }
  }

  @Override
  public void pauseMeasurement(String measurementId) throws MeasurementException {
    log.debug("Pausing measurement with id: {}", measurementId);
    verifyMeasurementExists(measurementId);
    pollManager.cancelPollJob(measurements.get(measurementId));
  }

  @Override
  public void resumeMeasurement(String measurementId) throws MeasurementException {
    verifyMeasurementExists(measurementId);
    log.debug("Resuming measurement with id: {}", measurementId);
    pollManager.submitPollJob(measurements.get(measurementId), this);

  }

  /*
  * ===================================================================================================================
  * MeasurementListener implementation
  * ===================================================================================================================
  */


  @Override
  public void newCapabilityValue(CapabilityValue value) {
    dispatchCapabilityValued(Arrays.asList(value));
  }

  /*
  * ===================================================================================================================
  * Private utilities, spring init
  * ===================================================================================================================
  */

  private void verifyMeasurementExists(String id) throws MeasurementException {
    if(!measurements.containsKey(id)) {
      throw new MeasurementException("Measurement with given id doesn't exist. ID: " + id);
    }
  }

  private void dispatchCapabilityValued(List<CapabilityValue> values) {
    for (CapabilityValueListener listener : capabilityValueListeners) {
      listener.newCapabilityValues(values);
    }
  }

  public void setTransportProxiesManager(TransportProxiesManager transportProxiesManager) {
    this.transportProxiesManager = transportProxiesManager;
  }

  public void setCoreRemoteService(CoreRemoteService coreRemoteService) {
    capabilityValueListeners.add(coreRemoteService);
  }

  public void setPollManager(CapabilityValuePollManager pollManager) {
    this.pollManager = pollManager;
  }

  public void setCoreResourcesService(CoreResourcesService coreResourcesService) {
    this.coreResourcesService = coreResourcesService;
  }
}
