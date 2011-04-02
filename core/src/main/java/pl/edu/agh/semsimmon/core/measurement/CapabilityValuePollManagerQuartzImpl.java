package pl.edu.agh.semsimmon.core.measurement;

import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import pl.edu.agh.semsimmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semsimmon.common.exception.MeasurementException;
import pl.edu.agh.semsimmon.common.vo.core.measurement.MeasurementDefinition;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.text.ParseException;

/**
 * Quartz based implementation of CapabilityValuePollManagerQuartz
 *
 * @author tkozak
 *         Created at 12:43 14-08-2010
 */
public class CapabilityValuePollManagerQuartzImpl implements CapabilityValuePollManager {

  private static final Logger log = LoggerFactory.getLogger(CapabilityValuePollManagerQuartzImpl.class);

  private static final String POLLING_JOB_GROUP_NAME = "semsimmon.measurement.polling";
  private static final String POLLING_JOB_NAME_PREFIX = "POLL_";
  private static final String CRON_EXPR_PATTERN = "0/{0} * * * * ?";

  

  /**
   * For scheduling actual poll tasks.
   */
  private Scheduler scheduler;

  private CoreMeasurementService coreMeasurementService;

  private ApplicationContext applicationContext;

  /*
  * ===================================================================================================================
  * CapabilityValuePollManager implementation
  * ===================================================================================================================
  */

  /**
   * {@inheritDoc}
   */
  @Override
  public void submitPollJob(MeasurementDefinition measurementDefinition, MeasurementListener listener)
      throws MeasurementException {
    try {
      final String name = getJobName(measurementDefinition);
      final String cronExpression = MessageFormat.format(CRON_EXPR_PATTERN, measurementDefinition.getUpdateInterval());
      final Trigger jobTrigger = new CronTrigger(name, POLLING_JOB_GROUP_NAME, cronExpression);
      final JobDetail job = createJobDetails(measurementDefinition, listener, name);
      scheduler.scheduleJob(job, jobTrigger);
    } catch (SchedulerException e) {
      log.error("Error scheduling polling job", e);
      throw new MeasurementException("Error creating capability valu polling job. Reason: " + e.getMessage());
    } catch (ParseException e) {
      log.error("Error scheduling polling job", e);
      throw new MeasurementException("Error creating capability valu polling job. Reason: " + e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelPollJob(MeasurementDefinition measurementDefinition) throws MeasurementException {
    try {
      scheduler.deleteJob(getJobName(measurementDefinition), POLLING_JOB_GROUP_NAME);
    } catch (SchedulerException e) {
      log.error("Error scheduling polling job", e);
      throw new MeasurementException("Error creating capability valu polling job. Reason: " + e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePollJob(MeasurementDefinition measurementDefinition) throws MeasurementException {
    try {
      final String jobName = getJobName(measurementDefinition);
      JobDetail jobDetail = scheduler.getJobDetail(jobName, POLLING_JOB_GROUP_NAME);
      final MeasurementListener listener = (MeasurementListener)
          jobDetail.getJobDataMap().get(PollJob.LISTENER_ATTR_NAME);
      scheduler.deleteJob(jobName, POLLING_JOB_GROUP_NAME);
      submitPollJob(measurementDefinition, listener);
    } catch (SchedulerException e) {
      log.error("Error updating polling job", e);
      throw new MeasurementException("Error updating capability value polling job. Reason: " + e.getMessage());
    }
  }

/*
* ===================================================================================================================
* Private utilities, spring init
* ===================================================================================================================
*/

  private JobDetail createJobDetails(MeasurementDefinition measurementDefinition, MeasurementListener listener, String name) {
    final JobDetail job = new JobDetail();
    job.setGroup(POLLING_JOB_GROUP_NAME);
    job.setName(name);
    job.setJobClass(PollJob.class);
    JobDataMap jobResources = new JobDataMap();
    jobResources.put(PollJob.LISTENER_ATTR_NAME, listener);
    jobResources.put(PollJob.MEASUREMENT_DEF_ATTR_NAME, measurementDefinition);
    job.setJobDataMap(jobResources);
    return job;
  }


  private String getJobName(MeasurementDefinition measurementDefinition) {
    return POLLING_JOB_NAME_PREFIX + measurementDefinition.getId();
  }

  @PostConstruct
  public void setupScheduler() throws SchedulerException {
    DirectSchedulerFactory.getInstance().createVolatileScheduler(20);
    scheduler = DirectSchedulerFactory.getInstance().getScheduler();
    scheduler.start();
    scheduler.getContext().put(PollJob.MEASUREMENT_SERVICE_ATTR_NAME, coreMeasurementService);
  }

  public void setCoreMeasurementService(CoreMeasurementService coreMeasurementService) {
    this.coreMeasurementService = coreMeasurementService;
  }
}
