package pl.edu.agh.semsimmon.gui.controllers.action;

import java.lang.reflect.Method;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 18:26 10-07-2010
 */
public interface ReflectionButtonPressListenerFactory {

  ReflectionButtonPressListener createListener(Object target, Method method);

}
