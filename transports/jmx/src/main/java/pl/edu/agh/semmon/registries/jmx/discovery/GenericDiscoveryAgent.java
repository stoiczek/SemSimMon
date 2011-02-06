package pl.edu.agh.semmon.registries.jmx.discovery;

import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:42 11-07-2010
 */
public class GenericDiscoveryAgent implements DiscoveryAgent {

  @Override
  public List<Resource> discoveryChildren(MBeanServerConnection connection, Resource parent, String type) throws IOException {
    final String resourceName = type.substring(type.lastIndexOf('#') + 1);
    final Resource hardwareResource = new Resource(parent.getUri() + "/" + resourceName, type,
        Collections.<String, Object>emptyMap());
    return Arrays.asList(hardwareResource);
  }
}
