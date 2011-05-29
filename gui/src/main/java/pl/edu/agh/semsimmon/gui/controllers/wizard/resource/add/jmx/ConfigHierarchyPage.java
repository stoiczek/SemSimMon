package pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.jmx;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.TextInput;
import pl.edu.agh.semsimmon.gui.controllers.wizard.BaseWizardPageController;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:16 27-08-2010
 */
public class ConfigHierarchyPage extends BaseWizardPageController<BoxPane> {

  @BXML
  private TextInput applicationTextInput;

  @BXML
  private TextInput clusterTextInput;

  private String application;
  private String clusterId;

  @Override
  public void pageShowing() {

  }

  @Override
  public void pageHiding(boolean forward) {
    clusterId = clusterTextInput.getText();
    application = applicationTextInput.getText();

  }

  @Override
  protected Class getBindableClass() {
    return ConfigHierarchyPage.class;
  }

  public String getApplication() {
    return application;
  }

  public String getClusterId() {
    return clusterId;
  }
}
