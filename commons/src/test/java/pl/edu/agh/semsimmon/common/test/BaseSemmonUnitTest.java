package pl.edu.agh.semsimmon.common.test;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;

/**
 * Base class for all unit test classes.
 * Currently it only setups all annotation-configure mocks.
 *
 * @author tkozak
 *         Created at 12:05 14-08-2010
 */
public class BaseSemmonUnitTest {

  @BeforeTest
  public void setupMocks() {
    MockitoAnnotations.initMocks(this);
  }
}
