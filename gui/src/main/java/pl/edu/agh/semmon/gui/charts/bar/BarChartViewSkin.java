package pl.edu.agh.semmon.gui.charts.bar;

import org.apache.pivot.charts.ChartView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semmon.gui.charts.BaseSemmonChartViewSkin;
import pl.edu.agh.semmon.gui.charts.CategoryDatasetChartViewSkin;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;
import pl.edu.agh.semmon.gui.util.UriUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 16:09 21-08-2010
 */
public class BarChartViewSkin extends CategoryDatasetChartViewSkin {

  private static final Logger log = LoggerFactory.getLogger(BarChartViewSkin.class);

  public BarChartViewSkin() {
    super();
    // key is a series and thus resource
        // row is column and thus capability
        chart = ChartFactory.createBarChart("", "", "",
            dataSet, PlotOrientation.VERTICAL, true, false, false);
  }

}
