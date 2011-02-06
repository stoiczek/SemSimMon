package pl.edu.agh.semmon.core.testsupport;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import pl.edu.agh.semmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.util.Collections;

/**
 * Factory for mock VOs.
 *
 * @author tkozak
 *         Created at 14:40 14-08-2010
 */
public class MocksFactory {

   public static Resource createMockResource() {
    return new Resource(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10),
        Collections.<String, Object>emptyMap());
  }

  public static MeasurementDefinition createMockMeasurementDefinition() {
    MeasurementDefinition def = new MeasurementDefinition();
    def.setId(RandomStringUtils.randomAlphanumeric(10));
    def.setCapabilityUri(RandomStringUtils.randomAlphanumeric(10));
    def.setUpdateInterval(RandomUtils.nextInt(59));
    def.setResourceUri(RandomStringUtils.randomAlphanumeric(10));
    return def;
  }

}
