package pl.edu.agh.semmon.common.exception;

/**
 * Generic exception related to resources management.
 *
 * @author tkozak
 *         Created at 10:26 03-09-2010
 */
public class ResourcesException extends Exception {

  public ResourcesException() {
  }

  public ResourcesException(String message) {
    super(message);
  }

  public ResourcesException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourcesException(Throwable cause) {
    super(cause);
  }
}
