package pl.edu.agh.semsimmon.registries.ocmg;

import org.apache.commons.io.IOUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import pl.edu.agh.semsimmon.common.consts.OcmgRegistryConsts;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tkozak
 *         Created at 03:12 22-05-2010
 * @TODO description
 */
@ContextConfiguration(locations = {"classpath:ocmgtransport-context.xml", "classpath:consts-beans.xml"})
public class OcmgRequringTest extends AbstractTestNGSpringContextTests {
  
  protected static final String TEST_APP_NAME = "test_app";
  protected static final String URI_PORT_REPL = "#port#";
  protected static final String TEST_URI = "ocmg://localhost:" + URI_PORT_REPL + "/" + TEST_APP_NAME;
  private static final String OCMG_MONITOR_EXECUTABLE = "/bin/cg-ocmg-monitor";
  private static final String TEST_APP_CMD = "mpiexec ring --ocmg-appname " + TEST_APP_NAME + "--ocmg-mainsm ";
  protected static int ocmgPort = -1;
  protected static Process ocmgMonitorProcess;
  protected static Process testAppProcess;
  protected static Process mpdProcess;

  protected static String connectionString;

  @BeforeTest
  public synchronized void startOcmg() throws IOException, InterruptedException {
    if (ocmgPort > 0) {
      return;
    }
    String cgLocation = System.getenv("CG_LOCATION");
    String monitorCmd = cgLocation + OCMG_MONITOR_EXECUTABLE;
    ProcessBuilder builder = new ProcessBuilder(monitorCmd);
    builder.environment().put("LD_LIBRARY_PATH", cgLocation + "/lib");
    System.out.println("Executing: " + monitorCmd);
    ocmgMonitorProcess = builder.start();
    Thread.sleep(1000);


    byte[] outputBuff = null;
    final InputStream ocmgMonitorInputStream = ocmgMonitorProcess.getInputStream();
    if (ocmgMonitorInputStream.available() > 0) {
      System.out.println("Got monitor output content: ");
      try {
        int bytes = ocmgMonitorInputStream.available();
        outputBuff = new byte[bytes];
        ocmgMonitorInputStream.read(outputBuff);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    String content = new String(outputBuff);
    String connectionString = content.substring(content.indexOf(':') + 2);
    connectionString = connectionString.replaceAll("\\s", "");
    System.out.println("Connection string: " + connectionString);
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          IOUtils.copy(ocmgMonitorInputStream, System.out);
        } catch (IOException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
    }).start();


    builder.command("mpd");
    mpdProcess = builder.start();
    Thread.sleep(1000);


    URL resource = this.getClass().getClassLoader().getResource("ring");
    Assert.assertNotNull(resource);
    String ringPath = resource.getPath();
    new File(ringPath).setExecutable(true, true);
    System.out.println("Ring path: " + ringPath);

    builder.command("mpiexec", ringPath, "--ocmg-appname", TEST_APP_NAME, "--ocmg-mainsm", connectionString);
    testAppProcess = builder.start();
    Thread.sleep(10000);
    if (testAppProcess.getInputStream().available() > 0) {
      System.out.println("Got test app output content: ");
      try {
        int bytes = testAppProcess.getInputStream().available();
        outputBuff = new byte[bytes];
        testAppProcess.getInputStream().read(outputBuff);
        System.out.write(outputBuff);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (testAppProcess.getErrorStream().available() > 0) {
      System.out.println("Got test app output content: ");
      try {
        int bytes = testAppProcess.getInputStream().available();
        outputBuff = new byte[bytes];
        testAppProcess.getErrorStream().read(outputBuff);
        System.out.write(outputBuff);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    ocmgPort = Integer.parseInt(connectionString.split(":")[1], 16);
    System.out.println("Monitor listening port: " + ocmgPort);
    OcmgRequringTest.connectionString = "7f000101:" + Integer.toString(ocmgPort, 16);
  }

  @AfterTest
  public synchronized void stopOcmg() {
    if (ocmgPort < 0) {
      return;
    }
    ocmgPort = -1;
    ocmgMonitorProcess.destroy();
    testAppProcess.destroy();
    mpdProcess.destroy();
  }

  protected Resource createTestResource() {
    String uri = "semsimmon://sample.host";
    Map<String, Object> propertiesMap = new HashMap<String, Object>();
    propertiesMap.put(OcmgRegistryConsts.CONNECTION_TYPE,
        OcmgRegistryConsts.CONNECTION_TYPE_SOCKET);
    propertiesMap.put(OcmgRegistryConsts.MAIN_SM_CONNECTION_STRING, connectionString);
    Resource resource = new Resource(uri, "", propertiesMap);
    return resource;
  }
}
