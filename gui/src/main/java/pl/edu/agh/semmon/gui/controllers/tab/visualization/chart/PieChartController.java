package pl.edu.agh.semmon.gui.controllers.tab.visualization.chart;

import pl.edu.agh.semmon.gui.charts.pie.PieChartView;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;

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
