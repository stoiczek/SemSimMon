package pl.edu.agh.semsimmon.gui.charts.bar;

import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartViewSkin;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 16:09 21-08-2010
 */
public class BarChartView extends BaseSemSimMonChartView {

  @Override
  protected Class<? extends BaseSemSimMonChartViewSkin> getSkinClass() {
    return BarChartViewSkin.class;
  }

  
}
