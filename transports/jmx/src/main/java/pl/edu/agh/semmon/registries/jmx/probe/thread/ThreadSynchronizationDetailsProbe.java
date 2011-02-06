package pl.edu.agh.semmon.registries.jmx.probe.thread;

import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.jmx.probe.CapabilityProbe;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 15:21 25-07-2010
 */
public class ThreadSynchronizationDetailsProbe implements CapabilityProbe {

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri, MBeanServerConnection connection) throws IOException {
    if (!resource.getTypeUri().equals(KnowledgeConstants.THREAD_URI)) {
      throw new IllegalArgumentException("Got invalid resource type - this probe supports only threads");
    }
    if (!resource.getProperties().containsKey(ResourcePropertyNames.Thread.ID)) {
      throw new IllegalArgumentException("Got invalid resource. This probe can return thread's user time of properly " +
          "discovered thread. Missing thread id resource property");
    }
    final ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(connection, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
    final ThreadInfo info = threadMXBean.getThreadInfo((Long) resource.getProperty(ResourcePropertyNames.Thread.ID));
    long value = -1;
    if (capabilityUri.equals(KnowledgeConstants.THREAD_BLOCKED_COUNT_CAP)) {
      value = info.getBlockedCount();
    } else if (capabilityUri.equals(KnowledgeConstants.THREAD_BLOCKED_TIME_CAP)) {
      value = info.getBlockedTime();
    } else if (capabilityUri.equals(KnowledgeConstants.THREAD_WAITED_COUNT_CAP)) {
      value = info.getWaitedCount();
    } else if (capabilityUri.equals(KnowledgeConstants.THREAD_WAITED_TIME_CAP)) {
      value = info.getWaitedTime();
    } else {
      throw new IllegalArgumentException("Got invalid capability uri: " + capabilityUri);
    }
    return new CapabilityValue(value);
  }
}
