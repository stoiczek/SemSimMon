package pl.edu.agh.semsimmon.gui.logic.metric;

import pl.edu.agh.semsimmon.common.api.measurement.CapabilityValueListener;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.measurement.MeasurementDefinition;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Measurement details container. Has measurement definition, and result listeners.
 *
 * @author tkozak
 *         Created at 13:38 07-08-2010
 */
public class Measurement implements CapabilityValueListener {

  private MeasurementDefinition measurementDefinition = new MeasurementDefinition();

  private String label;

  private Resource resource;

  private Date creationDate = new Date();

  private boolean active = true;

  List<CapabilityValue> values = new LinkedList<CapabilityValue>();

  private List<CapabilityValueListener> listeners = new LinkedList<CapabilityValueListener>();

  public String getResourceUri() {
    return measurementDefinition.getResourceUri();
  }

  public void setResourceUri(String resourceUri) {
    measurementDefinition.setResourceUri(resourceUri);
  }

  public String getCapabilityUri() {
    return measurementDefinition.getCapabilityUri();
  }

  public void setCapabilityUri(String capabilityUri) {
    measurementDefinition.setCapabilityUri(capabilityUri);
  }

  public long getUpdateInterval() {
    return measurementDefinition.getUpdateInterval();
  }

  public void setUpdateInterval(long updateInterval) {
    measurementDefinition.setUpdateInterval(updateInterval);
  }

  public String getId() {
    return measurementDefinition.getId();
  }

  public void setId(String id) {
    measurementDefinition.setId(id);
  }

  public MeasurementDefinition getMeasurementDefinition() {
    return measurementDefinition;
  }

  public void setMeasurementDefinition(MeasurementDefinition measurementDefinition) {
    if(measurementDefinition == null) {
      this.measurementDefinition = new MeasurementDefinition();
    } else {
      this.measurementDefinition = measurementDefinition;
    }
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public synchronized List<CapabilityValue> getValues() {
    return values;
  }

  @Override
  public synchronized void newCapabilityValues(List<CapabilityValue> values) {
    this.values.addAll(values);
    for(CapabilityValueListener listener : listeners) {
      listener.newCapabilityValues(values);
    }
  }

  public void addCapabilityValueListener(CapabilityValueListener listener) {
    listeners.add(listener);
  }

  public void removeCapabilityValueListener(CapabilityValueListener listener) {
    listeners.remove(listener);
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
