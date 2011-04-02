package pl.edu.agh.semsimmon.common.vo.core.measurement;

import java.io.Serializable;
import java.util.Date;

/**
 * Wrapper for values of capability. It may contain different value formats (floating points, integers, arrays),
 * with timestamp
 * Created at 04:13 22-05-2010
 * @author tkozak
 */
public class CapabilityValue implements Serializable {


  public enum ValueType {
    NUMERIC, ARRAY
  }

  private Number numberValue;

  private Number[] arrayValue;

  private ValueType valueType;

  private Date gatherTimestamp;

  private String metricsId;

  /**
   * Constructs CapabilityValue using given numeric value. Sets value and appropriate value type.
   *
   * @param numberValue value used to construct this CapabilityValue
   */
  public CapabilityValue(Number numberValue) {
    this.numberValue = numberValue;
    gatherTimestamp = new Date();
    valueType = ValueType.NUMERIC;
  }

  public CapabilityValue(Number[] arrayValue) {
    this.arrayValue = arrayValue;
    gatherTimestamp = new Date();
    valueType = ValueType.ARRAY;
  }

  public CapabilityValue(Number[] arrayValue, Date gatherTimestamp) {
    this.arrayValue = arrayValue;
    this.gatherTimestamp = gatherTimestamp;
    valueType = ValueType.ARRAY;
  }


  public CapabilityValue(Number numberValue, Date gatherTimestamp) {
    this.numberValue = numberValue;
    this.gatherTimestamp = gatherTimestamp;
  }

  /**
   * Returns numeric value of this CapabilityValue.
   *
   * @return numeric value of this CapabilityValue
   */
  public Number getNumericValue() {
    return numberValue;
  }


  /**
   * Returns type of this CapabilityValue.
   *
   * @return type of this CapabilityValue
   */
  public ValueType getValueType() {
    return valueType;
  }

  public Number[] getArrayValue() {
    return arrayValue;
  }

  public Date getGatherTimestamp() {
    return gatherTimestamp;
  }

  public String getMetricsId() {
    return metricsId;
  }

  public void setMetricsId(String metricsId) {
    this.metricsId = metricsId;
  }
}
