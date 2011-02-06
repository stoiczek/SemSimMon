package pl.edu.agh.semmon.registries.jmx.probe.thread;

import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.jmx.probe.CapabilityProbe;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 15:13 25-07-2010
 */
public class ThreadTimingProbe implements CapabilityProbe {

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri, MBeanServerConnection connection) throws IOException {
    if(!resource.getTypeUri().equals(KnowledgeConstants.THREAD_URI)) {
      throw new IllegalArgumentException("Got invalid resource type - this probe supports only threads");
    }
    if(!resource.getProperties().containsKey(ResourcePropertyNames.Thread.ID)) {
      throw new IllegalArgumentException("Got invalid resource. This probe can return thread's user time of properly " +
          "discovered thread. Missing thread id resource property");
    }
    ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(connection, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
    long threadId = (Long) resource.getProperty(ResourcePropertyNames.Thread.ID);
    long time = 0;
    if(capabilityUri.equals(KnowledgeConstants.THREAD_USER_TIME_CAP)) {
      time =  threadMXBean.getThreadUserTime(threadId);
    } else if(capabilityUri.equals(KnowledgeConstants.THREAD_CPU_TIME_CAP)) {
      time = threadMXBean.getThreadCpuTime(threadId);
    }
    return new CapabilityValue(time);
  }
}
