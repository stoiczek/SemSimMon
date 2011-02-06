package pl.edu.agh.semmon.gui.controllers.tab;

import org.apache.pivot.wtk.Component;
import org.springframework.core.io.Resource;
import pl.edu.agh.semmon.gui.controllers.BaseController;

/**
 * Base class for all tab controller. Provides usefull initialization mechanisms.
 *
 * @author tkozak
 *         Created at 19:47 04-06-2010
 */
public abstract class BaseTabController<T extends Component> extends BaseController<T> implements TabController<T> {

  private String tabLabelKey;

  private Resource tabIcon;

  @Override

  public T getTabContent() {
    return component;
  }

  @Override
  public String getTabLabelKey() {
    return tabLabelKey;
  }

  @Override
  public Resource getTabIcon() {
    return tabIcon;
  }

  public void setTabLabelKey(String tabLabelKey) {
    this.tabLabelKey = tabLabelKey;
  }

  public void setTabIcon(Resource tabIcon) {
    this.tabIcon = tabIcon;
  }
}
