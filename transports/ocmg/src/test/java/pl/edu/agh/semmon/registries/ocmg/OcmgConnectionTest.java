package pl.edu.agh.semmon.registries.ocmg;

import org.testng.annotations.Test;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

/**
 * @author tkozak
 *         Created at 46:10 20-05-2010
 */
public class OcmgConnectionTest extends OcmgRequringTest {


  OcmgConnection connection;


  @Test
  public void connectionTest() throws OcmgException {
    Resource resource = createTestResource();
    connection = new OcmgConnection(resource);
    connection.connect();
  }

  @Test(dependsOnMethods = "connectionTest")
  public void disconnectionTest() throws OcmgException {
    connection.disconnect();
  }

}
