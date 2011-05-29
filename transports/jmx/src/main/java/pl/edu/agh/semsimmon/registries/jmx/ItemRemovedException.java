package pl.edu.agh.semsimmon.registries.jmx;

/**
 * Created by IntelliJ IDEA.
 * User: tkozak
 * Date: 29.05.11
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class ItemRemovedException extends RuntimeException {

  public ItemRemovedException() {
  }

  public ItemRemovedException(String message) {
    super(message);
  }

  public ItemRemovedException(String message, Throwable cause) {
    super(message, cause);
  }

  public ItemRemovedException(Throwable cause) {
    super(cause);
  }
}
