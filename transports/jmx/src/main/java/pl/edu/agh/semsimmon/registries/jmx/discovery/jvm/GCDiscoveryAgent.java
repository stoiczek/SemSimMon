package pl.edu.agh.semsimmon.registries.jmx.discovery.jvm;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.DiscoveryAgent;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * Discovery agent, that can list JVM's garbage collectors
 *
 * @author tkozak
 *         Created at 19:18 19-07-2010
 */
public class GCDiscoveryAgent implements DiscoveryAgent {

  @Override
  public List<Resource> discoveryChildren(MBeanServerConnection connection, Resource parent, String type) throws IOException {
    List<Resource> resources = new LinkedList<Resource>();
    try {
      Set<ObjectName> gcs = connection.queryNames(
          ObjectName.getInstance(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",*"), null);
      for (ObjectName gcObjectName : gcs) {
        String gcName = gcObjectName.getKeyProperty("name");
        final Map<String,Object> properties = new HashMap<String, Object>();
        properties.put(ResourcePropertyNames.GarbageCollector.NAME, gcName);
        properties.put(ResourcePropertyNames.GarbageCollector.GC_NAME, gcName);
        resources.add(new Resource(parent.getUri() + "/" + gcName, KnowledgeConstants.GC_URI, properties));
      }
    } catch (MalformedObjectNameException e) {
      throw new IOException(e);
    }
    return resources;

  }

}
