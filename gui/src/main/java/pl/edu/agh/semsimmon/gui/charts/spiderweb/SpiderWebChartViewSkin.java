package pl.edu.agh.semsimmon.gui.charts.spiderweb;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.RectangleEdge;
import pl.edu.agh.semsimmon.gui.charts.CategoryDatasetChartViewSkin;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:32 19-09-2010
 */
public class SpiderWebChartViewSkin extends CategoryDatasetChartViewSkin {

  SpiderWebPlot spiderWebPlot;

  Set<String> capabilities = new HashSet<String>();

  public SpiderWebChartViewSkin() {
    spiderWebPlot = new SpiderWebPlot(dataSet);
    spiderWebPlot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
    chart = new JFreeChart("", TextTitle.DEFAULT_FONT, spiderWebPlot, true);
    LegendTitle legendtitle = new LegendTitle(spiderWebPlot);
    legendtitle.setPosition(RectangleEdge.BOTTOM);
  }

  @Override
  public void addMeasurement(Measurement measurement) {
    super.addMeasurement(measurement);
    capabilities.add(measurement.getCapabilityUri());
  }

  @Override
  protected String getRowKey(Measurement measurement) {
    return super.getColumnKey(measurement);
  }

  @Override
  protected String getColumnKey(Measurement measurement) {
    return super.getRowKey(measurement);
  }
}
