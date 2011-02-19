package pl.edu.agh.semmon.gui.controllers.tab;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.*;
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


  @BXML
  private ListView measurementsList;

  @BXML
  private PushButton removeMeasurementButton;

  @BXML
  private PushButton pauseMeasurementButton;

  @BXML
  private PushButton resumeMeasurementButton;

  @BXML
  private TablePane measurementDetails;

  @BXML
  private TableView measurementValues;

//  @BXML(id = "details.measurementResourceName")
  @BXML
  private Label measurementResourceName;

//  @BXML(id = "details.measurementCapabilityName")
  @BXML
  private Label measurementCapabilityName;

//  @BXML(id = "details.measurementStartDate")
  @BXML
  private Label measurementStartDate;

//  @BXML(id = "details.measurementId")
  @BXML
  private Label measurementId;

  private Map<String, ListItemDataContainer> measurementsMap = new HashMap();

  private MeasurementService measurementService;

  private Measurement currentlySelectedMeasurement;

  private boolean publishValues = false;

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
  private synchronized void removeMeasurementButtonPressed() throws MeasurementException {
    ListItemDataContainer container = (ListItemDataContainer) measurementsList.getSelectedItem();
    if (container == null) {
      return;
    }
    Measurement measurement = (Measurement) container.getAdditionalContent();
    measurementService.removeMeasurement(measurement.getId());
    publishValues = false;
    measurementValues.getTableData().clear();
    if (currentlySelectedMeasurement != null) {
      currentlySelectedMeasurement.removeCapabilityValueListener(MeasurementsTabController.this);
    }
    pauseMeasurementButton.setVisible(true);
    resumeMeasurementButton.setVisible(false);
    measurementResourceName.setText("");
    measurementCapabilityName.setText("");
    measurementId.setText("");
    measurementStartDate.setText("");
    currentlySelectedMeasurement = null;


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
  public synchronized void newCapabilityValues(java.util.List<CapabilityValue> values) {
    if (!publishValues) {
      return;
    }
    final List data = measurementValues.getTableData();
        displayMeasurementValues(data, values);
//    measurementValues.repaint();
  }

  public void setMeasurementService(MeasurementService measurementService) {
    this.measurementService = measurementService;
  }

  private class DetailsFillSelectionListener extends ListViewSelectionListener.Adapter {


    @Override
    public void selectedRangesChanged(ListView listView, Sequence<Span> previousSelectedRanges) {
      // TODO copied and pasted to add measurement to visualization dialog - pull it up and reuse


      ListItemDataContainer container = (ListItemDataContainer) listView.getSelectedItem();
      final List valueRows = measurementValues.getTableData();
      valueRows.clear();
      if (currentlySelectedMeasurement != null) {
        currentlySelectedMeasurement.removeCapabilityValueListener(MeasurementsTabController.this);
      }
      if (container == null) {
        pauseMeasurementButton.setVisible(true);
        resumeMeasurementButton.setVisible(false);
        measurementResourceName.setText("");
        measurementCapabilityName.setText("");
        measurementId.setText("");
        measurementStartDate.setText("");
        currentlySelectedMeasurement = null;
        return;
      }
      Measurement measurement = (Measurement) container.getAdditionalContent();
      if (measurement.isActive()) {
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

      currentlySelectedMeasurement = measurement;
      publishValues = true;
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

  private void displayMeasurementValues(List data, java.util.List<CapabilityValue> values) {
    for (CapabilityValue value : values) {
      org.apache.pivot.collections.HashMap dataMap = new org.apache.pivot.collections.HashMap();
      dataMap.put("timestamp", getFormattedDate(value.getGatherTimestamp()));
      dataMap.put("value", value.getNumericValue().toString());
      data.add(dataMap);
    }
  }
}
