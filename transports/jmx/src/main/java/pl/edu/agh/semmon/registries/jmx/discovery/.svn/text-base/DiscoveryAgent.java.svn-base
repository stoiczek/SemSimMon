package pl.edu.agh.semmon.registries.jmx.discovery;

import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.util.List;

/**
 * Interface enabling more sophisticated resource of JMX-based child resources.
 *
 * @author tkozak
 *         Created at 11:40 11-07-2010
 */
public interface DiscoveryAgent {

  List<Resource> discoveryChildren(MBeanServerConnection connection, Resource parent, String type) throws IOException;

}
