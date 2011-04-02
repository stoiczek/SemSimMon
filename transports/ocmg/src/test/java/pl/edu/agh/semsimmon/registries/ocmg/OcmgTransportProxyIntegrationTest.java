package pl.edu.agh.semsimmon.registries.ocmg;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryEvent;
import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryListener;
import pl.edu.agh.semsimmon.common.api.transport.TransportException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;
import static pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants.*;


/**
 * TODO description
 *
 * @author tkozak
 *         Created at 18:57 29-08-2010
 */
public class OcmgTransportProxyIntegrationTest extends OcmgRequringTest {

  @Autowired
  private OcmgTransportProxyImpl proxy;


  private Map<String, Resource> resourceSamples = new HashMap<String, Resource>();

  @BeforeClass
  public void setupListener() {
    proxy.addResourceDiscoveryListener(new IResourceDiscoveryListener() {
      @Override
      public void processEvent(IResourceDiscoveryEvent event) {
        if (event.getEventType() == IResourceDiscoveryEvent.EventType.NEW_RESOURCES_DISCOVERED) {
          for (Resource res : event.getResources()) {
            try {
              proxy.registerResource(res);
              resourceSamples.put(res.getTypeUri(), res);
            } catch (TransportException e) {
              ByteArrayOutputStream bar = new ByteArrayOutputStream();
              e.printStackTrace(new PrintWriter(bar));
              fail("Got transport exception during resource registry: " + bar.toString());
            }
          }
        }
      }
    });
  }

  @Test
  public void discoveryAppsTest() throws Exception {
    final Resource rootResource = createTestResource();
    List<Resource> apps = proxy.discoverDirectChildren(rootResource, APPLICATION_URI);
    assertEquals(apps.size(), 1);
    resourceSamples.put(APPLICATION_URI, apps.get(0));
    proxy.registerResource(apps.get(0));
  }

  @Test(dependsOnMethods = "discoveryAppsTest")
  public void clustersDiscoveryTest() throws Exception {
    doDiscoveryTest(APPLICATION_URI, CLUSTER_URI);
  }

  @Test(dependsOnMethods = "clustersDiscoveryTest")
  public void nodesDiscoveryTest() throws Exception {
    doDiscoveryTest(CLUSTER_URI, NODE_URI);
  }

  @Test(dependsOnMethods = "nodesDiscoveryTest")
  public void osDiscoveryTest() throws Exception {
    doDiscoveryTest(NODE_URI, OS_URI);
  }

  @Test(dependsOnMethods = "nodesDiscoveryTest")
  public void pmDiscoveryTest() throws Exception {
    doDiscoveryTest(NODE_URI, PHYSICAL_MEMORY_URI);
  }

  @Test(dependsOnMethods = "nodesDiscoveryTest")
  public void vmDiscoveryTest() throws Exception {
    doDiscoveryTest(NODE_URI, VIRTUAL_MEMORY_URI);
  }

  @Test(dependsOnMethods = "nodesDiscoveryTest")
  public void cpuDiscoveryTest() throws Exception {
    doDiscoveryTest(NODE_URI, CPU_URI);
  }

  @Test(dependsOnMethods = "nodesDiscoveryTest")
  public void storageDiscoveryTest() throws Exception {
    doDiscoveryTest(NODE_URI, HARD_DRIVE_URI);
  }

  @Test(dependsOnMethods = "nodesDiscoveryTest")
  public void netifaceDiscoveryTest() throws Exception {
    doDiscoveryTest(NODE_URI, NETWORK_INTERFACE_URI);
  }

  @Test(dependsOnMethods = "nodesDiscoveryTest")
  public void processDiscoveryTest() throws Exception {
    doDiscoveryTest(NODE_URI, PROCESS_URI);
  }

  @Test(dependsOnMethods = "processDiscoveryTest")
  public void threadDiscoveryTest() throws Exception {
    doDiscoveryTest(PROCESS_URI, THREAD_URI);
  }


//  @Test(dependsOnMethods = "processDiscoveryTest")
  public void functionDiscoveryTest() throws Exception {
    doDiscoveryTest(PROCESS_URI, FUNCTION_URI);
  }


  private void doDiscoveryTest(String parentUri, String childUri) throws TransportException {
    final Resource app = resourceSamples.get(parentUri);
    proxy.discoverChildren(app, Arrays.asList(childUri));
    assertTrue(resourceSamples.containsKey(childUri));
    proxy.registerResource(resourceSamples.get(childUri));
  }

  public void setProxy(OcmgTransportProxyImpl proxy) {
    this.proxy = proxy;
  }
}
