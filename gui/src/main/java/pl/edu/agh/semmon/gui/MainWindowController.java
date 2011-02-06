package pl.edu.agh.semmon.gui;

import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtkx.WTKX;
import org.apache.pivot.wtkx.WTKXSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import pl.edu.agh.semmon.gui.controllers.BaseController;
import pl.edu.agh.semmon.gui.controllers.tab.TabController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author tkozak
 *         Created at 32:20 02-06-2010
 * @TODO description
 */
public class MainWindowController extends BaseController<Window> {

  @WTKX
  private TabPane mainTabPane;

  @Autowired(required = false)
  private Display display;

  @Autowired(required = false)
  private List<TabController> tabControllers;

  private Map<String, Action> actionMap;


  @Override
  protected Class getBindableClass() {
    return MainWindowController.class;
  }

  @Override
  protected void preSerialize() throws IOException {
    for(Map.Entry<String,  Action> entry : actionMap.entrySet()) {
      Action.getNamedActions().put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  protected void postBinding() throws IOException {
    try {
      for (TabController controller : tabControllers) {
        controller.serializeContent();
        mainTabPane.getTabs().add(controller.getTabContent());
        TabPane.setLabel(controller.getTabContent(), resources.getString(controller.getTabLabelKey()));
        TabPane.setIcon(controller.getTabContent(), controller.getTabIcon().getURL());
      }
      component.setVisible(true);
      component.open(display);
    } catch (SerializationException e) {
      throw new IOException(e);
    } catch (IllegalAccessException e) {
      throw new IOException(e);
    }
  }
  
  public void setDisplay(Display display) {
    this.display = display;
  }

  public Window getWindow() {
    return component;
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public void setTabControllers(List<TabController> tabControllers) {
    this.tabControllers = tabControllers;
  }

  public void setActionMap(Map<String, Action> actionMap) {
    this.actionMap = actionMap;
  }
}
