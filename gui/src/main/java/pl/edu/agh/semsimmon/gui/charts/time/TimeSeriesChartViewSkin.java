package pl.edu.agh.semsimmon.gui.charts.time;

import org.apache.pivot.charts.ChartView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartViewSkin;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Skin for time series visualization chart.
 *
 * @author tkozak
 *         Created at 12:26 21-08-2010
 */
public class TimeSeriesChartViewSkin extends BaseSemSimMonChartViewSkin {

  private static final Logger log = LoggerFactory.getLogger(TimeSeriesChartViewSkin.class);

  private Map<String, Measurement> measurements = new HashMap<String, Measurement>();

  TimeSeriesCollection data;

  public TimeSeriesChartViewSkin() {
    data = new TimeSeriesCollection();
    chart = ChartFactory.createTimeSeriesChart("", "", "", data, true, true, false);
    chart.setAntiAlias(true);
    XYPlot plot = (XYPlot) chart.getPlot();
    plot.setDomainGridlinesVisible(true);
    plot.setRangeGridlinesVisible(true);
    plot.setBackgroundPaint(new Color(123, 43, 65));
    chart.plotChanged(new PlotChangeEvent(plot));
//    chart.setBackgroundPaint();
  }


  @Override
  public void newCapabilityValues(java.util.List<CapabilityValue> values) {
    log.debug("Got new capabilities to update visualization");
    for (CapabilityValue value : values) {
      Measurement measurement = measurements.get(value.getMetricsId());
      if (data.getSeries(measurement.getLabel()) == null) {
        data.addSeries(new TimeSeries(measurement.getLabel()));
      }
      data.getSeries(measurement.getLabel()).add(new Second(value.getGatherTimestamp()), value.getNumericValue());
      log.debug("Amount of series: {}", data.getSeries().size());
    }
  }

  @Override
  public ChartView.Element getElementAt(int x, int y) {
    ChartView.Element element = new ChartView.Element(0, 0);
    ChartEntity chartEntity = getChartEntityAt(x, y);
    if (chartEntity instanceof CategoryItemEntity) {
      CategoryItemEntity categoryItemEntity = (CategoryItemEntity) chartEntity;
      CategoryDataset dataset = categoryItemEntity.getDataset();
      String columnKey = (String) categoryItemEntity.getColumnKey();
      int columnIndex = dataset.getColumnIndex(columnKey);
      String rowKey = (String) categoryItemEntity.getRowKey();
      int rowIndex = dataset.getRowIndex(rowKey);
      element = new ChartView.Element(rowIndex, columnIndex);
    } else if (chartEntity instanceof XYItemEntity) {
      XYItemEntity xyItemEntity = (XYItemEntity) chartEntity;
      element = new ChartView.Element(xyItemEntity.getSeriesIndex(),
          xyItemEntity.getItem());
    }
    return element;
  }

  @Override
  public void redrawChart() {
    while (this.data.getSeriesCount() > 0) {
      this.data.removeSeries(0);
    }
    populateData((TimeSeriesChartView) getComponent());
  }

  @Override
  protected void populateData(BaseSemSimMonChartView chartView) {
    data.removeAllSeries();
    super.populateData(chartView);
  }

  @Override
  public void addMeasurement(Measurement measurement) {
    log.debug("Adding measurement");
    final TimeSeries timeSeries = new TimeSeries(measurement.getLabel());
    timeSeries.setKey(measurement.getLabel());
    for (CapabilityValue value : measurement.getValues()) {
      timeSeries.addOrUpdate(new Second(value.getGatherTimestamp()), value.getNumericValue());
    }
    data.addSeries(timeSeries);
    measurements.put(measurement.getId(), measurement);
  }

  @Override
  public void removeMeasurement(Measurement measurement) {
    data.removeSeries(data.getSeries(measurement.getLabel()));
  }

  @Override
  protected JFreeChart createChart() {
    BaseSemSimMonChartView chartView = (BaseSemSimMonChartView) getComponent();
    chart.getXYPlot().getDomainAxis().setLabel(chartView.getHorizontalAxisLabel());
    chart.getXYPlot().getRangeAxis().setLabel(chartView.getVerticalAxisLabel());
    return chart;
  }
}
