package pl.edu.agh.semsimmon.registries.jmx.discovery;

import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.node.CPUDiscoveryAgent;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:31 11-07-2010
 */
public class CpuDiscoveryAgentTest {


  @Test
  public void testDiscovery() throws IOException {
    CPUDiscoveryAgent discoveryAgent = new CPUDiscoveryAgent();
    MBeanServerConnection conn = ManagementFactory.getPlatformMBeanServer();
    Resource parent = new Resource("semsimmon://localhost/app1/clu1/node1", KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> cpu = discoveryAgent.discoveryChildren(conn, parent, "");
    assertEquals(cpu.size(), ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
  }
}
