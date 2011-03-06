package pl.edu.agh.semmon.gui.util;

import org.apache.pivot.wtk.content.TreeNode;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:49 13-06-2010
 */
public class TreeNodeContainer<T> extends TreeNode {

  private T content;


  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }



}
