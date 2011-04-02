package pl.edu.agh.semsimmon.common.api.measurement;

import pl.edu.agh.semsimmon.common.exception.MeasurementException;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.List;
import java.util.Map;

/**
 * Interface for service related to measurements - gathering capability values etc.
 *
 * @author tkozak
 *         Created at 11:37 07-08-2010
 */
public interface CoreMeasurementService {


  /**
   * Returns value of given capability from given resource.
   *
   * @param resource      resource to get capability value of
   * @param capabilityUri uri of capability to get value of
   * @return capability value
   * @throws MeasurementException on any error
   */
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri) throws MeasurementException;

  /**
   * Fetches values of all capabilities from given resource, and using given list of capability uris.
   *
   * @param resource       resource to get capability values of
   * @param capabilityUris list containing uris of capabilities to fetch values of
   * @return map containing capability uri -> capability value pairs
   * @throws MeasurementException on any error
   */
  public Map<String, CapabilityValue> getAllCapabilitiesValues(Resource resource, List<String> capabilityUris)
      throws MeasurementException;

  /**
   * Returns value of given capability from given resource.
   *
   * @param resourceUri   uri of resource to get capability value of
   * @param capabilityUri uri of capability to get value of
   * @return capability value
   * @throws MeasurementException on any error
   */
  public CapabilityValue getCapabilityValue(String resourceUri, String capabilityUri) throws MeasurementException;

  /**
   * Fetches values of all capabilities from given resource, and using given list of capability uris.
   *
   * @param resourceUri    URI of  resource to get capability values of
   * @param capabilityUris list containing uris of capabilities to fetch values of
   * @return map containing capability uri -> capability value pairs
   * @throws MeasurementException on any error
   */
  public Map<String, CapabilityValue> getAllCapabilitiesValues(String resourceUri, List<String> capabilityUris)
      throws MeasurementException;


  /**
   * Registers given listener to be notified about new capablity values
   *
   * @param listener listener instance to be registered
   */
  public void addCapabilityValueListener(CapabilityValueListener listener);

  /**
   * Creates measurement using given definition.
   *
   * @param measurementDefinition definition of measurement to create
   * @return newly created measurement's id. It will be used to notify about ne capability values.
   * @throws MeasurementException on any error
   */
  public String createMeasurement(MeasurementDefinition measurementDefinition) throws MeasurementException;

  /**
   * Updates measurement. Given measurement definition MUST contain proper measurement id.
   *
   * @param measurementDefinition definition of measurement to udpate.
   * @throws MeasurementException on any error
   */
  public void updateMeasurement(MeasurementDefinition measurementDefinition) throws MeasurementException;

  /**
   * Removes measurement with given id.
   *
   * @param measurementId id of measurement to remove.
   * @throws MeasurementException on any error
   */
  public void removeMeasurement(String measurementId) throws MeasurementException;

  /**
   * Removes all measurements of given resource.
   * @param resource resource to remove measurements of.
   * @throws MeasurementException on error with removal.
   */
  public void removeMeasurementsOfResource(Resource resource) throws MeasurementException;

  void pauseMeasurement(String measurementId) throws MeasurementException;

  void resumeMeasurement(String measurementId) throws MeasurementException;
}
