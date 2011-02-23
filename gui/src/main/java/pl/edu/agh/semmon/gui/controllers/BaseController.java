package pl.edu.agh.semmon.gui.controllers;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.json.JSON;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import pl.edu.agh.semmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semmon.gui.controllers.action.ReflectionButtonPressListener;
import pl.edu.agh.semmon.gui.controllers.action.RelectionButtonPressListenerFactory;
import pl.edu.agh.semmon.gui.util.UriUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Base class for all controllers. Most helpful in initialization process - serializes given resource to
 * pivot objects, binds all @Action event handlers
 *
 * @author tkozak
 *         Created at 20:54 04-06-2010
 */
public abstract class BaseController<T extends Component> implements Controller<T> {

  private static final Logger log = LoggerFactory.getLogger(BaseController.class.getName());

  private static final String DEFAULT_ACTION_METHOD_SUFFIX = "ButtonPressed";

  private static final int DEFAULT_ACTION_TARGET_STRIP_CHARS = "Pressed".length();

  protected static final String DATE_TIME_FORMAT_KEY = "general.dateTimeFormat";
  protected static final String TIME_FORMAT_KEY = "general.timeFormat";

  private static final String RESOURCE_FILE = "i18n/Semmon";

  protected T component;

  protected Resource bxmlContentResource;

  protected BXMLSerializer serializer;

  private RelectionButtonPressListenerFactory listenerFactory;

  protected Resources resources;

  public T getComponent() {
    return component;
  }

  @Override
  @PostConstruct
  public void deserializeContent() throws SerializationException, IOException, IllegalAccessException {
    resources = new Resources(RESOURCE_FILE);
    serializer = new BXMLSerializer();
    serializer.setResources(resources);
    //noinspection unchecked
    preSerialize();
    component = (T) serializer.readObject(bxmlContentResource.getURL(), resources);
    Class clazz = getBindableClass();
    log.debug("Binding with class: {}", clazz.getName());
    serializer.bind(this, clazz);
    bindActions(clazz);
    postBinding();
  }

  private void bindActions(Class clazz) throws IllegalAccessException {
    for (final Method method : clazz.getDeclaredMethods()) {
      ButtonAction buttonAction = method.getAnnotation(ButtonAction.class);
      if (buttonAction == null) {
        continue;
      }
      String targetItem;
      if (!buttonAction.target().isEmpty()) {
        targetItem = buttonAction.target();
      } else if (method.getName().endsWith(DEFAULT_ACTION_METHOD_SUFFIX)) {
        String methodName = method.getName();
        targetItem = methodName.substring(0, methodName.length() - DEFAULT_ACTION_TARGET_STRIP_CHARS);
      } else {
        continue;
      }
      for (Field field : clazz.getDeclaredFields()) {
        if (field.getName().equals(targetItem) && Button.class.isAssignableFrom(field.getType())) {
          field.setAccessible(true);
          method.setAccessible(true);
          Button button = (Button) field.get(this);
          if(button == null) {
            log.error("Button not found: " + field.getName());
            continue;
          }
          final ReflectionButtonPressListener listener = listenerFactory.createListener(this, method);
          if (buttonAction.type() == ButtonAction.Type.INSTANT) {
            listener.setExecutorService(null);
          } else {

          }
          button.getButtonPressListeners().add(listener);
          break;
        }
      }

    }
  }

  protected abstract Class getBindableClass();

  protected void preSerialize() throws IOException {

  }

  protected void postBinding() throws IOException {
  }

  public void setBxmlContentResource(Resource bxmlContentResource) {
    this.bxmlContentResource = bxmlContentResource;
  }

  public void setListenerFactory(RelectionButtonPressListenerFactory listenerFactory) {
    this.listenerFactory = listenerFactory;
  }

  protected String getCapabilityLabel(String capName) {
    return UriUtils.getCapabilityLabel(capName);
  }

  protected String getFormattedDate(Date date) {
    Date now = new Date();
    String timePattern;
    if (now.getTime() - date.getTime() > 1000 * 60 * 60 * 24) {
      timePattern = JSON.get(resources, DATE_TIME_FORMAT_KEY);
    } else {
      timePattern = JSON.get(resources, TIME_FORMAT_KEY);
    }
    return DateFormatUtils.format(date, timePattern);
  }
}
