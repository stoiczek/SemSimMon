package pl.edu.agh.semsimmon.gui.controllers.action;

import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Exception handler that displays pop-up with small error information.
 *
 * @author tkozak
 *         Created at 18:31 10-07-2010
 */
public class PopupExceptionHandler implements ExceptionHandler, ApplicationContextAware {

  private static final Logger log = LoggerFactory.getLogger(PopupExceptionHandler.class);

  private Window parentWindow;

  private ApplicationContext appContext;

  @Override
  public void handleException(Exception e) {
    log.error("Exception caught during action.", e);
    if (parentWindow == null) {
      parentWindow = appContext.getBean(Window.class);
    }
    if(e instanceof InvocationTargetException) {
      e = (Exception) e.getCause();
    }
    StringBuilder msgBuilder = new StringBuilder("Snap!. Seems, that we've encountered a problem ");
    msgBuilder.append(e.getClass());
    if(e.getMessage() != null) {
      msgBuilder.append(": ").append(e.getMessage());
    }
    msgBuilder.append("\n");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    e.printStackTrace(new PrintStream(outputStream));
//    msgBuilder.append(outputStream.toString());
    Prompt.prompt(MessageType.WARNING, msgBuilder.toString(), parentWindow);
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.appContext = applicationContext;
  }
}
