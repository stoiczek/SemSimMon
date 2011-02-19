package pl.edu.agh.semmon.gui.controllers.tab.visualization;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.wtk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.gui.UiFactory;
import pl.edu.agh.semmon.gui.controllers.BaseController;
import pl.edu.agh.semmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semmon.gui.controllers.dialog.AddMeasurToVisDialogCtrl;
import pl.edu.agh.semmon.gui.controllers.tab.visualization.chart.ChartType;
import pl.edu.agh.semmon.gui.controllers.tab.visualization.chart.VisualizationChart;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;
import pl.edu.agh.semmon.gui.logic.metric.MeasurementService;
import pl.edu.agh.semmon.gui.util.ButtonDataContainer;
import pl.edu.agh.semmon.gui.util.ListItemDataContainer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Controls single visualization tab.
 *
 * @author tkozak
 *         Created at 20:53 10-08-2010
 */
public class VisualizationController extends BaseController<BoxPane> {

  private static final int OPTIONS_COL_EXPANDED_WIDTH = 400;

  private static final Logger log = LoggerFactory.getLogger(VisualizationController.class);

  @BXML
  private BoxPane separator;

  @BXML
  private TablePane.Column optionsColumn;

  @BXML
  private TablePane.Row mainRow;

  @BXML
  private GridPane chartWrapper;

  @BXML
  private TextInput labelTextInput;

  @BXML
  private TextInput chartTitleTextInput;

  @BXML
  private TextInput chartSubtitleTextInput;

  @BXML
  private Form optionsForm;

  @BXML
  private ListView measurementsList;

  @BXML
  private PushButton addMeasurementPushButton;

  @BXML
  private PushButton removeMeasurementPushButton;

  @BXML
  private PushButton pauseVisualizationPushButton;

  @BXML
  private PushButton resumeVisualizationPushButton;

  @BXML
  private PushButton removeVisualizationPushButton;

  @BXML
  private ButtonGroup chartTypeButtonGroup;

  private VisualizationChart visualizationChart;

  private MeasurementService measurementService;

  private UiFactory uiFactory;

  private List<Measurement> measurements = new LinkedList<Measurement>();


  public void addMeasurement(Measurement measurement) {
    log.debug("Adding measurement to visualization {}", labelTextInput.getText());
    visualizationChart.addMeasurement(measurement);
    measurements.add(measurement);
    ListItemDataContainer container = new ListItemDataContainer();
    container.setText(measurement.getLabel());
    container.setAdditionalContent(measurement);
    final org.apache.pivot.collections.List data = measurementsList.getListData();
    data.add(container);
  }


  @Override
  protected Class getBindableClass() {
    return VisualizationController.class;
  }

  @ButtonAction
  private void removeVisualizationPushButtonPressed() {
    log.debug("Remove visualization button pressed");
    TabPane parent = (TabPane) component.getParent();
    parent.getTabs().remove(component);
  }

  @ButtonAction
  private void addMeasurementPushButtonPressed() {
    log.debug("Add measurement button pressed - showing add measurement to visualization dialog.");
    List<Measurement> measurements;
    measurements = measurementService.getAllMeasurements();
    measurements.removeAll(this.measurements);
    final AddMeasurToVisDialogCtrl dialog = uiFactory.createAddMeasurToVisDialog();
    dialog.initialize(this, measurements);
  }

  @ButtonAction
  private void removeMeasurementPushButtonPressed() {
    ListItemDataContainer container = (ListItemDataContainer) measurementsList.getSelectedItem();
    Measurement measurement = (Measurement) container.getAdditionalContent();
    visualizationChart.removeMeasurement(measurement);
    final org.apache.pivot.collections.List data = measurementsList.getListData();
    data.remove(container);
  }

  @ButtonAction
  private void pauseVisualizationPushButtonPressed() {
    visualizationChart.pause();
    pauseVisualizationPushButton.setVisible(false);
    resumeVisualizationPushButton.setVisible(true);
  }

  @ButtonAction
  private void resumeVisualizationPushButtonPressed() {
    visualizationChart.resume();
    component.repaint();
    pauseVisualizationPushButton.setVisible(true);
    resumeVisualizationPushButton.setVisible(false);
  }

