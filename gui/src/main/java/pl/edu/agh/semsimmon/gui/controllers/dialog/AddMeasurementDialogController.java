package pl.edu.agh.semsimmon.gui.controllers.dialog;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.collections.LinkedList;
import org.apache.pivot.wtk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.exception.MeasurementException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;
import pl.edu.agh.semsimmon.gui.logic.metric.MeasurementService;
import pl.edu.agh.semsimmon.gui.util.ListItemDataContainer;

import java.io.IOException;
import java.util.List;

/**
 * Controller of view responsible for adding new measurement.
 *
 * @author tkozak
 *         Created at 18:41 07-08-2010
 */
public class AddMeasurementDialogController extends BaseDialogController {

  private static final Logger log = LoggerFactory.getLogger(AddMeasurementDialogController.class);

  @BXML
  private PushButton cancelButton;

  @BXML
  private PushButton okButton;

  @BXML
  private ListButton capabilityList;

  @BXML
  private TextInput pollIntervalTextInput;

  @BXML
  private TextInput labelTextInput;

  private MeasurementService measurementService;

  /**
   * Resource to which we'll be adding new measurement
   */
  private Resource resource;

  @Override
  protected Class getBindableClass() {
    return AddMeasurementDialogController.class;
  }


  public void setupCapabilities() throws IOException {
    final List<String> capabilities = measurementService.getAllCapabilitiesNames(resource);
    LinkedList<ListItemDataContainer> capabilitiesListItems = new LinkedList<ListItemDataContainer>();
    for(String capability : capabilities) {
      ListItemDataContainer container = new ListItemDataContainer();
      container.setAdditionalContent(capability);
      container.setText(getLabelForURI(capability));
      capabilitiesListItems.add(container);
    }
    capabilityList.setListData(capabilitiesListItems);
    capabilityList.setSelectedIndex(0);
    component.setLocation(100,100);
    component.open(parentWindow);
  }

  @ButtonAction
  private void cancelButtonPressed() {
    log.debug("Adding of new measurement was cancelled");
    closeFrame();
  }

  @ButtonAction(type = ButtonAction.Type.BACKGROUND)
  private void okButtonPressed() throws MeasurementException {
    log.debug("Adding new measurement");
    ListItemDataContainer container = (ListItemDataContainer) capabilityList.getSelectedItem();
    Measurement measurement = new Measurement();
    measurement.setCapabilityUri(container.getAdditionalContent().toString());
    measurement.setUpdateInterval(Long.parseLong(pollIntervalTextInput.getText()));
    measurement.setResource(resource);
    measurement.setLabel(labelTextInput.getText());
    measurementService.addMeasurement(measurement);
  }

  @ButtonAction(target = "okButton", type = ButtonAction.Type.INSTANT)
  private void closeFrame() {
    component.close();
    component.clear();
  }

  public void setMeasurementService(MeasurementService measurementService) {
    this.measurementService = measurementService;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }
}
