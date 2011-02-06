package pl.edu.agh.semmon.core.measurement;

import pl.edu.agh.semmon.common.exception.MeasurementException;
import pl.edu.agh.semmon.common.vo.core.measurement.MeasurementDefinition;

/**
 * Provides services that can poll for capability values in scheduled manner.
 *
 * @author tkozak
 *         Created at 12:36 14-08-2010
 */
public interface CapabilityValuePollManager {

  /**
   * Submits job polling for capability value described by given measurement definition.
   * @param measurementDefinition definition of jub to create.
   * @param listener listener which should be notified about capability values
   */
  void submitPollJob(MeasurementDefinition measurementDefinition, MeasurementListener listener) throws MeasurementException;

  /**
   * Cancels polling job, for given measurement.
   * @param measurementDefinition definition of job to cancel
   */
  void cancelPollJob(MeasurementDefinition measurementDefinition) throws MeasurementException;

  /**
   * Updates already submitted job. New job parameters are taken from given measurement definition.
   * @param measurementDefinition definition of job to update
   */
  void updatePollJob(MeasurementDefinition measurementDefinition) throws MeasurementException;

}
