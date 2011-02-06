package pl.edu.agh.semmon.core.measurement;

import org.apache.commons.lang.math.RandomUtils;
import org.mockito.Matchers;
import org.quartz.*;
import org.testng.annotations.Test;
import pl.edu.agh.semmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semmon.common.exception.MeasurementException;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semmon.core.testsupport.MocksFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Test clas for PollJob class.
 *
 * @author tkozak
 *         Created at 16:24 14-08-2010
 */
public class PollJobTest {


  @Test
  public void doTest() throws SchedulerException, MeasurementException {
    PollJob job = new PollJob();
    JobExecutionContext context = mock(JobExecutionContext.class);
    Map params = new HashMap();
    JobDataMap jobMap = new JobDataMap(params);
    MeasurementDefinition def = MocksFactory.createMockMeasurementDefinition();
    MeasurementListener listener = mock(MeasurementListener.class);
    Scheduler scheduler = mock(Scheduler.class);
    CoreMeasurementService service = mock(CoreMeasurementService.class);
    SchedulerContext schedulerContext = new SchedulerContext(Collections.singletonMap(PollJob.MEASUREMENT_SERVICE_ATTR_NAME, service));
    jobMap.put(PollJob.LISTENER_ATTR_NAME, listener);
    jobMap.put(PollJob.MEASUREMENT_DEF_ATTR_NAME, def);
    jobMap.put(PollJob.MEASUREMENT_SERVICE_ATTR_NAME, service);
    when(context.getMergedJobDataMap()).thenReturn(jobMap);
    when(context.getScheduler()).thenReturn(scheduler);
    when(scheduler.getContext()).thenReturn(schedulerContext);
    CapabilityValue capValue = new CapabilityValue(RandomUtils.nextLong());
    when(service.getCapabilityValue(Matchers.<String>any(), Matchers.<String>any())).thenReturn(capValue);
    job.execute(context);
    verify(context).getMergedJobDataMap();
    verify(service).getCapabilityValue(def.getResourceUri(), def.getCapabilityUri());
    verify(listener).newCapabilityValue(capValue);
    assertEquals(capValue.getMetricsId(), def.getId());
  }


}
