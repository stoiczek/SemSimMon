package pl.edu.agh.semmon.gui.controllers.dialog;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtkx.WTKX;
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

  @WTKX
  private ListView measurementsList;

  @WTKX
  private TablePane measurementDetails;

  @WTKX(id = "details.measurementResourceName")
  private Label measurementResourceName;

  @WTKX(id = "details.measurementCapabilityName")
  private Label measurementCapabilityName;

  @WTKX(id = "details.measurementStartDate")
  private Label measurementStartDate;

  @WTKX(id = "details.measurementId")
  private Label measurementId;

  @WTKX
  private PushButton okButton;

  @WTKX
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
    for (int i = 0; i < items.getLength(); i++) {
      ListItemDataContainer container  = (ListItemDataContainer) items.get(i);
      owner.addMeasurement((Measurement) container.getAdditionalContent());
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
