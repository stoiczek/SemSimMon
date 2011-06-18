package pl.edu.agh.semsimmon.core.impl;

import pl.edu.agh.semsimmon.common.api.CoreServiceFacade;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeService;
import pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semsimmon.common.api.resource.CoreResourcesService;
import pl.edu.agh.semsimmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semsimmon.common.api.remote.RemoteEventsListener;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEventsListener;
import pl.edu.agh.semsimmon.common.exception.MeasurementException;
import pl.edu.agh.semsimmon.common.exception.ResourceNotRegisteredException;
import pl.edu.agh.semsimmon.common.exception.ResourcesException;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.core.remote.CoreRemoteService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The Core element of SemSimMon.
 *
 * @author koperek
 */
public class CoreServiceFacadeImpl implements CoreServiceFacade {

  private CoreResourcesService coreResourcesService = null;

  private CoreMeasurementService measurementService;

  private CoreRemoteService coreRemoteService;

  private KnowledgeService knowledgeService;

  /*
  * ===================================================================================================================
  * CoreResourcesService delegations
  * ===================================================================================================================
  */

  /**
   * {@inheritDoc}
   */
  @Override
  public void addResourceListener(ResourceEventsListener listener) {
    coreResourcesService.addResourceListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeResourceListener(ResourceEventsListener listener) {
    coreResourcesService.removeResourceListener(listener);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isResourceRegistered(String uri) {
    return coreResourcesService.isResourceRegistered(uri);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getAllRegisteredResources() {
    return coreResourcesService.getAllRegisteredResources();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getResourceType(String uri) {
    return coreResourcesService.getResourceType(uri);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getResourceCapabilities(String uri)
      throws ResourceNotRegisteredException {
    return coreResourcesService.getResourceCapabilities(uri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getResourceCapabilities(Resource resource) throws ResourceNotRegisteredException {
    return coreResourcesService.getResourceCapabilities(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Resource> discoverDirectChildren(Resource resource, String childrenType) {
    return coreResourcesService.discoverDirectChildren(resource, childrenType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterResource(String uri) {
    coreResourcesService.unregisterResource(uri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerResource(String uri, String serviceUri, Map<String, Object> parameters)
      throws ResourceAlreadyRegisteredException {
    coreResourcesService.registerResource(uri, serviceUri, parameters);
  }

  @Override
  public void registerResource(Resource resource) throws ResourceAlreadyRegisteredException {
    coreResourcesService.registerResource(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Resource getResourceForURI(String uri) throws ResourceNotRegisteredException {
    return coreResourcesService.getResourceForURI(uri);
  }

  @Override
  public void stopResource(Resource resource) throws ResourcesException {
    coreResourcesService.stopResource(resource);
  }

  @Override
  public void pauseResource(Resource resource) throws ResourcesException {
    coreResourcesService.pauseResource(resource);
  }

  @Override
  public void resumeResource(Resource resource) throws ResourcesException {
    coreResourcesService.resumeResource(resource);
  }

/*
  * ===================================================================================================================
  * CoreMeasurementService delegations
  * ===================================================================================================================
  */

  /**
   * {@inheritDoc}
   */
  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri) throws MeasurementException {
    return measurementService.getCapabilityValue(resource, capabilityUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, CapabilityValue> getAllCapabilitiesValues(Resource resource, List<String> capabilityUris)
      throws MeasurementException {
    return measurementService.getAllCapabilitiesValues(resource, capabilityUris);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CapabilityValue getCapabilityValue(String resourceUri, String capabilityUri) throws MeasurementException {
    return measurementService.getCapabilityValue(resourceUri, capabilityUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, CapabilityValue> getAllCapabilitiesValues(String resourceUri, List<String> capabilityUris) throws MeasurementException {
    return measurementService.getAllCapabilitiesValues(resourceUri, capabilityUris);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addCapabilityValueListener(CapabilityValueListener listener) {
    measurementService.addCapabilityValueListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createMeasurement(MeasurementDefinition measurementDefinition) throws MeasurementException {
    return measurementService.createMeasurement(measurementDefinition);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateMeasurement(MeasurementDefinition measurementDefinition) throws MeasurementException {
    measurementService.updateMeasurement(measurementDefinition);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeMeasurement(String measurementId) throws MeasurementException {
    measurementService.removeMeasurement(measurementId);
  }

  @Override
  public void removeMeasurementsOfResource(Resource resource) throws MeasurementException {
    measurementService.removeMeasurementsOfResource(resource);
  }

  @Override
  public void pauseMeasurement(String measurementId) throws MeasurementException {
    measurementService.pauseMeasurement(measurementId);
  }

  @Override
  public void resumeMeasurement(String measurementId) throws MeasurementException {
    measurementService.resumeMeasurement(measurementId);
  }

  /*
  * ===================================================================================================================
  * CoreRemoteService delegations
  * ===================================================================================================================
  */

  @Override
  public void addRemoteEventsListener(RemoteEventsListener listener) {
    coreRemoteService.addRemoteEventsListener(listener);
  }

  /*
  * ===================================================================================================================
  * KnowledgeService delegations
  * ===================================================================================================================
  */

  @Override
  public void reloadOntology(byte[] ontologyContent) throws IOException {
    knowledgeService.reloadOntology(ontologyContent);
  }

  /*
  * ===================================================================================================================
  * Spring initialization
  * ===================================================================================================================
  */

  @SuppressWarnings({"UnusedDeclaration"})
  public void setCoreResourcesService(
      CoreResourcesService coreResourcesService) {
    this.coreResourcesService = coreResourcesService;
  }

  public void setMeasurementService(CoreMeasurementService measurementService) {
    this.measurementService = measurementService;
  }

  public void setCoreRemoteService(CoreRemoteService coreRemoteService) {
    this.coreRemoteService = coreRemoteService;
  }

  public void setKnowledgeService(KnowledgeService knowledgeService) {
    this.knowledgeService = knowledgeService;
  }
}
