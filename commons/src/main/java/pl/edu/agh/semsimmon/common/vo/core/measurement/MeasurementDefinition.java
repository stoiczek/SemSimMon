package pl.edu.agh.semsimmon.common.vo.core.measurement;

import pl.edu.agh.semsimmon.common.vo.BaseValueObject;

/**
 * MeasurementDefinition value object - used to create/update measurements in communication between gui and core.
 *
 * @author tkozak
 *         Created at 22:10 14-07-2010
 */
public class MeasurementDefinition extends BaseValueObject {

  /**
   * URI of resource being monitored by this measurement
   */
  private String resourceUri;

  /**
   * URI of capability being monitored.
   */
  private String capabilityUri;

  /**
   * Refresh interval.
   */
  private long updateInterval;

  /**
   * Id of this measurement.
   */
  private String id;

  public String getResourceUri() {
    return resourceUri;
  }

  public void setResourceUri(String resourceUri) {
    this.resourceUri = resourceUri;
  }

  public String getCapabilityUri() {
    return capabilityUri;
  }

  public void setCapabilityUri(String capabilityUri) {
    this.capabilityUri = capabilityUri;
  }

  public long getUpdateInterval() {
    return updateInterval;
  }

  public void setUpdateInterval(long updateInterval) {
    this.updateInterval = updateInterval;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
