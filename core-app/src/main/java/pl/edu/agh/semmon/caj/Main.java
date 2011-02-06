package pl.edu.agh.semmon.caj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.agh.semmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semmon.common.api.resource.CoreResourcesService;
import pl.edu.agh.semmon.core.measurement.CoreMeasurementServiceImpl;
import pl.edu.agh.semmon.core.remote.CoreRemoteServiceImpl;
import pl.edu.agh.semmon.core.resource.CoreResourcesServiceImpl;

public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

  public static void main(String[] args) throws InterruptedException {
//    Console c = System.console();
//    if (c == null) {
//      System.out.println("Running form non-interactive env is prohibited");
//      return;
//    }
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
        new String[]{"jmxtransport-context.xml",
            "semmonCoreContext.xml",
            "ocmgtransport-context.xml",
            "consts-beans.xml",
            "remotingContext.xml"});
    final CoreResourcesService coreResourcesService = ctx.getBean(CoreResourcesServiceImpl.class);
    final CoreMeasurementService coreMeasurementService = ctx.getBean(CoreMeasurementServiceImpl.class);
    final CoreRemoteServiceImpl coreRemoteService = ctx.getBean(CoreRemoteServiceImpl.class);
    coreResourcesService.addResourceListener(coreRemoteService);
    coreMeasurementService.addCapabilityValueListener(coreRemoteService);
    coreRemoteService.initEventsDispatcher();
    //noinspection InfiniteLoopStatement
    while (true) {
//      String line = c.readLine();
//      System.out.println("Got Line: '" + line + "'");
//      if (line.equalsIgnoreCase(RemotingConsts.QUIT_SENTENCE)) {
//        System.out.println("Ending");
//        break;
//      }
      Thread.sleep(Long.MAX_VALUE);
    }
//    System.out.println("Stopping Application context");
//    ctx.stop();
//    System.out.println("Semmon Core ends");
//    System.exit(0);
  }


}
