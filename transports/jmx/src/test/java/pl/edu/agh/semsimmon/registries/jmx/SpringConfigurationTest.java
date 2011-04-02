package pl.edu.agh.semsimmon.registries.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryEvent;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryListener;
import pl.edu.agh.semsimmon.common.api.transport.TransportException;
import pl.edu.agh.semsimmon.common.consts.JmxRegistryConsts;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import javax.management.MBeanServer;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

import static org.testng.Assert.*;
import static pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants.*;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 13:07 31-07-2010
 */

@ContextConfiguration(locations = {"classpath:jmxtransport-context.xml"})
public class SpringConfigurationTest extends AbstractTestNGSpringContextTests {

  private final String SERVICE_URI = "service:jmx:rmi://localhost:9994/jndi/rmi://localhost:4099/connector";

  @Autowired
  private JmxTransportProxy proxy;

  private Registry rmiRegistry;

  RMIConnectorServer rmiServer;

  private Map<String, Resource> resourceSamples = new HashMap<String, Resource>();

  @BeforeSuite
  public void setupRmiJmxService() throws IOException {
    rmiRegistry = LocateRegistry.createRegistry(4099);
    MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    String svc = SERVICE_URI;
    JMXServiceURL url = new JMXServiceURL(svc);
    rmiServer = new RMIConnectorServer(url, null, mbeanServer);
    rmiServer.start();
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

  @AfterSuite
  public void tearDownJmxService() throws IOException {
    rmiServer.stop();
  }

  @Test
  public void registerDeregisterTest() throws TransportException {
    final Map<String, Object> props = Collections.<String, Object>singletonMap(JmxRegistryConsts.SERVICE_URL_PROPERTY, SERVICE_URI);
    Resource testNodeResource = new Resource("semsimmon://localhost/app1/clu1/node1", NODE_URI, props);
    proxy.registerResource(testNodeResource);
    proxy.unregisterResource(testNodeResource);
  }


  @Test
  public void discoveryTest() throws TransportException {
    final Map<String, Object> props = Collections.<String, Object>singletonMap(JmxRegistryConsts.SERVICE_URL_PROPERTY, SERVICE_URI);
    Resource testNodeResource = new Resource("semsimmon://localhost/app1/clu1/node1", NODE_URI, props);
    resourceSamples.put(NODE_URI, testNodeResource);
    proxy.registerResource(testNodeResource);

    final List<String> nodeChildren = Arrays.asList(JVM_URI, CPU_URI,
        VIRTUAL_MEMORY_URI,
        PHYSICAL_MEMORY_URI,
        HARD_DRIVE_URI,
        OS_URI, NETWORK_INTERFACE_URI);
    proxy.discoverChildren(testNodeResource, nodeChildren);
    for (String nodeChildType : nodeChildren) {
      assertTrue(resourceSamples.containsKey(nodeChildType), "Resource of type: " + nodeChildType + " haven't been discovered");
    }
    final List<String> jvmChildTypes = Arrays.asList(THREAD_URI, CLASS_LOADER_URI, GC_URI);
    proxy.discoverChildren(resourceSamples.get(JVM_URI), jvmChildTypes);
    for (String nodeChildType : jvmChildTypes) {
      assertTrue(resourceSamples.containsKey(nodeChildType), "Resource of type: " + nodeChildType + " haven't been discovered");
    }
  }

  // disabled as I don't have such a probe currently

  @Test(dependsOnMethods = "discoveryTest", enabled = false)
  public void nodeProbeTest() throws Exception {
    Resource nodeRes = resourceSamples.get(NODE_URI);
    CapabilityValue value = proxy.getCapabilityValue(nodeRes, UPTIME_CAP);
    assertFalse(value == null, "Got null result");
    assertTrue(value.getNumericValue().longValue() > 0);
  }

  @Test(dependsOnMethods = "discoveryTest")
  public void jvmProbeTest() throws Exception {
    Resource nodeRes = resourceSamples.get(JVM_URI);
    String[] capabilities = new String[]{
        CLASSES_LOADED_TOTAL_CAP, TOTAL_COMPILATION_TIME_CAP, OPEN_FILE_DESCRIPTOR_CAP, LIVE_THREADS_CAP,
        HEAP_USAGE_CAP, STARTED_THREADS_CAP, NON_HEAP_USAGE_CAP, CLASSES_UNLOADED_TOTAL_CAP,
        TOTAL_CPU_TIME_CAP, UPTIME_CAP, LIVE_THREADS_CAP, CLASSES_LOADED_CAP
    };
    checkCapabilities(nodeRes, capabilities);
  }

  @Test(dependsOnMethods = "discoveryTest")
  public void threadProbeTest() throws TransportException {
    Resource threadRes = resourceSamples.get(THREAD_URI);
    String[] capabilities = new String[]{
        THREAD_USER_TIME_CAP, THREAD_CPU_TIME_CAP, THREAD_BLOCKED_COUNT_CAP, THREAD_BLOCKED_TIME_CAP,
        THREAD_WAITED_COUNT_CAP, THREAD_WAITED_COUNT_CAP
    };
    checkCapabilities(threadRes, capabilities);

  }


  @Test(dependsOnMethods = "discoveryTest")
  public void gcProbeTest() throws TransportException {
    Resource threadRes = resourceSamples.get(GC_URI);
    String[] capabilities = new String[]{
        GC_COUNT_CAP, GC_TIME_CAP
    };
    checkCapabilities(threadRes, capabilities);

  }

  private void checkCapabilities(Resource nodeRes, String[] capabilities) throws TransportException {
    for (String capabilityType : capabilities) {
      CapabilityValue value = proxy.getCapabilityValue(nodeRes, capabilityType);
      assertNotNull(value, "Got null result");
      if (value.getValueType() == CapabilityValue.ValueType.NUMERIC) {
        assertNotNull(value.getNumericValue());
      }
    }
  }


  public void setProxy(JmxTransportProxy proxy) {
    this.proxy = proxy;
  }
}
