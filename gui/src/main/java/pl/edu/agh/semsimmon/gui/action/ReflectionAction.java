package pl.edu.agh.semsimmon.gui.action;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:57 19-09-2010
 */
public class ReflectionAction extends Action {

  private static final Logger log = LoggerFactory.getLogger(ReflectionAction.class);

  private Object target;

  private String method;

  @Override
  public void perform(Component source) {
    try {
      Class targetClass = target.getClass();
      Method targetMethod = targetClass.getMethod(method);
      log.debug("Invoking method: {}", targetMethod.getName() );
      targetMethod.invoke(target);
    } catch (NoSuchMethodException e) {
      log.error("Error calling method", e);
    } catch (InvocationTargetException e) {
      log.error("Error calling method", e);
    } catch (IllegalAccessException e) {
      log.error("Error calling method", e);
    }
  }

  public void setTarget(Object target) {
    this.target = target;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}
