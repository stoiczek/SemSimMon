/**
 *
 */
package pl.edu.agh.semmon.common.vo.core.resource;

import pl.edu.agh.semmon.common.api.resource.IResourceDiscoveryEvent;

import java.util.List;

/**
 * @author koperek
 */
public class ResourceDiscoveryEvent implements IResourceDiscoveryEvent {

  private Resource parentResource;
  private List<Resource> resources;


  private EventType eventType;

  public ResourceDiscoveryEvent(Resource parentResource, List<Resource> resources, EventType eventType) {
    this.parentResource = parentResource;
    this.resources = resources;
    this.eventType = eventType;
  }

  public ResourceDiscoveryEvent(List<Resource> resources, EventType eventType) {
    this.resources = resources;
    this.eventType = eventType;
  }

  public Resource getParentResource() {
    return parentResource;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public EventType getEventType() {
    return eventType;
  }
}
