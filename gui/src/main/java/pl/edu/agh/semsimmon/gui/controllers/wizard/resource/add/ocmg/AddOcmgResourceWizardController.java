package pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.ocmg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.gui.controllers.wizard.BaseWizardPageController;
import pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.BaseAddResourceWizardCtrl;
import pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.ConfigMonitorPage;
import pl.edu.agh.semsimmon.gui.logic.resource.ResourcesService;

import java.io.IOException;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 11:43 29-08-2010
 */
public class AddOcmgResourceWizardController extends BaseAddResourceWizardCtrl {

  private static final Logger log = LoggerFactory.getLogger(AddOcmgResourceWizardController.class);

  private ResourcesService resourcesService;

  @Override
  protected void wizardFinished()  {
    log.debug("Add Ocmg resource wizard finished.");
    final SelectAppsPageController selectAppsPageController = (SelectAppsPageController) pages.get(2);
    final List<Resource> applications = selectAppsPageController.getSelectedApplications();
    log.debug("Adding {} applications", applications.size());
    for(Resource app : applications ) {
      try {
        resourcesService.addOcmgApplication(app);
      } catch (ResourceAlreadyRegisteredException e) {
        // TODO drop that fckn exception
      }
    }
  }


  @Override
  protected void pageSwitching(BaseWizardPageController oldPage, BaseWizardPageController newPage) {
    if (oldPage instanceof ConfigMonitorPage) {
      try {
        initConnection((ConfigMonitorPage) oldPage);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else if (oldPage instanceof ConfigMainSMPage) {
      populateApps((ConfigMainSMPage) oldPage, (SelectAppsPageController) newPage);
    }
  }


  private void populateApps(ConfigMainSMPage smPage, SelectAppsPageController selectApps) {
    final String connectionString = smPage.getConnectionString();
    log.debug("Populating application from MainSM: {}" , connectionString);
    List<Resource> apps = resourcesService.getOcmgApplications(connectionString, connectionId);
    selectApps.populateApplications(apps);
  }

  public void setResourcesService(ResourcesService resourcesService) {
    this.resourcesService = resourcesService;
  }
}
