package pl.edu.agh.semsimmon.common.vo.core.resource;

import pl.edu.agh.semsimmon.common.vo.BaseValueObject;

import java.util.HashMap;
import java.util.Map;

public class Resource extends BaseValueObject {

  private static final long serialVersionUID = -3891412413578714709L;

  
  private String typeUri;
  private String uri;
  private Map<String, Object> properties;

  public Resource(String uri, String typeUri, Map<String, Object> properties) {
    this.typeUri = typeUri;
    this.uri = uri;
    this.properties = new HashMap<String, Object>();
    this.properties.putAll(properties);
  }

  public boolean hasProperty(String key) {
    return properties.containsKey(key);
  }

  public void setProperty(String key, Object value) {
    properties.put(key, value);
  }

  public Object getProperty(String key) {
    return properties.get(key);
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

  public String getUri() {
    return uri;
  }

  public String getTypeUri() {
    return typeUri;
  }

  @Override
  public String toString() {
    return uri + " " + typeUri;
  }

  public void merge(Resource child1) {
    properties.putAll(child1.properties);
  }
}
