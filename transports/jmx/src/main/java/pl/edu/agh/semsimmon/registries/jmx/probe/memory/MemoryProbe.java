package pl.edu.agh.semsimmon.registries.jmx.probe.memory;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.probe.CapabilityProbe;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 18:22 25-07-2010
 */
public class MemoryProbe implements CapabilityProbe {

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri, MBeanServerConnection connection) throws IOException {
    final String resourceType = resource.getTypeUri();
    try {
    if (!(resourceType.equals(KnowledgeConstants.PHYSICAL_MEMORY_URI) ||
        resourceType.equals(KnowledgeConstants.VIRTUAL_MEMORY_URI))) {
      throw new IllegalArgumentException("Got invalid resource type: " + resourceType);
    }
      long totalValue = -1;
      long freeValue = -1;
      final ObjectName objectName = ObjectName.getInstance(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
      if (resource.getTypeUri().equals(KnowledgeConstants.PHYSICAL_MEMORY_URI)) {
        totalValue =(Long) connection.getAttribute(objectName, "TotalPhysicalMemorySize");
        freeValue = (Long) connection.getAttribute(objectName, "FreePhysicalMemorySize");
      } else {
        totalValue =(Long) connection.getAttribute(objectName, "TotalSwapSpaceSize");
        freeValue = (Long) connection.getAttribute(objectName, "FreeSwapSpaceSize");
      }
      return new CapabilityValue(getValue(totalValue, freeValue, capabilityUri));
    } catch (ReflectionException e) {
      throw new IOException(e);
    } catch (InstanceNotFoundException e) {
      throw new IOException(e);
    } catch (AttributeNotFoundException e) {
      throw new IOException(e);
    } catch (MBeanException e) {
      throw new IOException(e);
    } catch (MalformedObjectNameException e) {
      throw new IOException(e);
    }
  }


  private long getValue(long totalMemory, long freeMemory, String capability) {
    if (capability.equals(KnowledgeConstants.TOTAL_MEM_CAP)) {
      return totalMemory;
    }
    if (capability.equals(KnowledgeConstants.FREE_MEM_CAP)) {
      return freeMemory;
    }
    if (capability.equals(KnowledgeConstants.USED_MEM_CAP)) {
      return totalMemory - freeMemory;
    }
    throw new IllegalArgumentException("Got invalid capability uri: " + capability);
  }

}
