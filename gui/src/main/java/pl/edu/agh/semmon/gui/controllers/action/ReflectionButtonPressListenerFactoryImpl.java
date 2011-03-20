package pl.edu.agh.semmon.gui.controllers.action;

import java.lang.reflect.Method;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 18:27 10-07-2010
 */
public abstract class ReflectionButtonPressListenerFactoryImpl implements ReflectionButtonPressListenerFactory {


  @Override
  public ReflectionButtonPressListener createListener(Object target, Method method) {
    final ReflectionButtonPressListener listener = createEmptyListener();
    listener.setMethod(method);
    listener.setTarget(target);
    return listener;
  }

  protected abstract ReflectionButtonPressListener createEmptyListener();
}
