package pl.edu.agh.semsimmon.registries.jmx.probe.gc;

import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.probe.CollectionsProbe;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 19:21 25-07-2010
 */
public class CollectionsProbeTest {

  @Test
  public void doTest() throws IOException {
    CollectionsProbe probe = new CollectionsProbe();
    GarbageCollectorMXBean gcBean = ManagementFactory.getGarbageCollectorMXBeans().get(0);
    String gcName = gcBean.getName();
    Resource mockGcResource = new Resource("semsimmon://localhost/app1/clu1/node1/jvm1/" + gcName,
        KnowledgeConstants.GC_URI,
        Collections.<String, Object>singletonMap(ResourcePropertyNames.GarbageCollector.NAME, gcName));
    final MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
    CapabilityValue countValue = probe.getCapabilityValue(mockGcResource, KnowledgeConstants.GC_COUNT_CAP, beanServer);
    CapabilityValue timeValue = probe.getCapabilityValue(mockGcResource, KnowledgeConstants.GC_TIME_CAP, beanServer);
    // TODO think how to verify this??
  }

}
