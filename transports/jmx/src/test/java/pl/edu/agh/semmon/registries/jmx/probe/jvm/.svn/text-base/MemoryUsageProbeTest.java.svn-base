package pl.edu.agh.semmon.registries.jmx.probe.jvm;

import org.testng.annotations.Test;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.jmx.probe.ProbeTestUtils;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Collections;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 19:46 25-07-2010
 */
public class MemoryUsageProbeTest {

  @Test
  public void doTest() throws IOException {
    MemoryUsageProbe probe = new MemoryUsageProbe();
    Resource mockJvmResource = new Resource("semmon://localhost/app1/clu1/node1/jvm1", KnowledgeConstants.JVM_URI, Collections.<String, Object>emptyMap());
    final MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
    MemoryMXBean mMbean = ManagementFactory.getMemoryMXBean();
    long heapExpected = mMbean.getHeapMemoryUsage().getUsed();
    long nonHeapExpected = mMbean.getNonHeapMemoryUsage().getUsed();
    CapabilityValue heapMem = probe.getCapabilityValue(mockJvmResource, KnowledgeConstants.HEAP_USAGE_CAP, platformMBeanServer);
    CapabilityValue nonHeapMem = probe.getCapabilityValue(mockJvmResource, KnowledgeConstants.NON_HEAP_USAGE_CAP, platformMBeanServer);
    ProbeTestUtils.verifyWithError(heapExpected, heapMem, 5);
    ProbeTestUtils.verifyWithError(nonHeapExpected, nonHeapMem, 5);
  }

  


}
