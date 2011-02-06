package pl.edu.agh.semmon.gui.controllers.action;

import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 12:25 06-06-2010
 */
public class ReflectionButtonPressListener implements ButtonPressListener {

  private static final Logger log = LoggerFactory.getLogger(ReflectionButtonPressListener.class.getName());

  private Object target;

  private Method method;

  private ExceptionHandler exceptionHandler;

  private ExecutorService executorService;


  @Override
  public void buttonPressed(Button button) {

    Runnable actionRunnable = new Runnable() {
      @Override
      public void run() {
        try {
          method.invoke(target);
        } catch (Exception e) {
          if (exceptionHandler != null) {
            exceptionHandler.handleException(e);
          } else {
            log.error("Exception occured during action performing", e);
          }
        }
      }
    };
    if (executorService != null) {
      executorService.submit(actionRunnable);
    } else {
      log.warn("PressListener haven't got executor service configured. Performing execution on EDT");
      actionRunnable.run();
    }
  }


  public void setTarget(Object target) {
    this.target = target;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public void setExceptionHandler(ExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }

  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }
}
