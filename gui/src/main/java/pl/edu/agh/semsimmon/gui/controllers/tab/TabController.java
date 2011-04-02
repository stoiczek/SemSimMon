package pl.edu.agh.semsimmon.gui.controllers.tab;

import org.apache.pivot.wtk.Component;
import org.springframework.core.io.Resource;
import pl.edu.agh.semsimmon.gui.controllers.Controller;

/**
 * Interface for all tab controllers.
 *
 * @author tkozak
 *         Created at 19:27 04-06-2010
 */
public interface TabController<T extends Component> extends Controller<T> {

  /**
   * Returns component which should be emeded on main window's tab pane.
   *
   * @return conponent to embed.
   */
  Component getTabContent();

  String getTabLabelKey();

  Resource getTabIcon();
}
