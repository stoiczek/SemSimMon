package pl.edu.agh.semmon.gui.controllers.tab.visualization;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Vote;
import org.apache.pivot.wtk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.semmon.gui.UiFactory;
import pl.edu.agh.semmon.gui.controllers.tab.BaseTabController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls visualizations.
 *
 * @author tkozak
 *         Created at 20:35 10-08-2010
 */
public class VisualizationsTabController extends BaseTabController<TabPane> {

  private static final Logger log = LoggerFactory.getLogger(VisualizationsTabController.class);

  @BXML
  private TabPane visualizationsTabPane;

  @Autowired
  private UiFactory uiFactory;


  private List<VisualizationController> controllers = new ArrayList<VisualizationController>();

  @Override
  protected Class getBindableClass() {
    return VisualizationsTabController.class;
  }

  @Override
  protected void postBinding() throws IOException {
    VisualizationController controller = uiFactory.createVisualizationTab();
    final BoxPane tab = controller.getComponent();
    component.getTabs().insert(tab, 0);
    TabPane.setTabData(tab, "Visualization1");
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
        if (tabPane.getTabs().getLength() == 0) {
          addTab(0);
        } else {
          tabPane.setSelectedIndex(0);
        }
      }
    });
  }

  public void pauseAllVisualizations() {
    log.debug("Pausing all visualizations");
    for (VisualizationController ctrl : controllers) {
      ctrl.pauseVisualization();
    }
  }

  public void resumeAllVisualizations() {
    log.debug("Resuming all visualizations");
    for (VisualizationController ctrl : controllers) {
      ctrl.resumeVisualization();
    }
  }

  public void removeAllVisualizations() {
    log.debug("Removing all visualizations");
    TabPane.TabSequence tabs = visualizationsTabPane.getTabs();
    tabs.remove(0, tabs.getLength() - 1);
    controllers.clear();
  }

  private void addTab(int selectedIndex) {
    VisualizationController controller = uiFactory.createVisualizationTab();
    visualizationsTabPane.getTabs().insert(controller.getComponent(), selectedIndex);
    TabPane.setTabData(controller.getComponent(), "Visualization" + selectedIndex);
    visualizationsTabPane.setSelectedIndex(selectedIndex);
    controllers.add(controller);
  }


}
