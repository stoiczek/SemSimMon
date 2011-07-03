package pl.edu.agh.semsimmon.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @TODO description
 *
 * Created on: 2011-07-02 16:31
 * <br/>
 *
 *
 * author Tadeusz Kozak
 */
public class TestServlet extends HttpServlet {

  private static TestProcessor testProcessor = new TestProcessor();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      doProcess(req, resp);
    } catch (InterruptedException e) {
      throw new ServletException(e);
    }
  }


  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      doProcess(req, resp);
    } catch (InterruptedException e) {
      throw new ServletException(e);
    }
  }

  private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException, InterruptedException {
    String aString = req.getParameter("a");
    String bString = req.getParameter("b");
    double[][] aMatrix = string2SquareMatrix(aString);
    double[] bMatric = string2VectorMatrix(bString);
    final double[] result = testProcessor.doSolve(aMatrix, bMatric);
    StringBuilder resultStringBuilder = new StringBuilder();
    for(double entry : result) {
      resultStringBuilder.append(entry).append(',');
    }
    resultStringBuilder.deleteCharAt(resultStringBuilder.length() -1);
    resp.setStatus(200);
    resp.getOutputStream().print(resultStringBuilder.toString());
  }

  private double[] string2VectorMatrix(String bString) {
    String split[] = bString.split(",");
    double result[] = new double[split.length];
    int i = 0;
    for (String item : split) {
      result[i++] = Double.parseDouble(item);
    }
    return result;
  }

  private double[][] string2SquareMatrix(String aString) {
    String[] splitted = aString.split(",");
    int n = (int) Math.sqrt(splitted.length);
    double result[][] = new double[n][n];
    int k = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        result[i][j] = Double.parseDouble(splitted[k++]);
      }
    }
    return result;
  }

}
