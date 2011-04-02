package pl.edu.agh.semsimmon.gui.controllers.tab.visualization.chart;

import pl.edu.agh.semsimmon.gui.charts.pie.PieChartView;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:30 19-09-2010
 */
public class PieChartController extends BaseChartController<PieChartView> {

  public PieChartController() {
    chart = new PieChartView();
  }

  public PieChartController(List<Measurement> measurements) {
    this();
    for (Measurement measurement : measurements)
      chart.addMeasurement(measurement);
  }
}
