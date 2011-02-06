package pl.edu.agh.semmon.core.measurement;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semmon.common.api.resource.CoreResourcesService;
import pl.edu.agh.semmon.common.exception.MeasurementException;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.measurement.MeasurementDefinition;

import java.util.Map;

/**
 * Job class responsible for polling of capability values.
 *
 * @author tkozak
 *         Created at 14:08 14-08-2010
 */
@SuppressWarnings({"UnusedDeclaration"})
public class PollJob implements Job {

  private static final Logger log = LoggerFactory.getLogger(PollJob.class);

  public static final String LISTENER_ATTR_NAME = "listener";
  public static final String MEASUREMENT_DEF_ATTR_NAME = "measurementDefinition";

  public static final String MEASUREMENT_SERVICE_ATTR_NAME = "measurementService";


  private MeasurementListener listener;
  private CoreResourcesService resourcesService;
  private CoreMeasurementService measurementService;
  private MeasurementDefinition measurementDefinition;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      setupDependencies(context);
      CapabilityValue value = measurementService.getCapabilityValue(
          measurementDefinition.getResourceUri(),
          measurementDefinition.getCapabilityUri());
      value.setMetricsId(measurementDefinition.getId());
      listener.newCapabilityValue(value);
    } catch (MeasurementException e) {
      log.error("Error getting capability value.", e);
    } catch (SchedulerException e) {
      log.error("Error getting capability value.", e);
    }
  }

  private void setupDependencies(JobExecutionContext context) throws SchedulerException {
    Map params = context.getMergedJobDataMap();
    measurementService =
        (CoreMeasurementService) context.getScheduler().getContext().get(MEASUREMENT_SERVICE_ATTR_NAME);
    listener = (MeasurementListener) params.get(LISTENER_ATTR_NAME);
    measurementDefinition = (MeasurementDefinition) params.get(MEASUREMENT_DEF_ATTR_NAME);
  }

  public void setListener(MeasurementListener listener) {
    this.listener = listener;
  }

  public void setMeasurementDefinition(MeasurementDefinition measurementDefinition) {
    this.measurementDefinition = measurementDefinition;
  }
}
