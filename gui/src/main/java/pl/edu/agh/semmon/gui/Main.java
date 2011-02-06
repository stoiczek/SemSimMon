package pl.edu.agh.semmon.gui;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author tkozak
 *         Created at 57:09 02-06-2010
 * @TODO description
 */
public class Main implements Application {

  private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

  private static Main INSTANCE;

  public static Main getINSTANCE() {
    return INSTANCE;
  }

  private static final String[] APP_CONFIG_LOCATIONS = new String[]{
      "spring/logicContext.xml",
      "spring/uiContext.xml",
      "jmxtransport-context.xml",
      "semmonCoreContext.xml",
      "ocmgtransport-context.xml",
      "consts-beans.xml"
  };

  private static final String DISPLAY_BEAN_ID = "display";

  ClassPathXmlApplicationContext ctx;

  @Override
  public void resume() throws Exception {
    log.debug("Resuming application");
  }

  @Override
  public void suspend() throws Exception {
    log.debug("Suspending application");
  }

  @Override
  public boolean shutdown(boolean optional) throws Exception {
    log.debug("Shutting down application");
    Window mainWindow = (Window) ctx.getBeansOfType(Window.class).values().iterator().next();
    mainWindow.close();
    ctx.stop();
    return false;
  }

  @Override
  public void startup(final Display display, Map<String, String> properties) throws Exception {
    log.debug("Starting up application");
    INSTANCE = this;
    ctx = new ClassPathXmlApplicationContext(APP_CONFIG_LOCATIONS, false);
    ctx.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
      @Override
      public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerSingleton(DISPLAY_BEAN_ID, display);
      }
    });
    ctx.refresh();
    ctx.start();
  }

  public static void main(String[] args) {
    DesktopApplicationContext.main(Main.class, args);
  }
}
