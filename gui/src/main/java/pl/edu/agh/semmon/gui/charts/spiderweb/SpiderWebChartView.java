package pl.edu.agh.semmon.gui.charts.spiderweb;

import pl.edu.agh.semmon.gui.charts.BaseSemmonChartView;
import pl.edu.agh.semmon.gui.charts.BaseSemmonChartViewSkin;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:32 19-09-2010
 */
public class SpiderWebChartView extends BaseSemmonChartView {

  @Override
  protected Class<? extends BaseSemmonChartViewSkin> getSkinClass() {
    return SpiderWebChartViewSkin.class;
  }
}
