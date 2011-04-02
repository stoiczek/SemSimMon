package pl.edu.agh.semsimmon.gui.charts.bar;

import pl.edu.agh.semsimmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemmonChartViewSkin;

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
