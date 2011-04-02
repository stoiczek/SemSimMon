package pl.edu.agh.semsimmon.registries.jmx.probe;

import org.testng.Assert;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:28 25-07-2010
 */
public final class ProbeTestUtils {

  public static void verifyWithError(long expected, CapabilityValue heapMem, int errorRate) {
    long diff = 0;
    if (expected != 0) {
      diff = Math.abs((expected - heapMem.getNumericValue().longValue()) / expected * 100);
    }
    Assert.assertTrue(diff < errorRate, "Error is too big. Expected: " + expected + ", but got: " +
        heapMem.getNumericValue().longValue() + ". Diff: " + diff);
  }

}