  @Override
  protected void postBinding() throws IOException {
    final String chartTitle = "Visualization";
    Class defaultChartControllerClass = ChartType.getDefaultChartType().getChartClass();
    try {
      visualizationChart = (VisualizationChart) defaultChartControllerClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    visualizationChart.getChartComponent().setTitle(chartTitle);
    TabPane.setTabData(component, chartTitle);
    labelTextInput.setText(chartTitle);
    chartTitleTextInput.setText(chartTitle);
    final GridPane.Row row = new GridPane.Row();
    row.add(visualizationChart.getChartComponent());
    chartWrapper.getRows().add(row);
    mainRow.setHeight(component.getHeight());
    chartWrapper.getComponentMouseListeners().add(createOptionHideMouseListener());
    separator.getComponentMouseListeners().add(createOptionShowMouseListener());
    component.getComponentListeners().add(createResizingComponentListener());

    labelTextInput.getTextInputContentListeners().add(new TextInputContentListener.Adapter() {

      @Override
      public void textChanged(TextInput textInput) {
        TabPane.setTabData(component, textInput.getText());
      }
    });

    chartTitleTextInput.getTextInputContentListeners().add(new TextInputContentListener.Adapter() {

      @Override
      public void textChanged(TextInput textInput) {
        visualizationChart.getChartComponent().setTitle(textInput.getText());
        visualizationChart.getChartComponent().repaint();
      }
    });
    chartSubtitleTextInput.getTextInputContentListeners().add(new TextInputContentListener.Adapter() {
      @Override
      public void textChanged(TextInput textInput) {
        visualizationChart.updateChartSubTitle(textInput.getText());
        visualizationChart.getChartComponent().repaint();
      }
    });

    chartTypeButtonGroup.getButtonGroupListeners().add(new ButtonGroupListener.Adapter() {

      @Override
      public void selectionChanged(ButtonGroup buttonGroup, Button previousSelection) {
        final ButtonDataContainer data = (ButtonDataContainer) buttonGroup.getSelection().getButtonData();
        try {
          changeChart((ChartType) data.getEnumValue());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });

  }

  private void changeChart(ChartType enumValue) throws IllegalAccessException, InstantiationException,
      NoSuchMethodException, InvocationTargetException {
    log.debug("Changing chart type to: {}", enumValue.getChartClass().getSimpleName());
    visualizationChart.pause();
    visualizationChart.getChartComponent().clear();
    List<Measurement> measurementList = new LinkedList<Measurement>();
    for (Object item : measurementsList.getListData()) {
      ListItemDataContainer container = (ListItemDataContainer) item;
      measurementList.add((Measurement) container.getAdditionalContent());
    }
    final Constructor constructor = enumValue.getChartClass().getConstructor(List.class);
    visualizationChart = (VisualizationChart) constructor.newInstance(measurementList);
    visualizationChart.getChartComponent().setTitle(chartTitleTextInput.getText());

    final GridPane.Row row = new GridPane.Row();
    row.add(visualizationChart.getChartComponent());
    chartWrapper.getRows().remove(0, 1);
    chartWrapper.getRows().add(row);
  }


  private ComponentListener.Adapter createResizingComponentListener() {
    return new ComponentListener.Adapter() {
      @Override
      public void sizeChanged(Component component, int previousWidth, int previousHeight) {
        mainRow.setHeight(component.getHeight());
      }
    };
  }

  private ComponentMouseListener.Adapter createOptionShowMouseListener() {
    return new ComponentMouseListener.Adapter() {
      @Override
      public void mouseOver(Component component) {
        try {
          optionsColumn.setWidth(OPTIONS_COL_EXPANDED_WIDTH);
          optionsForm.setVisible(true);
        } catch (IllegalArgumentException e) {
          // blah blah blah - width is negative
        }
      }
    };
  }

  private ComponentMouseListener.Adapter createOptionHideMouseListener() {
    return new ComponentMouseListener.Adapter() {
      @Override
      public void mouseOver(Component component) {
        optionsForm.setVisible(false);
        optionsColumn.setWidth(1);
      }
    };
  }

  public void setMeasurementService(MeasurementService measurementService) {
    this.measurementService = measurementService;
  }

  public void setUiFactory(UiFactory uiFactory) {
    this.uiFactory = uiFactory;
  }
}
