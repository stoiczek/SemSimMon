package pl.edu.agh.semsimmon.gui.controllers.tab.visualization.chart;

import org.apache.pivot.charts.ChartView;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semsimmon.gui.controllers.BaseController;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Base class for all chart controllers. Acts as utility container for common functionalities.
 *
 * @author tkozak
 *         Created at 14:46 15-08-2010
 */
public abstract class BaseChartController<T extends BaseSemmonChartView> extends BaseController<T>
    implements VisualizationChart {

  protected T chart;


  @Override
  public ChartView getChartComponent() {
    return chart;
  }

  @Override
  protected Class getBindableClass() {
    return BaseChartController.class;
  }

  @Override
  public void addMeasurement(Measurement measurement) {
    chart.addMeasurement(measurement);
  }

  @Override
  public void removeMeasurement(Measurement measurement) {
    chart.removeMeasurement(measurement);
  }

  @Override
  public void pause() {
    chart.pause();
  }

  @Override
  public void resume() {
    chart.resume();
  }

  @Override
  public void updateChartTitle(String newTitle) {
    chart.updateChartTitle(newTitle);
  }

  @Override
  public void updateChartSubTitle(String newTitle) {
    chart.updateSubChartTitle(newTitle);
  }

  @Override
  public void newCapabilityValues(List<CapabilityValue> values) {
    chart.newCapabilityValues(values);
  }

  @Override
  public boolean requiresSameCapability() {
    return false;
  }

  @Override
  public BufferedImage getChartImage() {
    return chart.getChartImage();
  }


}
