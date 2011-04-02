package pl.edu.agh.semsimmon.registries.ocmg.probe;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.meters.LibCallMeter;
import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.LibCallMetersContainer;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

/**
 * @author tkozak
 *         Created at 57:10 28-05-2010
 * @TODO description
 */
public class FunctionProbe implements CapabilityProbe {

  public enum ProbeType {
    TIME, COUNT
  }

  private LibCallMetersContainer libCallMetersContainer;

  private ProbeType type;

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application) throws OcmgException {
    LibCallMeter libCallMeter = libCallMetersContainer.getMeter(resource.getUri());
    if(libCallMeter == null) {
      return new CapabilityValue(Double.NaN);
    }
    try {
      double value = 0.0;
      switch (type) {
        case COUNT:
          value = libCallMeter.getCount().getValue();
          break;
        case TIME:
          value = libCallMeter.getTime().getTime();
          break;
      }
      return new CapabilityValue(value);
    } catch (ConnectionException e) {
      throw new OcmgException("Error readging value", e);
    } catch (MonitorException e) {
      throw new OcmgException("Error readging value", e);
    }
  }

  public void setLibCallMetersContainer(LibCallMetersContainer libCallMetersContainer) {
    this.libCallMetersContainer = libCallMetersContainer;
  }

  public void setType(ProbeType type) {
    this.type = type;
  }
}
