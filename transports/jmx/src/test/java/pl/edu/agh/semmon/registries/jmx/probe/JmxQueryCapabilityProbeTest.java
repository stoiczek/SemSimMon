package pl.edu.agh.semmon.registries.jmx.probe;

import org.testng.Assert;
import org.testng.annotations.Test;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Collections;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 19:12 25-07-2010
 */
public class JmxQueryCapabilityProbeTest {


  @Test
  public void doTest() throws IOException {
    JmxQueryCapabilityProbe probe = new JmxQueryCapabilityProbe();
    JmxQuery query = new JmxQuery();
    query.setObjectName(ManagementFactory.RUNTIME_MXBEAN_NAME);
    query.setOperationName("Uptime");
    probe.setQueries(Collections.singletonMap(KnowledgeConstants.UPTIME_CAP, query));
    Resource mockJVMResource = new Resource("semmon://localhost/app1/clu1/node1/jvm1", KnowledgeConstants.JVM_URI, Collections.<String, Object>emptyMap());
    RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
    long expectedUptime = runtimeBean.getUptime();
    CapabilityValue value =  probe.getCapabilityValue(mockJVMResource, KnowledgeConstants.UPTIME_CAP, ManagementFactory.getPlatformMBeanServer());
    Assert.assertTrue(expectedUptime - value.getNumericValue().longValue() < 500);
  }

}
