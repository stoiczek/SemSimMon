package pl.edu.agh.semsimmon.common.api.resource;

import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.List;

/**
 *
 */
public interface IResourceDiscoveryEvent {


  public static enum EventType {
    NEW_RESOURCES_DISCOVERED, RESOURCES_REMOVED
  }

  /**
   * @return
   */
  List<Resource> getResources();

  /**
   * @return
   */
  Resource getParentResource();


  /**
   * @return
   */
  EventType getEventType();

}
