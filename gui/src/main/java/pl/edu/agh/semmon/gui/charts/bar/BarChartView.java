package pl.edu.agh.semmon.gui.charts.bar;

import pl.edu.agh.semmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semmon.gui.charts.BaseSemmonChartViewSkin;
import pl.edu.agh.semmon.gui.logic.metric.Measurement;

import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 16:09 21-08-2010
 */
public class BarChartView extends BaseSemmonChartView {

  @Override
  protected Class<? extends BaseSemmonChartViewSkin> getSkinClass() {
    return BarChartViewSkin.class;
  }

  
}
