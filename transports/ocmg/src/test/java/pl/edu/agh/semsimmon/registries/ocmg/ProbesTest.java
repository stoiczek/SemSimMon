package pl.edu.agh.semsimmon.registries.ocmg;

import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.Node;
import org.balticgrid.ocmg.objects.Site;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.probe.node.LoadAvgProbe;

import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 19:27 05-09-2010
 */
public class ProbesTest extends OcmgRequringTest {

  private OcmgConnection ocmgConnection;

  private Resource testAppResource;

  private Application app;

  Resource nodeResource;

  @BeforeClass
  public void setupOcmgConnection() throws Exception {
    if (!isOsSupported()) {
      return;
    }
    Resource rootResource = createTestResource();
    testAppResource = new Resource(rootResource.getUri() + "/" + TEST_APP_NAME, KnowledgeConstants.APPLICATION_URI, rootResource.getProperties());
    testAppResource.setProperty(ResourcePropertyNames.Application.NAME, TEST_APP_NAME);
    ocmgConnection = new OcmgConnection(testAppResource);
    ocmgConnection.connect();
    app = ocmgConnection.attachToApplication(testAppResource);
    final Site site = app.getHierarchy().getSiteTree().get(0).getSite();
    site.attach();
    String siteID = site.getCacheName();
    final Node node = app.getHierarchy().getSiteTree().get(0).getNodeTree().get(0).getNode();
    node.attach();
    String nodeId = node.getCacheName();
    nodeResource = new Resource(testAppResource.getUri() + "/" + siteID + "/" + nodeId, KnowledgeConstants.NODE_URI,
        Collections.<String, Object>emptyMap());
  }

  @AfterClass
  public void tearDownOcmgApp() throws OcmgException {
    if (!isOsSupported()) {
      return;
    }
    ocmgConnection.detachFromApplication(testAppResource);
  }

  @Test
  public void nodeLoad1MinTest() throws Exception {
    if (!isOsSupported()) {
      return;
    }
    testLoadAvg(LoadAvgProbe.Type._1MIN);
  }

  private void testLoadAvg(LoadAvgProbe.Type type) throws OcmgException {
    if (!isOsSupported()) {
      return;
    }
    LoadAvgProbe loadAvgProbe = new LoadAvgProbe();
    loadAvgProbe.setType(type);
    CapabilityValue value = loadAvgProbe.getCapabilityValue(nodeResource, app, "");
    assertNotNull(value);
    assertEquals(value.getValueType(), CapabilityValue.ValueType.NUMERIC);
  }

  @Test
  public void nodeLoad5MinTest() throws Exception {
    testLoadAvg(LoadAvgProbe.Type._5MIN);
  }

  @Test
  public void nodeLoad15MinTest() throws Exception {
    testLoadAvg(LoadAvgProbe.Type._15MIN);
  }
}
