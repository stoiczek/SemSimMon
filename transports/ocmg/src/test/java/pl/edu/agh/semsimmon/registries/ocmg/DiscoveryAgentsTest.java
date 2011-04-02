package pl.edu.agh.semsimmon.registries.ocmg;

import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.Node;
import org.balticgrid.ocmg.objects.Site;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.resource.*;
import pl.edu.agh.semsimmon.registries.ocmg.resource.node.*;

import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author tkozak
 *         Created at 56:16 24-05-2010
 * @TODO description
 */
public class DiscoveryAgentsTest extends OcmgRequringTest {

  private OcmgConnection ocmgConnection;

  private Resource testAppResource;

  private Application app;

  @BeforeClass
  public void setupOcmgConnection() throws OcmgException {
    Resource rootResource = createTestResource();
    testAppResource = new Resource(rootResource.getUri() + "/" + TEST_APP_NAME, KnowledgeConstants.APPLICATION_URI, rootResource.getProperties());
    testAppResource.setProperty(ResourcePropertyNames.Application.NAME, TEST_APP_NAME);
    ocmgConnection = new OcmgConnection(testAppResource);
    ocmgConnection.connect();
    app = ocmgConnection.attachToApplication(testAppResource);
  }


  @AfterClass
  public void disposeOcmgConnection() throws OcmgException {
    ocmgConnection.disconnect();
  }


  @Test
  public void clustersDiscoveringAgentTest() throws OcmgException {
    ResourceAgent agent = new ClustersResourceAgent();
    List<Resource> sites = agent.discoverChildResources(app, testAppResource, KnowledgeConstants.CLUSTER_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(sites.isEmpty());

  }

  @Test
  public void nodeDiscoveryTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    ResourceAgent agent = new NodeResourceAgent();
    Resource siteResource = new Resource(testAppResource.getUri() + "/" + siteID, KnowledgeConstants.CLUSTER_URI,
        Collections.<String, Object>singletonMap(ResourcePropertyNames.Cluster.ID, siteID));
    List<Resource> nodes = agent.discoverChildResources(app, siteResource, KnowledgeConstants.CLUSTER_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(nodes.isEmpty());
  }

  @Test
  public void processesDiscoveryAgentTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    ResourceAgent agent = new ProcessResourceAgent();
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> processes = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.CLUSTER_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(processes.isEmpty());
    for (Resource resource : processes) {
      assertTrue(resource.hasProperty(ResourcePropertyNames.RESOURCE_PAUSEABLE_PN));
      assertTrue((Boolean) resource.getProperty(ResourcePropertyNames.RESOURCE_PAUSEABLE_PN));
      assertTrue(resource.hasProperty(ResourcePropertyNames.Process.GLOBAL_ID));
      assertTrue(resource.hasProperty(ResourcePropertyNames.Process.ARGUMENTS));
      assertTrue(resource.hasProperty(ResourcePropertyNames.Process.GROUP_ID));
      assertTrue(resource.hasProperty(ResourcePropertyNames.Process.USER_ID));
    }
  }

