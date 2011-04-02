package pl.edu.agh.semsimmon.gui.charts;

import biz.ixnay.pivot.charts.skin.jfree.JFreeChartViewSkin;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.awt.image.BufferedImage;
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
    return chart;
  }

  protected void populateData(BaseSemmonChartView chartView) {
    Collection<Measurement> measurements = chartView.getMeasurements();
    for (Measurement measurement : measurements) {
      addMeasurement(measurement);
    }
  }

  public BufferedImage getChartImage() {
    int width = 800;
    int height = 600;
    return chart.createBufferedImage(width, height);
  }


  public abstract void redrawChart();

  public abstract void addMeasurement(Measurement measurement);

  public abstract void removeMeasurement(Measurement measurement);
}
