package pl.edu.agh.semsimmon.gui.charts.spiderweb;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.RectangleEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.gui.charts.CategoryDatasetChartViewSkin;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.*;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:32 19-09-2010
 */
public class SpiderWebChartViewSkin extends CategoryDatasetChartViewSkin {

  private static final Logger log = LoggerFactory.getLogger(SpiderWebChartViewSkin.class.getName());

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

}
