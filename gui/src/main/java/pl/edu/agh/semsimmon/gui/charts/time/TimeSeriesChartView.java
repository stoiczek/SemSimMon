package pl.edu.agh.semsimmon.gui.charts.time;

import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartViewSkin;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 12:26 21-08-2010
 */
public class TimeSeriesChartView extends BaseSemSimMonChartView {

  public TimeSeriesChartView() {
    setHorizontalAxisLabel("Time");
  }

  public TimeSeriesChartView(List<Measurement> measurementsList) throws InstantiationException, IllegalAccessException {
    super(measurementsList);
    setHorizontalAxisLabel("Time");
  }

  @Override
  protected Class<? extends BaseSemSimMonChartViewSkin> getSkinClass() {
    return TimeSeriesChartViewSkin.class;
  }
}
