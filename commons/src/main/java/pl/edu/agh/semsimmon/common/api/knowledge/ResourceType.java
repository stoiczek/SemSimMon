package pl.edu.agh.semsimmon.common.api.knowledge;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:53 13-06-2010
 */
public enum ResourceType {
  TERE("");

  private String uri;

  ResourceType(String uri) {
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }
}
