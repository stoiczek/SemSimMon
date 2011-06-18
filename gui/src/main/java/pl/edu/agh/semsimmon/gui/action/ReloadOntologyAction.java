/*
 * Copyright (C) SayMama Ltd 2011
 *
 * All rights reserved. Any use, copying, modification, distribution and selling
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package pl.edu.agh.semsimmon.gui.action;

import org.apache.pivot.wtk.*;
import pl.edu.agh.semsimmon.gui.MainWindowController;

/**
 * @author Tadeusz Kozak
 * @TODO description
 * <p/>
 * Created on: 2011-06-18 12:40
 * <br/>
 * <a href="http://www.saymama.com">http://www.saymama.com</a>
 */
public class ReloadOntologyAction extends Action {

  private SheetCloseListener closeListener;

  private MainWindowController owner;

  @Override
  public void perform(Component source) {
    final FileBrowserSheet fbs = new FileBrowserSheet(FileBrowserSheet.Mode.OPEN);
    fbs.open(owner.getWindow(), closeListener);
  }

  public void setCloseListener(SheetCloseListener closeListener) {
    this.closeListener = closeListener;
  }

  public void setOwner(MainWindowController owner) {
    this.owner = owner;
  }
}
