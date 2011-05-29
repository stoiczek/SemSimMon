package pl.edu.agh.semsimmon.gui.charts.pie;

import org.apache.pivot.charts.ChartView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.data.general.DefaultPieDataset;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartView;
import pl.edu.agh.semsimmon.gui.charts.BaseSemSimMonChartViewSkin;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Skin of pie chart view.
 *
 * @author tkozak
 *         Created at 20:08 14-09-2010
 */
public class PieChartViewSkin extends BaseSemSimMonChartViewSkin {

  DefaultPieDataset set = new DefaultPieDataset();

  Map<String, Measurement> measurementMap = new HashMap<String, Measurement>();

  public PieChartViewSkin() {
    chart = ChartFactory.createPieChart("", set, true, true, false);
  }

  @Override
  public void redrawChart() {
    set.clear();
    populateData((BaseSemSimMonChartView) getComponent());
  }

  @Override
  public void addMeasurement(Measurement measurement) {
    measurementMap.put(measurement.getId(), measurement);
    final List<CapabilityValue> capabilityValues = measurement.getValues();
    if (!capabilityValues.isEmpty()) {
      final Number value = capabilityValues.get(capabilityValues.size() - 1).getNumericValue();
      set.setValue(measurement.getLabel(), value);
    }
  }

  @Override
  public void removeMeasurement(Measurement measurement) {
    set.remove(measurement.getLabel());
    measurementMap.remove(measurement.getId());
  }

  @Override
  public void newCapabilityValues(List<CapabilityValue> values) {
    for(CapabilityValue value : values) {
      Measurement measurement = measurementMap.get(value.getMetricsId());
      set.setValue(measurement.getLabel(), value.getNumericValue());
    }
  }

  @Override
  public ChartView.Element getElementAt(int x, int y) {
    ChartView.Element element = null;
    ChartEntity chartEntity = getChartEntityAt(x, y);
    if (chartEntity instanceof PieSectionEntity) {
      PieSectionEntity pieSectionEntity = (PieSectionEntity) chartEntity;
      int sectionIndex = pieSectionEntity.getSectionIndex();
      int seriesIndex = pieSectionEntity.getPieIndex();
      element = new ChartView.Element(seriesIndex, sectionIndex);
    }
    return element;
  }
}
