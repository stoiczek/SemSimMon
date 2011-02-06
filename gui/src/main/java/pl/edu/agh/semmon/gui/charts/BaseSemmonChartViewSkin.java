package pl.edu.agh.semmon.gui.charts;

import biz.ixnay.pivot.charts.skin.jfree.JFreeChartViewSkin;
import org.jfree.chart.JFreeChart;
import pl.edu.agh.semmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 12:42 21-08-2010
 */
public abstract class BaseSemmonChartViewSkin extends JFreeChartViewSkin implements CapabilityValueListener {

  protected JFreeChart chart;

  public void updateChartTitle(String newTitle) {
    chart.setTitle(newTitle);
  }

  public void updateSubChartTitle(String newTitle) {
    List<String> subtitles;
    if (newTitle == null || newTitle.isEmpty()) {
      subtitles = Arrays.asList(newTitle);
    } else {
      subtitles = Collections.emptyList();
    }
    chart.setSubtitles(subtitles);
  }

  @Override
  protected JFreeChart createChart() {
    BaseSemmonChartView chartView = (BaseSemmonChartView) getComponent();
    String title = chartView.getTitle();
    chart.setTitle(title);
//    chart.getC getXYPlot().getDomainAxis().setLabel(horizontalAxisLabel);
//    chart.getXYPlot().getRangeAxis().setLabel(verticalAxisLabel);
//    populateData(chartView);
    return chart;
  }

  protected void populateData(BaseSemmonChartView chartView) {
    Collection<Measurement> measurements = chartView.getMeasurements();
    for (Measurement measurement : measurements) {
      addMeasurement(measurement);
    }
  }
  
  public abstract void redrawChart();

  public abstract void addMeasurement(Measurement measurement);

  public abstract void removeMeasurement(Measurement measurement);
}
