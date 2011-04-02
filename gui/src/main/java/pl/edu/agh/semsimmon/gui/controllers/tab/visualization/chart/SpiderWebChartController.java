package pl.edu.agh.semsimmon.gui.controllers.tab.visualization.chart;

import pl.edu.agh.semsimmon.gui.charts.spiderweb.SpiderWebChartView;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:30 19-09-2010
 */
public class SpiderWebChartController extends BaseChartController{
    public SpiderWebChartController() {
    chart = new SpiderWebChartView();
  }

  public SpiderWebChartController(List<Measurement> measurements) {
    this();
    for(Measurement measurement : measurements)
    chart.addMeasurement(measurement);
  }

}
