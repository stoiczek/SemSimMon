package pl.edu.agh.semmon.gui.logic.connection;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collection;

/**
 * Tests ExternalProcessCoreConnection - whether it can start external core, and properly connect/disconnect from it
 *
 * @author tkozak
 *         Created at 14:41 12-09-2010
 */
public class ExternalProcessTest {

  @Test
  public void doTest() throws IOException, InterruptedException {
    ExternalProcessCoreConnection connection = new ExternalProcessCoreConnection(
        System.getProperty("user.name") + "@localhost", "");
    connection.startCore();
    connection.connect();
    Thread.sleep(20000);
    Collection<String> resources = connection.getCoreServiceFacade().getAllRegisteredResources();
    connection.disconnect();
    connection.stopCore();
  }

}
