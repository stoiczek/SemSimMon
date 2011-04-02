package pl.edu.agh.semsimmon.registries.jmx.discovery;

import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:46 11-07-2010
 */
public class HardwareDiscoveryAgentTest {


  @Test
  public void testDiscovery() throws IOException {
    GenericDiscoveryAgent discoveryAgent = new GenericDiscoveryAgent();
    MBeanServerConnection conn = ManagementFactory.getPlatformMBeanServer();
    Resource parent = new Resource("semsimmon://localhost/app1/clu1/node1", KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());

    String types[] = new String[]{
        KnowledgeConstants.PHYSICAL_MEMORY_URI,
        KnowledgeConstants.VIRTUAL_MEMORY_URI,
        KnowledgeConstants.NETWORK_INTERFACE_URI,
        KnowledgeConstants.HARD_DRIVE_URI
    };
    for (String type : types) {
      checkType(discoveryAgent, conn, parent, type);
    }

  }

  private void checkType(GenericDiscoveryAgent discoveryAgent, MBeanServerConnection conn, Resource parent, String type) throws IOException {
    List<Resource> resources = discoveryAgent.discoveryChildren(conn, parent, type);
    assertEquals(resources.size(), 1);
    assertTrue(resources.get(0).getUri().endsWith(type.substring(type.lastIndexOf('#') + 1)));
  }
}
