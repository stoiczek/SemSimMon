package pl.edu.agh.semmon.gui.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:31 18-09-2010
 */
public class TestApp {

  private static final Random RND = new Random(System.currentTimeMillis());

  private static final Logger log = LoggerFactory.getLogger(TestApp.class);

  public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
    Runnable performAction = new Runnable() {
      @Override
      public void run() {
        log.debug("Starting test thread");
        for (int i = 0; i < 3; i++) {
          try {
            cpuUsage();
            memoryConsumption();
          } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          } 
        }
        log.debug("Ending test thread");
      }
    };

    while (true) {
      for(int i=0;i<5;i++) {
        new Thread(performAction).start();
        Thread.sleep(1000);
      }
      Thread.sleep(25000 + RND.nextInt(10000));
    }
  }


  private static void memoryConsumption() throws InterruptedException {
    int step = 20;
    List<byte[]> container = new LinkedList<byte[]>();
    log.debug("Starting memory consumption");
    long endTime = System.currentTimeMillis() + RND.nextInt(2000) + 3000;
    while (true) {
      for (int j = 0; j < step; j++) {
        container.add(new byte[100]);
      }
      Thread.sleep(500);
      if (System.currentTimeMillis() > endTime) {
        break;
      }
    }
    container.clear();
    Thread.sleep(1000);
    log.debug("Calling GC");
    System.gc();

  }

  private static void cpuUsage() throws InterruptedException, IOException, NoSuchAlgorithmException {
    int step = 20;
    byte data[] = new byte[100 * 1024];
    FileInputStream fileIo = new FileInputStream("/dev/random");
    fileIo.read(data);
    fileIo.close();
    long endTime = System.currentTimeMillis() + RND.nextInt(5000) + 5000;
    log.debug("Starting cpu consumption");
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    while (true) {
      for (int j = 0; j < step; j++) {
        messageDigest.digest(data);
        Thread.sleep(10);
      }
      Thread.sleep(500);
      if (System.currentTimeMillis() > endTime) {
        break;
      }
    }
    log.debug("CPU consumption done");
  }

}
