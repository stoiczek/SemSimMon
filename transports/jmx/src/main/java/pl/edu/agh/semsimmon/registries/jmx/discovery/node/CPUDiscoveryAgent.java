package pl.edu.agh.semsimmon.registries.jmx.discovery.node;

import com.sun.management.OperatingSystemMXBean;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.DiscoveryAgent;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 13:53 11-07-2010
 */
public class CPUDiscoveryAgent implements DiscoveryAgent {

  @Override
  public List<Resource> discoveryChildren(MBeanServerConnection connection, Resource parent, String type) throws IOException {

    OperatingSystemMXBean osProxy = ManagementFactory.newPlatformMXBeanProxy
        (connection, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);

    int processorsCount = osProxy.getAvailableProcessors();
    final List<Resource> cpus = new LinkedList<Resource>();
    for (int i = 0; i < processorsCount; i++) {
      cpus.add(createCPUResource(parent, i));
    }
    return cpus;
  }

  private Resource createCPUResource(Resource parent, int i) {
    return new Resource(parent.getUri() + "/CPU_" + i, KnowledgeConstants.CPU_URI, Collections.<String, Object>emptyMap());
  }
}
