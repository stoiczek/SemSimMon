package pl.edu.agh.semsimmon.gui.charts;

import org.apache.pivot.charts.ChartView;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.gui.charts.bar.BarChartView;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;
import pl.edu.agh.semsimmon.gui.util.UriUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:35 19-09-2010
 */
public class CategoryDatasetChartViewSkin extends BaseSemmonChartViewSkin {

  private static final Logger log = LoggerFactory.getLogger(CategoryDatasetChartViewSkin.class);

  protected DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
  private Map<String, Measurement> measurements = new HashMap<String, Measurement>();

  public CategoryDatasetChartViewSkin() {
    dataSet = new DefaultCategoryDataset();
  }

  @Override
  public void redrawChart() {
    dataSet.clear();
    populateData((BarChartView) getComponent());
  }

  @Override
  public void addMeasurement(Measurement measurement) {
    final List<CapabilityValue> capabilityValueList = measurement.getValues();
    measurements.put(measurement.getId(), measurement);
    if(!capabilityValueList.isEmpty()) {
    dataSet.addValue(capabilityValueList.get(capabilityValueList.size() - 1).getNumericValue(),
        getRowKey(measurement),
        getColumnKey(measurement));
    }
  }

  protected String getColumnKey(Measurement measurement) {
    final String resource = measurement.getResourceUri();
    return resource.substring(resource.lastIndexOf('/') + 1);
  }

  protected String getRowKey(Measurement measurement) {
    return UriUtils.getCapabilityLabel(measurement.getCapabilityUri());
  }

  @Override
  public void removeMeasurement(Measurement measurement) {
    dataSet.removeValue(getRowKey(measurement), getColumnKey(measurement));
  }

  @Override
  public void newCapabilityValues(List<CapabilityValue> values) {
    log.debug("Got new capabilities to update visualization");
    for (CapabilityValue value : values) {
      Measurement measurement = measurements.get(value.getMetricsId());
      dataSet.addValue(value.getNumericValue(), getRowKey(measurement), getColumnKey(measurement));
    }
  }

  @Override
  public ChartView.Element getElementAt(int x, int y) {
    ChartView.Element element = null;

    ChartEntity chartEntity = getChartEntityAt(x, y);
    if (chartEntity instanceof CategoryItemEntity) {
      CategoryItemEntity categoryItemEntity = (CategoryItemEntity) chartEntity;
      CategoryDataset dataset = categoryItemEntity.getDataset();

      String columnKey = (String) categoryItemEntity.getColumnKey();
      int columnIndex = dataset.getColumnIndex(columnKey);

      String rowKey = (String) categoryItemEntity.getRowKey();
      int rowIndex = dataset.getRowIndex(rowKey);

      element = new ChartView.Element(rowIndex, columnIndex);
    } else if (chartEntity instanceof XYItemEntity) {
      XYItemEntity xyItemEntity = (XYItemEntity) chartEntity;
      element = new ChartView.Element(xyItemEntity.getSeriesIndex(),
          xyItemEntity.getItem());
    }

    return element;
  }
}
