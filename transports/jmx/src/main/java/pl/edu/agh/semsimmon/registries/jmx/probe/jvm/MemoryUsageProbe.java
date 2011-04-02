package pl.edu.agh.semsimmon.registries.jmx.probe.jvm;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.probe.CapabilityProbe;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 21:22 19-07-2010
 */
public class MemoryUsageProbe implements CapabilityProbe {

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri, MBeanServerConnection connection) throws IOException {
    if(!resource.getTypeUri().equals(KnowledgeConstants.JVM_URI)) {
      throw new IllegalArgumentException("Invalid resource type. Only JVM resource is supported.");
    }
    final MemoryMXBean memoryMXBean = ManagementFactory.
        newPlatformMXBeanProxy(connection,ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
    MemoryUsage usage;
    if(capabilityUri.equals(KnowledgeConstants.HEAP_USAGE_CAP)) {
      usage = memoryMXBean.getHeapMemoryUsage();
    } else if(capabilityUri.equals(KnowledgeConstants.NON_HEAP_USAGE_CAP)) {
      usage = memoryMXBean.getNonHeapMemoryUsage();
    } else {
      throw new IllegalArgumentException("Unsupported capability uri: " + capabilityUri);
    }
    return new CapabilityValue(usage.getUsed());
  }


}
