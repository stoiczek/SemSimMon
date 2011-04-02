package pl.edu.agh.semsimmon.gui.controllers.tab.visualization.chart;

/**
 * Enum containing all chart types supported by Semmon's gui.
 *
 * @author tkozak
 *         Created at 13:37 15-08-2010
 */
public enum ChartType {


  PIE_CHART(PieChartController.class),
  BAR_CHART(BarChartController.class),
  LINE_CHART(TimeSeriesChartController.class),
  SPIDER_WEB_CHART(SpiderWebChartController.class);

  private Class chartClass;

  ChartType(Class chartClass) {
    this.chartClass = chartClass;
  }

  public Class getChartClass() {
    return chartClass;
  }

  public static ChartType getDefaultChartType() {
    return LINE_CHART; 
  }
}
