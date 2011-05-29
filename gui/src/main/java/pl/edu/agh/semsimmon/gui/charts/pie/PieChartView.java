package pl.edu.agh.semsimmon.gui.charts.pie;

import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartViewSkin;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:07 14-09-2010
 */
public class PieChartView extends BaseSemSimMonChartView {

  @Override
  protected Class<? extends BaseSemSimMonChartViewSkin> getSkinClass() {
    return PieChartViewSkin.class;
  }
}