  @Test
  public void threadDiscoveryAgentTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    final org.balticgrid.ocmg.objects.Process process = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getProcessList().get(0);
    process.attach();
    String processId = process.getCacheName();
    ResourceAgent agent = new ThreadResourceAgent();
    Resource processResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId + "/" + processId,
        KnowledgeConstants.PROCESS_URI, Collections.<String, Object>emptyMap());
    processResource.setProperty(ResourcePropertyNames.Process.GLOBAL_ID, process.getStaticInfo().getGlobalId());
    List<Resource> nodes = agent.discoverChildResources(app, processResource, KnowledgeConstants.THREAD_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(nodes.isEmpty());
  }

  @Test
  public void functionDiscoveryAgentTest() throws Exception {
//    Application app = ocmgConnection.attachToApplication(testAppResource);
//    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
//    site.attach();
//    String siteID = site.getCacheName();
//    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
//    node.attach();
//    String nodeId = node.getCacheName();
//    final org.balticgrid.ocmg.objects.Process process = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getProcessList().get(0);
//    process.attach();
//    String processId = process.getCacheName();
//    ProcessFunctionsResourceAgent agent = new ProcessFunctionsResourceAgent();
//
//    IFunctionMonitoringController controller = mock(IFunctionMonitoringController.class);
//    LibCallMetersContainer container = mock(LibCallMetersContainer.class);
//    agent.setMetersContainer(container);
//    agent.setFunctionMonitoringController(controller);
//    Resource processResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId + "/" + processId,
//        KnowledgeConstants.PROCESS_URI, Collections.<String, Object>emptyMap());
//    when(controller.functionInterestingForResource(processResource, "MPI_Send")).thenReturn(true);
//    List<Resource> nodes = agent.discoverChildResources(app, processResource, KnowledgeConstants.FUNCTION_URI);
//    ocmgConnection.detachFromApplication(testAppResource);
//    assertFalse(nodes.isEmpty());
//    verify(controller).functionInterestingForResource(processResource, "MPI_Send");
//    verify(container).addMeter(eq(nodes.get(0).getUri()), any(LibCallMeter.class));
//    System.out.println();
//    for (Resource resource : nodes) {
//      System.out.println(resource.getUri());
//
//    }
  }

  @Test
  public void OsDiscoveryAgentTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    ResourceAgent agent = new OSResourceAgent();
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> nodes = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.OS_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertEquals(nodes.size(), 1);
    Resource res = nodes.get(0);
    assertTrue(res.getProperty(ResourcePropertyNames.Os.NAME).toString().length() > 0);
    assertTrue(res.getProperty(ResourcePropertyNames.Os.RELEASE).toString().length() > 0);
    assertTrue(res.getProperty(ResourcePropertyNames.Os.VERSION).toString().length() > 0);
  }

  @Test
  public void CpuDiscoveryAgentTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    BasicHardwareResourceAgent agent = new BasicHardwareResourceAgent();
    agent.setChildResourceName("CPU");
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> nodes = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.OS_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertEquals(nodes.size(), 1);
    Resource res = nodes.get(0);
    assertTrue(res.getUri().endsWith("CPU"));
  }

  @Test
  public void storageDiscoveryTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    StorageResourceAgent agent = new StorageResourceAgent();
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> hardDrives = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.HARD_DRIVE_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(hardDrives.isEmpty());
    for (Resource res : hardDrives) {
      assertEquals(res.getTypeUri(), KnowledgeConstants.HARD_DRIVE_URI);
      assertTrue(res.hasProperty(ResourcePropertyNames.HardDrive.BLOCKS));
      assertTrue(res.hasProperty(ResourcePropertyNames.HardDrive.DEVICE_NAME));
      assertTrue(res.hasProperty(ResourcePropertyNames.HardDrive.MOUNT_POINT));
    }
  }

  @Test
  public void netIfacesDiscoveryTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    NetIfaceResourceAgent agent = new NetIfaceResourceAgent();
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> netIfaces = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.NETWORK_INTERFACE_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(netIfaces.isEmpty());
    for (Resource res : netIfaces) {
      assertEquals(res.getTypeUri(), KnowledgeConstants.NETWORK_INTERFACE_URI);
      assertTrue(res.hasProperty(ResourcePropertyNames.NetworkInterface.INTERFACE_NAME));
    }
  }

  @Test
  public void cpusDiscoveryTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    CpuResourceAgent agent = new CpuResourceAgent();
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> cpus = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.CPU_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(cpus.isEmpty());
    for (Resource res : cpus) {
      assertEquals(res.getTypeUri(), KnowledgeConstants.CPU_URI);
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.BOGO_MIPS));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.CACHE));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.CLOCK));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.FAMILY));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.ID));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.MODEL));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.MODEL_NAME));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.STEPPING));
      assertTrue(res.hasProperty(ResourcePropertyNames.CPU.VENDOR_ID));
    }
  }

  @Test
  public void vmsDiscoveryTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    VirtualMemoryRA agent = new VirtualMemoryRA();
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> swaps = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.VIRTUAL_MEMORY_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(swaps.isEmpty());
    for (Resource res : swaps) {
      assertEquals(res.getTypeUri(), KnowledgeConstants.VIRTUAL_MEMORY_URI);
      assertTrue(res.hasProperty(ResourcePropertyNames.VirtualMemory.DEVICE_NAME));
      assertTrue(res.hasProperty(ResourcePropertyNames.VirtualMemory.SIZE));
      assertTrue(res.hasProperty(ResourcePropertyNames.VirtualMemory.TYPE));
    }
  }

  @Test
  public void pmsDiscoveryTest() throws Exception {
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    PhysicalMemoryRA agent = new PhysicalMemoryRA();
    Resource nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    List<Resource> memory = agent.discoverChildResources(app, nodeResource, KnowledgeConstants.VIRTUAL_MEMORY_URI);
    ocmgConnection.detachFromApplication(testAppResource);
    assertFalse(memory.isEmpty());
    for (Resource res : memory) {
      assertEquals(res.getTypeUri(), KnowledgeConstants.PHYSICAL_MEMORY_URI);
      assertTrue(res.hasProperty(ResourcePropertyNames.PhysicalMemory.SIZE));
    }
  }

  @Test
  public void applicationsDiscoveryTest() throws Exception {
    final Resource res = createTestResource();
    AppsResourceAgent appsDiscoveryAgent = new AppsResourceAgent();
    List<Resource> applications = appsDiscoveryAgent.discoverChildResources(null, res, KnowledgeConstants.APPLICATION_URI);
    assertEquals(applications.size(), 1);
    Resource application = applications.get(0);
    assertTrue(application.getUri().endsWith(TEST_APP_NAME));
    assertEquals(application.getTypeUri(), KnowledgeConstants.APPLICATION_URI);
  }


}
