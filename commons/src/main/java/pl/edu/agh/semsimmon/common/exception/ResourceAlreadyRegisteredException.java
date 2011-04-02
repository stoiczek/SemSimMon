/**
 *
 */
package pl.edu.agh.semsimmon.common.exception;

/**
 * @author koperek
 */
public class ResourceAlreadyRegisteredException extends Exception {

  private String uri;

  public ResourceAlreadyRegisteredException(String uri) {
    super("Resource already registered: " + uri);
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }

}
