package pl.edu.agh.semsimmon.gui.controllers.wizard;

/**
 * Created by IntelliJ IDEA.
 * User: tkozak
 * Date: 21.05.11
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class VetoException extends RuntimeException {

  public VetoException() {
  }

  public VetoException(String message) {
    super(message);
  }

  public VetoException(String message, Throwable cause) {
    super(message, cause);
  }

  public VetoException(Throwable cause) {
    super(cause);
  }
}
