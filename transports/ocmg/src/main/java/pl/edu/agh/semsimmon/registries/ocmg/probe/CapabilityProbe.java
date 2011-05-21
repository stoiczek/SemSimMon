package pl.edu.agh.semsimmon.registries.ocmg.probe;

import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

/**
 * @author tkozak
 *         Created at 53:12 22-05-2010
 *         Interface providing facility to implement capablility reading probes;
 */
public interface CapabilityProbe {

  /**
   * Acquires and returns capability value of given resource.
   *
   *
   * @param resource    resource to get capability value of.
   * @param application application that can be used to extract given capability value
   * @param capabilityType
   * @return resource's capability value
   */
  CapabilityValue getCapabilityValue(Resource resource, Application application, String capabilityType) throws OcmgException;


}
