package pl.edu.agh.semmon.gui.controllers.dialog;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.*;
import pl.edu.agh.semmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semmon.gui.controllers.tab.visualization.VisualizationController;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;
import pl.edu.agh.semmon.gui.util.ListItemDataContainer;

import java.io.IOException;
import java.util.List;

/**
 * Controller of Dialog responsible of adding measurement to visualization.
 *
 * @author tkozak
 *         Created at 19:45 18-08-2010
 */
public class AddMeasurToVisDialogCtrl extends BaseDialogController {

  @BXML
  private ListView measurementsList;

  @BXML
  private TablePane measurementDetails;

  @BXML(id = "details.measurementResourceName")
  private Label measurementResourceName;

  @BXML(id = "details.measurementCapabilityName")
  private Label measurementCapabilityName;

  @BXML(id = "details.measurementStartDate")
  private Label measurementStartDate;

  @BXML(id = "details.measurementId")
  private Label measurementId;

  @BXML
  private PushButton okButton;

  @BXML
  private PushButton cancelButton;


  private VisualizationController owner;

  @Override
  protected Class getBindableClass() {
    return AddMeasurToVisDialogCtrl.class;
  }

  @Override
  protected void postBinding() throws IOException {
    measurementsList.getListViewSelectionListeners().add(new DetailsFillSelectionListener());
  }


  @ButtonAction
  private void okButtonPressed() {
    Sequence items = measurementsList.getSelectedItems();
    if (items != null) {
      for (int i = 0; i < items.getLength(); i++) {
        ListItemDataContainer container = (ListItemDataContainer) items.get(i);
        owner.addMeasurement((Measurement) container.getAdditionalContent());
      }
    }
    cancelButtonPressed();
  }

  @ButtonAction
  private void cancelButtonPressed() {
    component.clear();
    component.close();
  }

  public void initialize(VisualizationController owner, List<Measurement> measurements) {
    this.owner = owner;
    for (Measurement measurement : measurements) {
      ListItemDataContainer listItem = new ListItemDataContainer();
      listItem.setAdditionalContent(measurement);
      listItem.setText(measurement.getLabel());
      final org.apache.pivot.collections.List<ListItemDataContainer> data =
          (org.apache.pivot.collections.List<ListItemDataContainer>) measurementsList.getListData();
      data.add(listItem);
      measurementsList.setListData(data);
    }
    component.setLocation(100, 100);
    component.open(parentWindow);
  }

  private Measurement getMeasurement() {
    ListItemDataContainer container = (ListItemDataContainer) measurementsList.getSelectedItem();
    Measurement measurement = (Measurement) container.getAdditionalContent();
    return measurement;
  }

  private class DetailsFillSelectionListener extends ListViewSelectionListener.Adapter {


    @Override
    public void selectedRangesChanged(ListView listView, Sequence<Span> previousSelectedRanges) {
      // TODO copied and pasted to add measurement to visualization dialog - pull it up and reuse
      Measurement measurement = getMeasurement();
      measurementResourceName.setText(measurement.getResourceUri());
      measurementCapabilityName.setText(getCapabilityLabel(measurement.getCapabilityUri()));
      measurementId.setText(measurement.getId());
      measurementStartDate.setText(getFormattedDate(measurement.getCreationDate()));
    }


  }


}
