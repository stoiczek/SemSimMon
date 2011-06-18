/*
 * Copyright (C) SayMama Ltd 2011
 *
 * All rights reserved. Any use, copying, modification, distribution and selling
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package pl.edu.agh.semsimmon.gui.controllers.action;

import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeUtils;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnection;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnectionsManager;

import java.io.File;
import java.io.FileInputStream;


/**
 * @author Tadeusz Kozak
 * @TODO description
 * <p/>
 * Created on: 2011-06-18 12:50
 * <br/>
 * <a href="http://www.saymama.com">http://www.saymama.com</a>
 */
public class OntologyFileSelectedListener implements SheetCloseListener {

  private CoreConnectionsManager connectionsManager;

  private static final Logger log = LoggerFactory.getLogger(OntologyFileSelectedListener.class);

  private ExceptionHandler exceptionHandler;

  @Override
  public void sheetClosed(Sheet sheet) {
    FileBrowserSheet fbs = (FileBrowserSheet) sheet;
    File selected = fbs.getSelectedFile();
    try {
      byte serialized[] = KnowledgeUtils.serializeOntology(new FileInputStream(selected));
      for (CoreConnection conn : connectionsManager.getCoreConnections()) {
        conn.getCoreServiceFacade().reloadOntology(serialized);
      }
    } catch (Exception e) {
      exceptionHandler.handleException(e);
    }

  }

  public void setConnectionsManager(CoreConnectionsManager connectionsManager) {
    this.connectionsManager = connectionsManager;
  }

  public void setExceptionHandler(ExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }
}
