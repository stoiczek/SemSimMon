package pl.edu.agh.semmon.gui.charts.time;

import pl.edu.agh.semmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semmon.gui.charts.BaseSemmonChartViewSkin;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;

import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 12:26 21-08-2010
 */
public class TimeSeriesChartView extends BaseSemmonChartView {

  public TimeSeriesChartView() {
    setHorizontalAxisLabel("Time");
  }

  public TimeSeriesChartView(List<Measurement> measurementsList) throws InstantiationException, IllegalAccessException {
    super(measurementsList);
    setHorizontalAxisLabel("Time");
  }

  @Override
  protected Class<? extends BaseSemmonChartViewSkin> getSkinClass() {
    return TimeSeriesChartViewSkin.class;
  }
}
