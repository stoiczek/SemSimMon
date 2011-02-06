package pl.edu.agh.semmon.core.measurement;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.edu.agh.semmon.common.exception.MeasurementException;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semmon.core.testsupport.MocksFactory;
import pl.edu.agh.semmon.core.transport.TransportProxiesManager;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static pl.edu.agh.semmon.core.testsupport.MocksFactory.createMockMeasurementDefinition;

/**
 * Test class for CapabilityValuePollManagerQuartzImpl class.
 *
 * @author tkozak
 *         Created at 14:37 14-08-2010
 */
public class CapabilityValuePollMangerTest {

  @Mock
  private Scheduler scheduler;

  @Mock
  private TransportProxiesManager proxiesManager;

  @InjectMocks
  private CapabilityValuePollManagerQuartzImpl pollManager;
  private static final String SEMMON_MEASUREMENT_POLLING = "semmon.measurement.polling";

  @BeforeMethod
  void setupMocks() {
    pollManager = new CapabilityValuePollManagerQuartzImpl();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void submitJobTest() throws MeasurementException, SchedulerException {
    MeasurementDefinition def = createMockMeasurementDefinition();
    MeasurementListener listener = createFakedListener();
    pollManager.submitPollJob(def, listener);
    verify(scheduler).scheduleJob(argThat(createJobDetailsMatcher(def, listener)), argThat(createTriggerMatcher(def)));
  }


  @Test
  public void updateJobTest() throws SchedulerException, MeasurementException {
    MeasurementDefinition oldDef = createMockMeasurementDefinition();
    MeasurementListener oldListener = createFakedListener();
    pollManager.submitPollJob(oldDef, oldListener);
    MeasurementDefinition def = createMockMeasurementDefinition();
    def.setId(oldDef.getId());
    final String jobName = "POLL_" + def.getId();

    JobDetail oldJobDetail = new JobDetail(jobName, SEMMON_MEASUREMENT_POLLING, PollJob.class);
    oldJobDetail.setJobDataMap(new JobDataMap(Collections.singletonMap(PollJob.LISTENER_ATTR_NAME, oldListener)));
    when(scheduler.getJobDetail(Matchers.<String>any(), Matchers.<String>any())).thenReturn(oldJobDetail);

    pollManager.updatePollJob(def);

    verify(scheduler).getJobDetail(jobName, SEMMON_MEASUREMENT_POLLING);
    verify(scheduler).deleteJob(jobName, SEMMON_MEASUREMENT_POLLING);
    verify(scheduler).scheduleJob(argThat(createJobDetailsMatcher(def, oldListener)), argThat(createTriggerMatcher(def)));
  }

  @Test
  public void cancelJobTest() throws MeasurementException, SchedulerException {
    MeasurementDefinition def = MocksFactory.createMockMeasurementDefinition();
    pollManager.cancelPollJob(def);
    verify(scheduler).deleteJob("POLL_" + def.getId(), SEMMON_MEASUREMENT_POLLING);

  }

  private MeasurementListener createFakedListener() {
    return new MeasurementListener() {
      @Override
      public void newCapabilityValue(CapabilityValue value) {
        //To change body of implemented methods use File | Settings | File Templates.
      }
    };
  }

  private BaseMatcher<Trigger> createTriggerMatcher(final MeasurementDefinition def) {
    return new BaseMatcher<Trigger>() {
      @Override
      public boolean matches(Object o) {
        CronTrigger trigger = (CronTrigger) o;
        if (!trigger.getCronExpression().equals("0/" + def.getUpdateInterval() + " * * * * ?")) {
          return false;
        }
        if (!trigger.getName().equals("POLL_" + def.getId())) {
          return false;
        }
        if (!trigger.getGroup().equals(SEMMON_MEASUREMENT_POLLING)) {
          return false;
        }
        return true;
      }

      @Override
      public void describeTo(Description description) {

      }
    };
  }

  private BaseMatcher<JobDetail> createJobDetailsMatcher(final MeasurementDefinition def, final MeasurementListener listener) {
    return new BaseMatcher<JobDetail>() {
      @Override
      public boolean matches(Object o) {
        JobDetail jobDetail = (JobDetail) o;
        if (!jobDetail.getJobClass().equals(PollJob.class)) {
          return false;
        }
        if (!jobDetail.getName().equals("POLL_" + def.getId())) {
          return false;
        }
        if (!(jobDetail.getJobDataMap().get(PollJob.LISTENER_ATTR_NAME) == listener)) {
          return false;
        }
        final MeasurementDefinition measurementDef = (MeasurementDefinition) jobDetail.getJobDataMap().get(PollJob.MEASUREMENT_DEF_ATTR_NAME);
        if (!measurementDef.getId().equals(def.getId())) {
          return false;
        }
        return true;
      }

      @Override
      public void describeTo(Description description) {
        //To change body of implemented methods use File | Settings | File Templates.
      }


    };
  }


}
