package pl.edu.agh.semmon.gui.controllers.tab.visualization;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Vote;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtkx.WTKX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.semmon.gui.UiFactory;
import pl.edu.agh.semmon.gui.controllers.tab.BaseTabController;

import java.io.IOException;

/**
 * Controls visualizations.
 *
 * @author tkozak
 *         Created at 20:35 10-08-2010
 */
public class VisualizationsTabController extends BaseTabController<TabPane> {

  private static final Logger log = LoggerFactory.getLogger(VisualizationsTabController.class);

  @WTKX
  private TabPane visualizationsTabPane;

  @Autowired
  private UiFactory uiFactory;

  @Override
  protected Class getBindableClass() {
    return VisualizationsTabController.class;
  }

  @Override
  protected void postBinding() throws IOException {
    VisualizationController controller = uiFactory.createVisualizationTab();
    final BoxPane tab = controller.getComponent();
    component.getTabs().insert(tab, 0);
    TabPane.setLabel(tab, "Visualization1");
    component.setSelectedIndex(0);
    visualizationsTabPane.getTabPaneSelectionListeners().add(new TabPaneSelectionListener.Adapter() {

      @Override
      public Vote previewSelectedIndexChange(TabPane tabPane, int selectedIndex) {
        if (selectedIndex == tabPane.getTabs().getLength() - 1) {
          log.debug("Selected last tab");
          addTab(selectedIndex);
          return Vote.DENY;
        }
        return Vote.APPROVE;
      }
    });
    visualizationsTabPane.getTabPaneListeners().add(new TabPaneListener.Adapter() {
      @Override
      public void tabsRemoved(TabPane tabPane, int index, Sequence<Component> tabs) {
        if (tabPane.getTabs().getLength() == 1) {
          addTab(0);
        } else {
          tabPane.setSelectedIndex(0);
        }
      }
    });
  }

  public void pauseAllVisualizations() {
    
  }

  public void resumeAllVisualizations() {

  }

  public void removeAllVisualizations() {

  }

  private void addTab(int selectedIndex) {
    VisualizationController controller = uiFactory.createVisualizationTab();
    visualizationsTabPane.getTabs().insert(controller.getComponent(), selectedIndex);
    TabPane.setLabel(controller.getComponent(), "Test" + selectedIndex);
    visualizationsTabPane.setSelectedIndex(selectedIndex);
    TabPane.setCloseable(controller.getComponent(), true);
  }


}
