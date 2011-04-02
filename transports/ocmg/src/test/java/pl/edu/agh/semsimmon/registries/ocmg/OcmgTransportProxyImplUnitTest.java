package pl.edu.agh.semsimmon.registries.ocmg;

import org.balticgrid.ocmg.objects.Application;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryEvent;
import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryListener;
import pl.edu.agh.semsimmon.common.consts.OcmgRegistryConsts;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.api.transport.TransportException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.resource.ResourceAgent;
import pl.edu.agh.semsimmon.registries.ocmg.probe.CapabilityProbe;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author tkozak
 *         Created at 57:12 19-05-2010
 * @TODO description
 */
public class OcmgTransportProxyImplUnitTest extends OcmgRequringTest {


  private OcmgTransportProxyImpl transportProxy;

  @BeforeTest
  public void createTransportProxy() {
    transportProxy = new OcmgTransportProxyImpl();
  }

  @Test
  public void isURISupportedTest() {
    final String[] validUris = new String[]{"ocmg://192.145.123.21:1234/some_app_123",
        "ocmg://some.host.com:1234/r8374893ur",
        "ocmg://8.8.8.8:1234/r8374893ur",
        "ocmg://[3ffe:1900:4545:3:200:f8ff:fe21:67cf]:4354/43534543"
    };
    final String[] invalidUris = new String[]{
        "http://www.gazeta.pl",
        "345643erdfvrgbhtgf",
        "jmx:rmi://myhost:8084/jndi/rmi://myhost:8083/server",
        "ocmg://8.8.8.8/r8374893ur",
        "ocmg://8.8.8.8:1234",
        "ocmg://8.8.8.8:1234",
        "ocmg://3ffe:1900:4545:3:200:f8ff:fe21:67cf:4354/43534543"
    };


    for (String uri : validUris) {
      Resource resource = new Resource(uri, KnowledgeConstants.APPLICATION_URI, Collections.<String, Object>emptyMap());
      resource.setProperty(OcmgRegistryConsts.CONNECTION_TYPE, OcmgRegistryConsts.CONNECTION_TYPE_GLOBUS);
      assertTrue(transportProxy.isResourceSupported(resource), "Valid URI hasn't passed: " + uri);
    }
    for (String uri : invalidUris) {
      Resource resource = new Resource(uri, KnowledgeConstants.APPLICATION_URI, Collections.<String, Object>emptyMap());
      resource.setProperty(OcmgRegistryConsts.CONNECTION_TYPE, OcmgRegistryConsts.CONNECTION_TYPE_GLOBUS);
      assertFalse(transportProxy.isResourceSupported(resource), "Invalid uri passed: " + uri);
    }
    Resource resource = new Resource(validUris[0], KnowledgeConstants.APPLICATION_URI, Collections.<String, Object>emptyMap());
    assertFalse(transportProxy.isResourceSupported(resource), "Property not set, shouldn't pass");

    resource = new Resource(validUris[0], KnowledgeConstants.APPLICATION_URI, Collections.<String, Object>emptyMap());
    resource.setProperty(OcmgRegistryConsts.CONNECTION_TYPE, "shit");
    assertFalse(transportProxy.isResourceSupported(resource), "Property set sadly badly, shouldn't pass");

    resource = new Resource(validUris[0], KnowledgeConstants.APPLICATION_URI, Collections.<String, Object>emptyMap());
    resource.setProperty(OcmgRegistryConsts.CONNECTION_TYPE, OcmgRegistryConsts.CONNECTION_TYPE_GLOBUS);
    assertTrue(transportProxy.isResourceSupported(resource), "Property set properly, should pass");

    resource = new Resource(validUris[0], KnowledgeConstants.APPLICATION_URI, Collections.<String, Object>emptyMap());
    resource.setProperty(OcmgRegistryConsts.CONNECTION_TYPE, OcmgRegistryConsts.CONNECTION_TYPE_SOCKET);
    assertTrue(transportProxy.isResourceSupported(resource), "Property set properly, should pass");

    resource = new Resource(validUris[0], "", Collections.<String, Object>emptyMap());
    resource.setProperty(OcmgRegistryConsts.CONNECTION_TYPE, OcmgRegistryConsts.CONNECTION_TYPE_SOCKET);
    assertFalse(transportProxy.isResourceSupported(resource), "Invalid type, shouldn't pass");
  }


  @Test
  public void registerResourceTest() throws Exception {
    Resource resource = createTestResource();
    transportProxy.registerResource(resource);
  }


  @Test(dependsOnMethods = "registerResourceTest")
  public void unregisterResourceTest() throws Exception {
    Resource resource = createTestResource();
    transportProxy.unregisterResource(resource);
  }

