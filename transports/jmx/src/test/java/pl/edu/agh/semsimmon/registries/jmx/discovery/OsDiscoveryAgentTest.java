package pl.edu.agh.semsimmon.registries.jmx.discovery;

import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.node.OsDiscoveryAgent;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 12:15 11-07-2010
 */
public class OsDiscoveryAgentTest {


  @Test
  public void testDiscovery() throws IOException {
    System.getProperties().list(System.out);

    OsDiscoveryAgent discoveryAgent = new OsDiscoveryAgent();
    MBeanServerConnection conn = ManagementFactory.getPlatformMBeanServer();
    Resource parent = new Resource("semsimmon://localhost/app1/clu1/node1", KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> os = discoveryAgent.discoveryChildren(conn, parent, "");
    assertEquals(1, os.size());
    Resource osResource = os.get(0);
    assertTrue(osResource.getUri().endsWith("OperatingSystem"));
    Map<String, Object> props = osResource.getProperties();
    assertTrue(props.containsKey(ResourcePropertyNames.Os.NAME));
    assertTrue(props.containsKey(ResourcePropertyNames.Os.ARCHITECTURE));
    assertTrue(props.containsKey(ResourcePropertyNames.Os.VERSION));
  }

}
