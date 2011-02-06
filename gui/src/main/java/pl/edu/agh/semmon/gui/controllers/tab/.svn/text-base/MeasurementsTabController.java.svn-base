package pl.edu.agh.semmon.gui.controllers.tab;

import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtkx.WTKX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semmon.common.exception.MeasurementException;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;
import pl.edu.agh.semmon.gui.logic.metric.MeasurementService;
import pl.edu.agh.semmon.gui.logic.metric.MeasurementsListener;
import pl.edu.agh.semmon.gui.util.ListItemDataContainer;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * Controller of measurements tab.
 *
 * @author tkozak
 *         Created at 20:50 04-06-2010
 */
public class MeasurementsTabController extends BaseTabController implements MeasurementsListener,
    CapabilityValueListener {

  private static final Logger log = LoggerFactory.getLogger(MeasurementsTabController.class);


  @WTKX
  private ListView measurementsList;

  @WTKX
  private PushButton removeMeasurementButton;

  @WTKX
  private PushButton pauseMeasurementButton;

  @WTKX
  private PushButton resumeMeasurementButton;

  @WTKX
  private TablePane measurementDetails;

  @WTKX
  private TablePane measurementValues;

  @WTKX(id = "details.measurementResourceName")
  private Label measurementResourceName;

  @WTKX(id = "details.measurementCapabilityName")
  private Label measurementCapabilityName;

  @WTKX(id = "details.measurementStartDate")
  private Label measurementStartDate;

  @WTKX(id = "details.measurementId")
  private Label measurementId;

  private Map<String, ListItemDataContainer> measurementsMap = new HashMap();

  private MeasurementService measurementService;

  private Measurement currentlySelectedMeasurement;

  @Override
  protected Class getBindableClass() {
    return MeasurementsTabController.class;
  }

  @Override
  protected void postBinding() throws IOException {
    measurementsList.setSelectMode(ListView.SelectMode.SINGLE);
    measurementsList.getListViewSelectionListeners().add(new DetailsFillSelectionListener());
  }

  @ButtonAction
  private void removeMeasurementButtonPressed() throws MeasurementException {
    ListItemDataContainer container = (ListItemDataContainer) measurementsList.getSelectedItem();
    if (container == null) {
      return;
    }
    Measurement measurement = (Measurement) container.getAdditionalContent();
    measurementService.removeMeasurement(measurement.getId());
  }

  @ButtonAction
  private void pauseMeasurementButtonPressed() throws MeasurementException {

    ListItemDataContainer container = (ListItemDataContainer) measurementsList.getSelectedItem();
    if (container == null) {
      return;
    }
    Measurement measurement = (Measurement) container.getAdditionalContent();
    measurementService.pauseMeasurement(measurement.getId());
    pauseMeasurementButton.setVisible(false);
    resumeMeasurementButton.setVisible(true);
  }

  @ButtonAction
  private void resumeMeasurementButtonPressed() throws MeasurementException {
    ListItemDataContainer container = (ListItemDataContainer) measurementsList.getSelectedItem();
    if (container == null) {
      return;
    }
    Measurement measurement = (Measurement) container.getAdditionalContent();
    measurementService.resumeMeasurement(measurement.getId());
    pauseMeasurementButton.setVisible(true);
    resumeMeasurementButton.setVisible(false);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public void measurementCreated(Measurement measurement) {
    log.debug("Got new measurement created event. Adding measurement to list.");
    ListItemDataContainer listItem = new ListItemDataContainer();
    listItem.setAdditionalContent(measurement);
    listItem.setText(measurement.getLabel());
    measurementsMap.put(measurement.getId(), listItem);
    final List<ListItemDataContainer> data = (List<ListItemDataContainer>) measurementsList.getListData();
    data.add(listItem);
    measurementsList.setListData(data);
  }

  @Override
  public void measurementRemoved(Measurement measurement) {
    if (!measurementsMap.containsKey(measurement.getId())) {
      log.warn("Got measurement removed event, with measurement not being registered");
      return;
    }
    ListItemDataContainer listItem = measurementsMap.get(measurement.getId());
    final List<ListItemDataContainer> data = (List<ListItemDataContainer>) measurementsList.getListData();
    data.remove(listItem);
    measurementsList.setListData(data);
  }

  @Override
  public void newCapabilityValues(java.util.List<CapabilityValue> values) {
    final TablePane.RowSequence valueRows = measurementValues.getRows();
    displayMeasurementValues(valueRows, values);
    measurementValues.repaint();
  }

  public void setMeasurementService(MeasurementService measurementService) {
    this.measurementService = measurementService;
  }

  private class DetailsFillSelectionListener extends ListViewSelectionListener.Adapter {


    @Override
    public void selectedRangesChanged(ListView listView, Sequence<Span> previousSelectedRanges) {
      // TODO copied and pasted to add measurement to visualization dialog - pull it up and reuse

      ListItemDataContainer container = (ListItemDataContainer) listView.getSelectedItem();
      Measurement measurement = (Measurement) container.getAdditionalContent();
      if(measurement.isActive()) {
        pauseMeasurementButton.setVisible(true);
        resumeMeasurementButton.setVisible(false);
      } else {
        pauseMeasurementButton.setVisible(false);
        resumeMeasurementButton.setVisible(true);
      }
      measurementResourceName.setText(measurement.getResourceUri());
      measurementCapabilityName.setText(getCapabilityLabel(measurement.getCapabilityUri()));
      measurementId.setText(measurement.getId());
      measurementStartDate.setText(getFormattedDate(measurement.getCreationDate()));
      if (currentlySelectedMeasurement != null) {
        currentlySelectedMeasurement.removeCapabilityValueListener(MeasurementsTabController.this);
      }
      currentlySelectedMeasurement = measurement;
      final TablePane.RowSequence valueRows = measurementValues.getRows();
      if (valueRows.getLength() > 1) {
        valueRows.remove(1, valueRows.getLength() - 2);
      }
      measurement.addCapabilityValueListener(MeasurementsTabController.this);
      java.util.List<CapabilityValue> values = measurement.getValues();
      Collections.sort(values, new Comparator<CapabilityValue>() {
        @Override
        public int compare(CapabilityValue o1, CapabilityValue o2) {
          return o1.getGatherTimestamp().compareTo(o2.getGatherTimestamp());
        }
      });
      displayMeasurementValues(valueRows, values);
    }

  }

  private void displayMeasurementValues(TablePane.RowSequence valueRows, java.util.List<CapabilityValue> values) {
    for (CapabilityValue value : values) {
      TablePane.Row row = new TablePane.Row();
      row.add(new Label(getFormattedDate(value.getGatherTimestamp())));
      row.add(new Label(value.getNumericValue().toString()));
      valueRows.add(row);
    }
  }
}
