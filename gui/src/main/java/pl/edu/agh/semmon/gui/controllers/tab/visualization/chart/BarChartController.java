package pl.edu.agh.semmon.gui.controllers.tab.visualization.chart;

import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.gui.charts.bar.BarChartView;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;

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
