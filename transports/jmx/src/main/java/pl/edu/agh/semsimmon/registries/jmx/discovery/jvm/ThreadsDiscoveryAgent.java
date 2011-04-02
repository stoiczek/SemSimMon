package pl.edu.agh.semsimmon.registries.jmx.discovery.jvm;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.DiscoveryAgent;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 21:19 11-07-2010
 */
public class ThreadsDiscoveryAgent implements DiscoveryAgent {

  @Override
  public List<Resource> discoveryChildren(MBeanServerConnection connection, Resource parent, String type) throws IOException {
    if (!parent.getTypeUri().equals(KnowledgeConstants.JVM_URI)) {
      throw new IllegalArgumentException("Got invalid parent resource type: " + parent.getTypeUri());
    }
    final ThreadMXBean threadProxy = ManagementFactory.newPlatformMXBeanProxy
        (connection, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
    ThreadInfo[] threadsDetails = threadProxy.getThreadInfo(threadProxy.getAllThreadIds());
    final List<Resource> threads = new LinkedList<Resource>();
    for (ThreadInfo threadDetails : threadsDetails) {
      threads.add(getThreadDetails(threadProxy, threadDetails, parent));
    }
    return threads;
  }


  private Resource getThreadDetails(ThreadMXBean threadProxy, ThreadInfo info, Resource parent) {
    final Map<String, Object> props = new HashMap<String, Object>();
    props.put(ResourcePropertyNames.Thread.ID, info.getThreadId());
    props.put(ResourcePropertyNames.Thread.NAME, info.getThreadName());
    return new Resource(parent.getUri() + "/Thread_" + info.getThreadId(), KnowledgeConstants.THREAD_URI, props);
  }
}
