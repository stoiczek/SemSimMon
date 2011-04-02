package pl.edu.agh.semsimmon.gui.controllers.tab.visualization.chart;

import pl.edu.agh.semsimmon.gui.charts.time.TimeSeriesChartView;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.List;

/**
 * Controller that handles line (XY) charts.
 *
 * @author tkozak
 *         Created at 15:03 15-08-2010
 */
@SuppressWarnings({"UnusedDeclaration"})
public class TimeSeriesChartController extends BaseChartController<TimeSeriesChartView> {

  public TimeSeriesChartController() {
    chart = new TimeSeriesChartView();
    chart.setShowLegend(true);
  }

  public TimeSeriesChartController(List<Measurement> measurements) {
    this();
    for(Measurement measurement : measurements)
    chart.addMeasurement(measurement);
  }

  @Override
  public boolean requiresSameCapability() {
    return true;
  }
}
