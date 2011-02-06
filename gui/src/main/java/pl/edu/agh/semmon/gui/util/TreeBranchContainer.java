package pl.edu.agh.semmon.gui.util;

import org.apache.pivot.wtk.content.TreeBranch;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:46 13-06-2010
 */
public class TreeBranchContainer<T> extends TreeBranch {

  private T content;

  private TreeBranchContainer<T> parent;

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  public TreeBranchContainer<T> getParent() {
    return parent;
  }

  public void setParent(TreeBranchContainer<T> parent) {
    this.parent = parent;
  }
}
