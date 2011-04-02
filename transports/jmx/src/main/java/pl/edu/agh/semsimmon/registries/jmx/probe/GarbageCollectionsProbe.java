package pl.edu.agh.semsimmon.registries.jmx.probe;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.probe.CapabilityProbe;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:12 25-07-2010
 */
public class GarbageCollectionsProbe implements CapabilityProbe {

  private static final String NAME_KEY = ",name=";

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri, MBeanServerConnection connection)
      throws IOException {
    if (!resource.getTypeUri().equals(KnowledgeConstants.GC_URI)) {
      throw new IllegalArgumentException("This probe supports only GarbageCollector resources");
    }
    if (!resource.getProperties().containsKey(ResourcePropertyNames.GarbageCollector.NAME)) {
      throw new IllegalArgumentException("Given resource is not properly discovered GC - missing name property");
    }
    final String gcName = resource.getProperty(ResourcePropertyNames.GarbageCollector.NAME).toString();
    final GarbageCollectorMXBean gcBean = ManagementFactory.newPlatformMXBeanProxy
        (connection, ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + NAME_KEY + gcName,
            GarbageCollectorMXBean.class);
    long value = -1;

    if (capabilityUri.equals(KnowledgeConstants.GC_COUNT_CAP)) {
      value = gcBean.getCollectionCount();
    } else if (capabilityUri.equals(KnowledgeConstants.GC_TIME_CAP)) {
      value = gcBean.getCollectionTime();
    } else {
      throw new IllegalArgumentException("Got invalid capability URI: " + capabilityUri);
    }
    return new CapabilityValue(value);
  }
}
