package pl.edu.agh.semmon.registries.ocmg.probe;

import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.OcmgException;

/**
 * @author tkozak
 *         Created at 53:12 22-05-2010
 *         Interface providing facility to implement capablility reading probes;
 */
public interface CapabilityProbe {

  /**
   * Acquires and returns capability value of given resource.
   *
   * @param resource    resource to get capability value of.
   * @param application application that can be used to extract given capability value
   * @return resource's capability value
   */
  CapabilityValue getCapabilityValue(Resource resource, Application application) throws OcmgException;


}
