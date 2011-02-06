package pl.edu.agh.semmon.registries.jmx.discovery;

import org.testng.annotations.Test;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.consts.JmxRegistryConsts;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.jmx.discovery.node.JvmDiscoveryAgent;

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
 *         Created at 15:23 11-07-2010
 */
public class JvmDiscoveryAgentTest {

  private static final String[] PROPS = new String[]{
      ResourcePropertyNames.Jvm.BOOT_CLASSPATH,
      ResourcePropertyNames.Jvm.CLASSPATH,
      ResourcePropertyNames.Jvm.INPUT_ARGS,
      ResourcePropertyNames.Jvm.LIB_PATH,
      ResourcePropertyNames.Jvm.NAME,
      ResourcePropertyNames.Jvm.SPEC_NAME,
      ResourcePropertyNames.Jvm.SPEC_VENDOR,
      ResourcePropertyNames.Jvm.SPEC_VERSION,
      ResourcePropertyNames.Jvm.VM_NAME,
      ResourcePropertyNames.Jvm.VM_VENDOR,
      ResourcePropertyNames.Jvm.VM_VERSION,
      ResourcePropertyNames.Jvm.START_TIME
  };

  @Test
  public void testDiscovery() throws IOException {
    JvmDiscoveryAgent discoveryAgent = new JvmDiscoveryAgent();
    MBeanServerConnection conn = ManagementFactory.getPlatformMBeanServer();
    Resource parent = new Resource("semmon://localhost/app1/clu1/node1", KnowledgeConstants.NODE_URI,
        Collections.<String, Object>singletonMap(JmxRegistryConsts.SERVICE_URL_PROPERTY,
            "service:jmx:rmi:///jndi/rmi://localhost:9024/jmxrmi"));
    List<Resource> jvms = discoveryAgent.discoveryChildren(conn, parent, "");
    assertEquals(jvms.size(), 1);
    Resource jvm = jvms.get(0);
    for (String property : PROPS) {
      assertTrue(jvm.getProperties().containsKey(property));
    }
  }


}
