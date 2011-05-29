package pl.edu.agh.semsimmon.gui.charts;

import org.apache.pivot.charts.ChartView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.gui.logic.metric.Measurement;
import pl.edu.agh.semsimmon.gui.util.UriUtils;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for all semsimmon's chart views.
 *
 * @author tkozak
 *         Created at 12:37 21-08-2010
 */
public abstract class BaseSemSimMonChartView extends ChartView implements CapabilityValueListener {

  private static final Logger log = LoggerFactory.getLogger(BaseSemSimMonChartView.class);

  protected Map<String, Measurement> measurements = new HashMap<String, Measurement>();

  private AtomicBoolean paused = new AtomicBoolean(false);


  protected BaseSemSimMonChartView() {
    Class<? extends BaseSemSimMonChartViewSkin> skinClass = getSkinClass();
    try {
      setSkin(skinClass.newInstance());
    } catch (InstantiationException e) {
      log.error("Invalid configuration of chart view. Tried to create skin from class: " + skinClass.getName(), e);
    } catch (IllegalAccessException e) {
      log.error("Invalid configuration of chart view. Tried to create skin from class: " + skinClass.getName(), e);
    }
  }

  protected BaseSemSimMonChartView(List<Measurement> measurementsList) throws InstantiationException, IllegalAccessException {
    this();
    for (Measurement meas : measurementsList) {
      measurements.put(meas.getId(), meas);
      String capName = meas.getCapabilityUri();
      setVerticalAxisLabel(UriUtils.getCapabilityLabel(capName));

    }
  }

  public void addMeasurement(Measurement measurement) {
    String capName = measurement.getCapabilityUri();
    setVerticalAxisLabel(UriUtils.getCapabilityLabel(capName));
    measurements.put(measurement.getId(), measurement);
    measurement.addCapabilityValueListener(this);
    BaseSemSimMonChartViewSkin skin = (BaseSemSimMonChartViewSkin) getSkin();
    skin.addMeasurement(measurement);
  }

  public void removeMeasurement(Measurement measurement) {
    measurement.removeCapabilityValueListener(this);
    measurements.remove(measurement.getId());
    ((BaseSemSimMonChartViewSkin) getSkin()).removeMeasurement(measurement);
  }


  public void clear() {
    super.clear();
    for (Measurement measurement : measurements.values()) {
      measurement.removeCapabilityValueListener(this);
    }
    measurements.clear();
  }


  @Override
  public void newCapabilityValues(List<CapabilityValue> values) {
    if (paused.get()) {
      return;
    }
    BaseSemSimMonChartViewSkin skin = (BaseSemSimMonChartViewSkin) getSkin();
    skin.newCapabilityValues(values);
    skin.repaintComponent();
  }

  public Collection<Measurement> getMeasurements() {
    return measurements.values();
  }

  public void pause() {
    paused.set(true);
  }

  public void resume() {
    BaseSemSimMonChartViewSkin skin = (BaseSemSimMonChartViewSkin) getSkin();
    skin.redrawChart();
    skin.repaintComponent();
    paused.set(false);
  }

  public void updateChartTitle(String newTitle) {
    BaseSemSimMonChartViewSkin skin = (BaseSemSimMonChartViewSkin) getSkin();
    skin.updateChartTitle(newTitle);
  }

  public void updateSubChartTitle(String newTitle) {
    BaseSemSimMonChartViewSkin skin = (BaseSemSimMonChartViewSkin) getSkin();
    skin.updateSubChartTitle(newTitle);
  }

  public BufferedImage getChartImage() {
    return ((BaseSemSimMonChartViewSkin) getSkin()).getChartImage();
  }


  protected abstract Class<? extends BaseSemSimMonChartViewSkin> getSkinClass();


}
