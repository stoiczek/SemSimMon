package pl.edu.agh.semsimmon.core.measurement;

import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 12:23 14-08-2010
 */
public interface MeasurementListener {

  void newCapabilityValue(CapabilityValue value);

}
