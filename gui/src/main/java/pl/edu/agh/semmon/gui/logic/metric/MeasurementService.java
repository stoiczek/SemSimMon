package pl.edu.agh.semmon.gui.logic.metric;

import pl.edu.agh.semmon.common.exception.MeasurementException;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.util.List;
import java.util.Map;

/**
 * Interface for services related to metrics, measurements, or capability values of resources.
 *
 * @author tkozak
 *         Created at 20:04 14-07-2010
 */
public interface MeasurementService {

  /**
   * Creates new measurement.
   *
   * @param mesMeasurement
   * @return id newly created measurement
   */
  Measurement addMeasurement(Measurement mesMeasurement) throws MeasurementException;

  /**
   * Removes measurement with given id.
   *
   * @param measurementId id of measurement to remove.
   */
  void removeMeasurement(String measurementId) throws MeasurementException;

  /**
   * Returns snapshot values of all capabilities of given resources, as dictionary - capability name to its formatted
   * value
   *
   * @param resource resource to get All capabilities of.
   * @return dictionary containing values of all current resource capabilities.
   */
  Map<String, String> getAllCapabilities(Resource resource) throws MeasurementException;

  /**
   * Returns names of given resource's capabilities.
   *
   * @param resource to get capability names of.
   * @return resource list containing names of given resource's capabilities.
   */
  List<String> getAllCapabilitiesNames(Resource resource);

  /**
   * Returns list containing all currently running measurements.
   *
   * @return list containing all currently running measurements
   */
  List<Measurement> getAllMeasurements();


  /**
   * Returns list containing all currently running measurements, which measures given capability by type.
   *
   * @return list type of capability to get measurements of.
   */
  List<Measurement> getAllMeasurements(String capabilityType);


  void pauseMeasurement(String id) throws MeasurementException;

  void resumeMeasurement(String id) throws MeasurementException;

  void resumeAllMeasurements() throws MeasurementException;

  void removeAllMeasurements() throws MeasurementException;

  void pauseAllMeasurements() throws MeasurementException;
}
