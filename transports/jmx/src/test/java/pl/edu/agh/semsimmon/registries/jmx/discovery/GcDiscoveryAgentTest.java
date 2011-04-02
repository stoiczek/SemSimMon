package pl.edu.agh.semsimmon.registries.jmx.discovery;

import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.jvm.GCDiscoveryAgent;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 19:40 19-07-2010
 */
public class GcDiscoveryAgentTest {

  @Test
  public void discoveryTest() throws IOException {
    GCDiscoveryAgent discoveryAgent = new GCDiscoveryAgent();
    MBeanServerConnection conn = ManagementFactory.getPlatformMBeanServer();
    Resource parent = new Resource("semsimmon://localhost/app1/clu1/node1/jvm_123", KnowledgeConstants.JVM_URI, Collections.<String, Object>emptyMap());
    List<Resource> gcs = discoveryAgent.discoveryChildren(conn, parent, "");
    for(Resource gc : gcs) {
      assertTrue(gc.hasProperty(ResourcePropertyNames.GarbageCollector.NAME));
    }
    assertEquals(gcs.size(), ManagementFactory.getGarbageCollectorMXBeans().size());
  }


}
