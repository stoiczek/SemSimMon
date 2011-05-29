package pl.edu.agh.semsimmon.gui.charts.time;

import pl.edu.agh.semsimmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemmonChartViewSkin;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;
import pl.edu.agh.semsimmon.gui.util.UriUtils;

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
