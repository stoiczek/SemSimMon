package pl.edu.agh.semsimmon.gui.charts.spiderweb;

import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartViewSkin;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:32 19-09-2010
 */
public class SpiderWebChartView extends BaseSemSimMonChartView {

  @Override
  protected Class<? extends BaseSemSimMonChartViewSkin> getSkinClass() {
    return SpiderWebChartViewSkin.class;
  }
}
