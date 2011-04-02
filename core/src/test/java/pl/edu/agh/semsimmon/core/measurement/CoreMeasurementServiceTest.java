package pl.edu.agh.semsimmon.core.measurement;

import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.transport.TransportProxy;
import pl.edu.agh.semsimmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.core.transport.TransportProxiesManager;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static pl.edu.agh.semsimmon.core.testsupport.MocksFactory.createMockMeasurementDefinition;
import static pl.edu.agh.semsimmon.core.testsupport.MocksFactory.createMockResource;

/**
 * Test class for CoreMeasurementServiceImpl
 *
 * @author tkozak
 *         Created at 12:04 14-08-2010
 */
public class CoreMeasurementServiceTest {

  @Mock
  private TransportProxy mockTransportProxy;

  @Mock
  private TransportProxiesManager proxiesManager;

  @Mock
  private CapabilityValuePollManager pollManager;

  @InjectMocks
  private CoreMeasurementServiceImpl service;

  @BeforeTest
  public void setupProxyManager() {
    service = new CoreMeasurementServiceImpl();
    MockitoAnnotations.initMocks(this);
    when(proxiesManager.findProxyForResource(Matchers.<Resource>any())).thenReturn(mockTransportProxy);
  }

  @Test
  public void getCapabilityValueTest() throws Exception{
    final Resource testResource = createMockResource();
    final String testCapabilityURI = "fakedUri";
    service.getCapabilityValue(testResource, testCapabilityURI);
    verify(proxiesManager).findProxyForResource(testResource);
    verify(mockTransportProxy).getCapabilityValue(testResource, testCapabilityURI);
  }


  @Test
  public void getAllCapabilitiesTest() throws Exception {
    final Resource testResource = createMockResource();
    final List<String> capabilities = Collections.singletonList("testCapability");
    service.getAllCapabilitiesValues(testResource, capabilities);
    verify(proxiesManager).findProxyForResource(testResource);
    verify(mockTransportProxy).getCapabilityValues(testResource, capabilities);
  }

  @Test
  public void createMeasurementTest() throws Exception {
    MeasurementDefinition def = createMockMeasurementDefinition();
    String uuid = service.createMeasurement(def);
    verify(pollManager).submitPollJob(def, service);
    assertEquals(uuid, def.getId());
  }

  @Test
  public void updateMeasurementTest() throws Exception {
    MeasurementDefinition def = createMockMeasurementDefinition();
    service.createMeasurement(def);
    def.setUpdateInterval(234);
    service.updateMeasurement(def);
    verify(pollManager).updatePollJob(def);
  }


  @Test
  public void removeMeasurementTest() throws Exception {
    MeasurementDefinition def = createMockMeasurementDefinition();
    String uuid = service.createMeasurement(def);
    service.removeMeasurement(uuid);
    verify(pollManager).cancelPollJob(def);
  }


}
