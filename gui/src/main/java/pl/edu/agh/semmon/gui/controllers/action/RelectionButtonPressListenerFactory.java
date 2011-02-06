package pl.edu.agh.semmon.gui.controllers.action;

import java.lang.reflect.Method;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 18:26 10-07-2010
 */
public interface RelectionButtonPressListenerFactory {

  ReflectionButtonPressListener createListener(Object target, Method method);

}
