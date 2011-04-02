package pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.ocmg;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Checkbox;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.gui.controllers.wizard.BaseWizardPageController;
import pl.edu.agh.semsimmon.gui.util.ListItemDataContainer;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 11:43 29-08-2010
 */
public class SelectAppsPageController extends BaseWizardPageController<BoxPane> {

  @BXML
  private BoxPane applicationsWrapper;

  private List<Checkbox> applicationCheckboxes = new LinkedList<Checkbox>();

  private List<Resource > selectedApplications = new LinkedList<Resource>();

  @Override
  public void pageShowing() {

  }

  @Override
  public void pageHiding() {
    for(Checkbox appCheckbox : applicationCheckboxes) {
      if(appCheckbox.isSelected()) {
        ListItemDataContainer container = (ListItemDataContainer) appCheckbox.getButtonData();
        selectedApplications.add((Resource) container.getAdditionalContent() );
      }
    }
  }

  @Override
  protected Class getBindableClass() {
    return SelectAppsPageController.class;
  }

  public void populateApplications(List<Resource> applications) {
    applicationCheckboxes.clear();
    applicationsWrapper.clear();
    for (Resource app : applications) {
      ListItemDataContainer appContainer = new ListItemDataContainer();
      appContainer.setAdditionalContent(app);
      appContainer.setText(app.getProperty(ResourcePropertyNames.Application.NAME).toString());
      Checkbox checkbox = new Checkbox();
      checkbox.setButtonData(appContainer);
      applicationCheckboxes.add(checkbox);
      applicationsWrapper.add(checkbox);
    }
  }

  public List<Resource> getSelectedApplications() {
    return selectedApplications;
  }
}
