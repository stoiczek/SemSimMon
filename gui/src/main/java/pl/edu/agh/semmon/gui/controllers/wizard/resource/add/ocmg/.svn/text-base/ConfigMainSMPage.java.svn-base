package pl.edu.agh.semmon.gui.controllers.wizard.resource.add.ocmg;

import org.apache.pivot.wtk.*;
import org.apache.pivot.wtkx.WTKX;
import pl.edu.agh.semmon.gui.controllers.wizard.BaseWizardPageController;

/**
 * @author tkozak
 *         Created at 11:43 29-08-2010
 */
public class ConfigMainSMPage extends BaseWizardPageController<BoxPane> {

  private static final String CONNECTION_STRING_PATTERN =
      "[0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f]:[0-9a-f][0-9a-f][0-9a-f]" +
          "[0-9a-f]";

  @WTKX
  private TextInput connStringInput;

  @WTKX
  private FlowPane connStringPane;

  @Override
  public void pageShowing() {

  }

  @Override
  public void pageHiding() {
    final String connString = getConnectionString();
    if (!connString.matches(CONNECTION_STRING_PATTERN)) {
      Form.Flag flag = new Form.Flag(MessageType.ERROR, resources.getString("wizards.resources.add.ocmg.invalidConnString"));
      Form.setFlag(connStringPane, flag);
      throw new IllegalStateException("Invalid connection string");
    }
  }

  @Override
  protected Class getBindableClass() {
    return ConfigMainSMPage.class;
  }

  public String getConnectionString() {
    return connStringInput.getText().trim();
  }

}
