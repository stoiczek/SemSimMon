package pl.edu.agh.semsimmon.gui.controllers.tab.visualization.chart;

import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.gui.charts.bar.BarChartView;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 16:59 21-08-2010
 */
public class BarChartController extends BaseChartController<BarChartView>{

  public BarChartController() {
    chart = new BarChartView();
  }

  public BarChartController(List<Measurement> measurements) {
    this();
    for(Measurement measurement : measurements)
    chart.addMeasurement(measurement);
  }
}
