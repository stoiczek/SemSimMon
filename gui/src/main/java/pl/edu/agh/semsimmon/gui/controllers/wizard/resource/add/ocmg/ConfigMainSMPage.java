package pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.ocmg;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.json.JSON;
import org.apache.pivot.wtk.*;
import pl.edu.agh.semsimmon.gui.controllers.wizard.BaseWizardPageController;
import pl.edu.agh.semsimmon.gui.controllers.wizard.VetoException;

/**
 * @author tkozak
 *         Created at 11:43 29-08-2010
 */
public class ConfigMainSMPage extends BaseWizardPageController<BoxPane> {

  private static final String CONNECTION_STRING_PATTERN =
      "[0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f]:[0-9a-f][0-9a-f][0-9a-f]" +
          "[0-9a-f]";

  @BXML
  private TextInput connStringInput;

  @BXML
  private FlowPane connStringPane;

  @Override
  public void pageShowing() {

  }

  @Override
  public void pageHiding(boolean forward) {
    final String connString = getConnectionString().trim().toLowerCase();
    if (!connString.matches(CONNECTION_STRING_PATTERN) && forward) {
      Form.Flag flag = new Form.Flag(MessageType.ERROR, JSON.<String>get(resources,
          "wizards.resources.add.ocmg.invalidConnString"));
      Form.setFlag(connStringPane, flag);
      throw new VetoException("Invalid connection string");
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
