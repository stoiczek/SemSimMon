package pl.edu.agh.semmon.gui.controllers.tab.visualization.chart;

import org.apache.pivot.charts.ChartView;
import pl.edu.agh.semmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;

import java.awt.image.BufferedImage;

/**
 * Interface for all visualization charts.
 *
 * @author tkozak
 *         Created at 14:42 15-08-2010
 */
public interface VisualizationChart extends CapabilityValueListener{

  /**
   * Returns chart visualization managed by this VisualizationChart instance.
   * @return chart visualization managed by this VisualizationChart instance
   */
  ChartView getChartComponent();

  /**
   * Adds given measurement to chat.
   * @param measurement measurement to add.
   */
  void addMeasurement(Measurement measurement);

  /**
   * Remove given measurement.
   * @param measurement measurement to remove.
   */
  void removeMeasurement(Measurement measurement);

  /**
   * Pauses this visualization chart. Visualization, when paused collects values, but doesn't refresh it's view.
   */
  void pause();

  /**
   * Resumes this visualization chart. This also triggers full repaint of chart to apply all cached data. 
   */
  void resume();

  void updateChartTitle(String newTitle);

  void updateChartSubTitle(String newTitle);

  /**
   * Checks whether this VisualizationChart requires all measurements to measure same capability (to have same units)
   * @return true if yes, false otherwise 
   */
  boolean requiresSameCapability();

  BufferedImage getChartImage();
}
