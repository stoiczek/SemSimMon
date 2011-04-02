package pl.edu.agh.semsimmon.gui.logic.resource.tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 18:39 06-06-2010
 */
public class ResourcesTree {

  private static final Logger log = LoggerFactory.getLogger(ResourcesTree.class);

  /**
   *
   */
  private ResourcesTreeNode root;

  /**
   *
   */
  private Map<String, ResourcesTreeNode> nodes = new HashMap<String, ResourcesTreeNode>();

  /**
   * @param root
   */
  public ResourcesTree(ResourcesTreeNode root) {
    this.root = root;
    nodes.put(root.getResource().getUri(), root);
  }


  /**
   * @param parentURI
   * @param resource
   * @return
   */
  public ResourcesTreeNode addResource(String parentURI, Resource resource) {
    final String resourceUri = resource.getUri();
    if (nodeRegistered(resourceUri)) {
      log.warn("Trying to add resource {} twice", resource);
      return nodes.get(resourceUri);
    }
    checkNodeRegistered(parentURI);
    if (nodes.containsKey(resourceUri)) {
      return nodes.get(resourceUri);
    }
    final ResourcesTreeNode parent = nodes.get(parentURI);
    final ResourcesTreeNode node = new ResourcesTreeNode(resource, parent);
    parent.getChildren().add(node);
    nodes.put(resourceUri, node);
    return node;
  }

  /**
   * @param uri
   */
  public void removeResource(String uri) {
    checkNodeRegistered(uri);
    final ResourcesTreeNode toRemove = nodes.get(uri);
    final ResourcesTreeNode parent = toRemove.getParent();
    parent.getChildren().remove(toRemove);
    removeRecursively(toRemove);
  }

  /**
   * @param resourceUri
   * @return
   */
  public ResourcesTreeNode getResourceTreeNode(String resourceUri) {
    checkNodeRegistered(resourceUri);
    return nodes.get(resourceUri);
  }

  /**
   * @return
   */
  public ResourcesTreeNode getRoot() {
    return root;
  }

  private void removeRecursively(ResourcesTreeNode node) {
    for (ResourcesTreeNode child : node.getChildren()) {
      removeRecursively(child);
    }
    nodes.remove(node.getResource().getUri());
  }

  private boolean nodeRegistered(String uri) {
    return nodes.containsKey(uri);
  }

  private void checkNodeRegistered(String uri) throws IllegalArgumentException {
    if (!nodeRegistered(uri)) {
      log.error("Resource with given uri ({}) isn't registered in this tree", uri);
      throw new IllegalArgumentException("Resource with given uri ({}) haven't been added to this tree");
    }
  }
}
