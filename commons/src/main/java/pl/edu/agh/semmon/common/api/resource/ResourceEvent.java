package pl.edu.agh.semmon.common.api.resource;

import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.util.List;

/**
 * Event describing some resources change - sent by Core for all external
 * listeners
 *
 * @author koperek
 * @author tkozak
 */

public interface ResourceEvent {


  /**
   * Types of resource event
   */
  public static enum Type {
    RESOURCES_ADDED, RESOURCES_REMOVED
  }

  /**
   * Returns type of event
   *
   * @return Type of event
   */
  Type getType();


  /**
   * Returns resource affected by this event.
   *
   * @return Returns affected resource
   */
  List<Resource> getResources();
}
