/**
 *
 */
package pl.edu.agh.semmon.common.vo.core.resource;

import pl.edu.agh.semmon.common.api.resource.ResourceEvent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Default implementation of {@link pl.edu.agh.semmon.common.api.resource.ResourceEvent}
 *
 * @author koperek
 */
public class ResourceEventImpl implements ResourceEvent, Serializable {

  private static final long serialVersionUID = -198899591057125398L;
    private Type eventType;
  private List<Resource> resource;


  public ResourceEventImpl(Type eventType, Resource resource) {
    this.eventType = eventType;
    this.resource = Arrays.asList(resource);
  }

  public ResourceEventImpl(Type eventType, List<Resource> resources) {
    this.eventType = eventType;
    this.resource = resources;
  }

    /**
   * {@inheritDoc}
   */
  @Override
  public Type getType() {
    return eventType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Resource> getResources() {
    return resource;
  }


  @Override
  public String toString() {
    return "ResourceEventImpl{" +
        ", eventType=" + eventType +
        ", resource=" + resource +
        '}';
  }
}
