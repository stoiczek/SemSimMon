package pl.edu.agh.semsimmon.gui.util;

import org.apache.pivot.wtk.content.TreeBranch;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:46 13-06-2010
 */
public class TreeBranchContainer<T> extends TreeBranch {

  private T content;


  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

}
