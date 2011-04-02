package pl.edu.agh.semsimmon.gui.charts.pie;

import pl.edu.agh.semsimmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemmonChartViewSkin;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:07 14-09-2010
 */
public class PieChartView extends BaseSemmonChartView {

  @Override
  protected Class<? extends BaseSemmonChartViewSkin> getSkinClass() {
    return PieChartViewSkin.class;
  }
}