  @Test
  public void hasCapabilityTest() throws TransportException {
    Map<String, Map<String, CapabilityProbe>> probes = new HashMap<String, Map<String, CapabilityProbe>>();
    CapabilityProbe probe = mock(CapabilityProbe.class);
    String resourceType = KnowledgeConstants.ONTOLOGY_URI + "#TestResource";
    String capabilityType = KnowledgeConstants.ONTOLOGY_URI + "#TestCapability";
    probes.put(resourceType, Collections.singletonMap(capabilityType, probe));
    transportProxy.setProbes(probes);
    try {
      Resource reallyBadResource = new Resource("SomeURI", "Some bad type", Collections.<String, Object>emptyMap());
      transportProxy.hasCapability(reallyBadResource, "test capability");
      fail();
    } catch (TransportException e) {
      assertTrue(true);
    }
    Resource badResource = new Resource("ocmg://test.com:58947/test_app", "Some bad type", Collections.<String, Object>emptyMap());
    Resource properResource = new Resource("ocmg://test.com:58947/test_app", resourceType,
        Collections.<String, Object>emptyMap());
    assertFalse(transportProxy.hasCapability(badResource, "testCapability"));
    assertFalse(transportProxy.hasCapability(properResource, "badCapability"));
    assertTrue(transportProxy.hasCapability(properResource, capabilityType));
  }

  @Test
  public void getCapabilityValueTest() throws TransportException, OcmgException {
    Map<String, Map<String, CapabilityProbe>> probes = new HashMap<String, Map<String, CapabilityProbe>>();


    CapabilityProbe probe = mock(CapabilityProbe.class);

    String resourceType = KnowledgeConstants.ONTOLOGY_URI + "#TestResource";
    String capabilityType = KnowledgeConstants.ONTOLOGY_URI + "#TestCapability";
    probes.put(resourceType, Collections.singletonMap(capabilityType, probe));
    transportProxy.setProbes(probes);
    Resource properResource = new Resource("ocmg://test.com:58947/test_app", resourceType,
        Collections.<String, Object>emptyMap());
    when(probe.getCapabilityValue(eq(properResource), any(Application.class))).thenReturn(new CapabilityValue(10));
    CapabilityValue retValue = transportProxy.getCapabilityValue(properResource, capabilityType);
    assertEquals(retValue.getValueType(), CapabilityValue.ValueType.NUMERIC);
    assertEquals(retValue.getNumericValue().intValue(), 10);
    verify(probe).getCapabilityValue(eq(properResource), any(Application.class));
  }


  @Test(dependsOnMethods = "registerResourceTest")
  public void discoverChildrenTest() throws Exception {
    ResourceAgent agent = mock(ResourceAgent.class);
    IResourceDiscoveryListener listener = mock(IResourceDiscoveryListener.class);
    final Resource testParent = createTestResource();
    final Resource childResType1 = new Resource(testParent.getUri() + "/test_res1", KnowledgeConstants.ONTOLOGY_URI + "#TestRes1", Collections.<String, Object>emptyMap());
    Resource childResType2 = new Resource(testParent.getUri() + "/test_res12", KnowledgeConstants.ONTOLOGY_URI + "#TestRes2", Collections.<String, Object>emptyMap());


    Map<String, ResourceAgent> agents = Collections.singletonMap(childResType1.getTypeUri(), agent);
//    when(agent.supportsChildrenType(testParent, childResType1.getType())).thenReturn(true);
//    when(agent.supportsChildrenType(testParent, childResType2.getType())).thenReturn(false);
    when(agent.discoverChildResources(any(Application.class), eq(testParent), eq(childResType1.getTypeUri()))).thenReturn(Collections.singletonList(childResType1));

    transportProxy.setResourceAgents(agents);
    transportProxy.addResourceDiscoveryListener(listener);
    transportProxy.discoverChildren(testParent, Arrays.asList(childResType1.getTypeUri(), childResType2.getTypeUri()));

//    verify(agent).supportsChildrenType(testParent, childResType1.getType());
//    verify(agent).supportsChildrenType(testParent, childResType2.getType());
    verify(agent).discoverChildResources(any(Application.class), eq(testParent), eq(childResType1.getTypeUri()));

    verify(listener).processEvent(argThat(new BaseMatcher<IResourceDiscoveryEvent>() {
      @Override
      public boolean matches(Object o) {
        if (!(o instanceof IResourceDiscoveryEvent)) {
          return false;
        }
        IResourceDiscoveryEvent event = (IResourceDiscoveryEvent) o;
        if (event.getEventType() != IResourceDiscoveryEvent.EventType.NEW_RESOURCES_DISCOVERED) {
          return false;
        }
        if (event.getParentResource() != testParent) {
          return false;
        }
        if (event.getResources().size() != 1) {
          return false;
        }
        if (event.getResources().get(0) != childResType1) {
          return false;
        }
        return true;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Any event that contains proper parent and child :/");
      }
    }));
  }

}
