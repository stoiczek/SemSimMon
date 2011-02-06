package pl.edu.agh.semmon.common.exception;

public class ResourceNotRegisteredException extends Exception {

  private String uri;

  public String getUri() {
    return uri;
  }

  public ResourceNotRegisteredException(String uri) {
    super("Resource not registered: " + uri);
    this.uri = uri;
  }

}
