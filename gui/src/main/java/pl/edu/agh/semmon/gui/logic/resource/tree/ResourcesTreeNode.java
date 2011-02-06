package pl.edu.agh.semmon.gui.logic.resource.tree;

import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.util.LinkedList;
import java.util.List;

/**
 * Wraps Resource, making it ResourcesTree-compatibile
 *
 * @author tkozak
 *         Created at 18:39 06-06-2010
 */
public class ResourcesTreeNode {

  /**
   *
   */
  private Resource resource;

  /**
   *
   */
  private List<ResourcesTreeNode> children;

  /**
   *
   */
  private ResourcesTreeNode parent;


  /**
   * @param resource
   * @param parent
   */
  public ResourcesTreeNode(Resource resource, ResourcesTreeNode parent) {
    this(resource, new LinkedList<ResourcesTreeNode>(), parent);
  }

  /**
   * @param resource
   * @param children
   * @param parent
   */
  public ResourcesTreeNode(Resource resource, List<ResourcesTreeNode> children, ResourcesTreeNode parent) {
    this.resource = resource;
    this.children = children;
    this.parent = parent;
  }

  public Resource getResource() {
    return resource;
  }

  public List<ResourcesTreeNode> getChildren() {
    return children;
  }

  public ResourcesTreeNode getParent() {
    return parent;
  }
}
