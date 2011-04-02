package pl.edu.agh.semsimmon.registries.jmx.probe;

import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import javax.management.MBeanServerConnection;
import java.io.IOException;


/**
 * @author tkozak
 *         Created at 53:12 22-05-2010
 *         Interface providing facility to implement capablility reading probes;
 */
public interface CapabilityProbe {

  /**
   * Acquires and returns capability value of given resource.
   *
   * @param resource      resource to get capability value of.
   * @param capabilityUri uri of capability to get value of.
   * @param connection    connection that can be used to extract given capability value
   * @return resource's capability value
   */
  CapabilityValue getCapabilityValue(Resource resource, String capabilityUri, MBeanServerConnection connection)
      throws IOException;


}