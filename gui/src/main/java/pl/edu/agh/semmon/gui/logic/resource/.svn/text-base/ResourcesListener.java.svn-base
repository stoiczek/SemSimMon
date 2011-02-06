package pl.edu.agh.semmon.gui.logic.resource;

import pl.edu.agh.semmon.gui.logic.resource.tree.ResourcesTreeNode;

import java.util.List;

/**
 * Provides facility letting listening of some resources life cycle.
 *
 * @author tkozak
 *         Created at 19:52 06-06-2010
 */
public interface ResourcesListener {

  /**
   * Called whenever resource has been added (or discovered)
   *
   * @param resourceNodes List of added nodes.
   */
  void resourcesAdded(List<ResourcesTreeNode> resourceNodes);

  /**
   * Called whenever resources will be removed.
   *
   * @param resourceUri uri of removed resource
   */
  void resourceRemoved(String resourceUri);

}
