package pl.edu.agh.semsimmon.registries.jmx.discovery.node;

import com.sun.management.OperatingSystemMXBean;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.DiscoveryAgent;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Discovers node's operating system.
 *
 * @author tkozak
 *         Created at 11:57 11-07-2010
 */
public class OsDiscoveryAgent implements DiscoveryAgent {

  @Override
  public List<Resource> discoveryChildren(MBeanServerConnection connection, Resource parent, String type) throws IOException {
    if (!parent.getTypeUri().equals(KnowledgeConstants.NODE_URI)) {
      throw new IllegalArgumentException("Got invalid parent resource type: " + parent.getTypeUri());
    }
    OperatingSystemMXBean osProxy = ManagementFactory.newPlatformMXBeanProxy
        (connection, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(ResourcePropertyNames.Os.NAME, osProxy.getName());
    properties.put(ResourcePropertyNames.Os.ARCHITECTURE, osProxy.getArch());
    properties.put(ResourcePropertyNames.Os.VERSION, osProxy.getVersion());
    final Resource osResource = new Resource(parent.getUri() + "/OperatingSystem", KnowledgeConstants.OS_URI, properties);
    return Arrays.asList(osResource);
  }
}
