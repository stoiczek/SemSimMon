package pl.edu.agh.semsimmon.gui;

import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.gui.controllers.dialog.AddMeasurToVisDialogCtrl;
import pl.edu.agh.semsimmon.gui.controllers.dialog.AddMeasurementDialogController;
import pl.edu.agh.semsimmon.gui.controllers.tab.visualization.VisualizationController;
import pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.jmx.AddJMXResourceWizardController;
import pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.ocmg.AddOcmgResourceWizardController;

import java.io.IOException;

/**
 * Factory for dynamic ui components.
 *
 * @author tkozak
 *         Created at 19:09 04-06-2010
 */
public abstract class UiFactory {

  /**
   * Creates new AddMeasurementDialogController for adding measurements to given resource
   *
   * @param resource resource which will get new measurement.
   * @return newly created AddMeasurementDialogController
   * @throws java.io.IOException on error with getting resource possible capabilities.
   */
  public AddMeasurementDialogController createAddMeasurementController(Resource resource) throws IOException {
    final AddMeasurementDialogController controller = createAddMeasurementControllerInternal();
    controller.setResource(resource);
    controller.setupCapabilities();
    return controller;
  }

  /**
   * =================================================================================
   * Spring lookup methods
   * =================================================================================
   */

  public abstract AddMeasurToVisDialogCtrl createAddMeasurToVisDialog();

  /**
   * @return
   */
  public abstract AddJMXResourceWizardController createAddJmxResourceDialog();

  /**
   * @return
   */
  public abstract AddOcmgResourceWizardController createAddOcmgResourceDialog();

  /**
   * @return
   */
  public abstract VisualizationController createVisualizationTab();

  /**
   * @return
   */
  protected abstract AddMeasurementDialogController createAddMeasurementControllerInternal();

}
