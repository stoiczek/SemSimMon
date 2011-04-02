package pl.edu.agh.semsimmon.registries.jmx.probe.memory;

import org.testng.Assert;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.probe.ProbeTestUtils;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Collections;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:15 25-07-2010
 */
public class MemoryProbeTest {

  @Test
  public void doTest() throws IOException {
    MemoryProbe memoryProbe = new MemoryProbe();
    Resource physicalMem = new Resource("semsimmon://localhost/app1/clu1/node1/PhysicalMemory",
        KnowledgeConstants.PHYSICAL_MEMORY_URI, Collections.<String, Object>emptyMap());
    Resource virtualMem = new Resource("semsimmon://localhost/app1/clu1/node1/VirtualMemory",
        KnowledgeConstants.VIRTUAL_MEMORY_URI, Collections.<String, Object>emptyMap());
    final MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
    OperatingSystemMXBean osBeanGlob = ManagementFactory.getOperatingSystemMXBean();
    com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) osBeanGlob;

    final long totalPhysical = osBean.getTotalPhysicalMemorySize();
    final long totalSwap = osBean.getTotalSwapSpaceSize();
    CapabilityValue value = memoryProbe.getCapabilityValue(physicalMem, KnowledgeConstants.TOTAL_MEM_CAP, beanServer);
    Assert.assertEquals(value.getNumericValue().longValue(), totalPhysical);
    value = memoryProbe.getCapabilityValue(virtualMem, KnowledgeConstants.TOTAL_MEM_CAP, beanServer);
    Assert.assertEquals(value.getNumericValue().longValue(), totalSwap);

    final long freePhysical = osBean.getFreePhysicalMemorySize();
    final long freeSwap = osBean.getFreeSwapSpaceSize();
    value = memoryProbe.getCapabilityValue(physicalMem, KnowledgeConstants.FREE_MEM_CAP, beanServer);
    ProbeTestUtils.verifyWithError(freePhysical, value, 5);
    value = memoryProbe.getCapabilityValue(virtualMem, KnowledgeConstants.FREE_MEM_CAP, beanServer);
    ProbeTestUtils.verifyWithError(freeSwap, value, 5);

    value = memoryProbe.getCapabilityValue(physicalMem, KnowledgeConstants.USED_MEM_CAP, beanServer);
    ProbeTestUtils.verifyWithError(totalPhysical - freePhysical, value, 5);
    value = memoryProbe.getCapabilityValue(virtualMem, KnowledgeConstants.USED_MEM_CAP, beanServer);
    ProbeTestUtils.verifyWithError(totalSwap - freeSwap, value, 5);
  }

}
