package pl.edu.agh.semsimmon.test;

import java.io.*;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @TODO description
 *
 * Created on: 2011-07-02 16:31
 * <br/>
 *
 *
 * author Tadeusz Kozak
 */
public class TestProcessor {

  private static final String JACOB_APP_HOME = "jacobi";

  private static final Logger log = LoggerFactory.getLogger(TestProcessor.class);

  public synchronized double[] doSolve(double[][] a, double[] b) throws IOException, InterruptedException {
    log.debug("Processing calculation request");
    final String jacobAppHome = System.getProperty("user.home") + File.separator + JACOB_APP_HOME;
    final File jackobAppHomeFile = new File(jacobAppHome);
    if (!jackobAppHomeFile.exists()) {
      throw new RuntimeException("Jacobi app home doesn't exist");
    }
    final File inputFileTarget = new File(jackobAppHomeFile, "jacobi.in");
    final File inputFile = File.createTempFile("semsimmon_input", ".tmp");
    final PrintWriter output = new PrintWriter(new FileWriter(inputFile));
    log.debug("Preparing data file");
    output.println(b.length);
    for (int i = 0; i < b.length; i++) {
      for (int j = 0; j < b.length; j++) {
        output.print(a[i][j]);
        output.print(" ");
      }
    }
    output.println();
    for (double aB : b) {
      output.println(aB);
    }
    output.flush();
    output.close();
    log.debug("Data file prepare submitting");
    inputFile.renameTo(inputFileTarget);
    final File resultFile = new File(jackobAppHomeFile, "jacobi.out");
    log.debug("Waiting for result file");
    while (!resultFile.exists()) {
      log.debug("Retrying...");
      Thread.sleep(500);
    }
    log.debug("Got result file");
    final FileInputStream input = new FileInputStream(resultFile);
    String resultString = IOUtils.toString(input);
    input.close();
    log.debug("Got result: " + resultString);
    log.debug("Trying to delete " + resultFile.getAbsolutePath());
    boolean deleteResult = resultFile.delete();
    if(!deleteResult) {
      log.error("Error removing file");
    } else {
      log.debug("File deleted");
    }
    String results[] = resultString.split("\\s+");
    if (results.length != b.length) {
      throw new RuntimeException("Invalid result");
    }
    double result[] = new double[b.length];
    int i = 0;
    for(String resultEntry : results) {
      result[i++] = Double.parseDouble(resultEntry);
    }
    log.debug("Processing done. returning result");
    return result;
  }
}
