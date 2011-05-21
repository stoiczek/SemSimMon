package pl.edu.agh.semsimmon.registries.ocmg.probe.mem;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgConstants;
import pl.edu.agh.semsimmon.registries.ocmg.probe.CapabilityProbe;
import pl.edu.agh.semsimmon.registries.ocmg.util.OcmgUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tkozak
 * Date: 21.05.11
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class PhyMemoryProbe extends MemoryProbe implements CapabilityProbe {

  private static final String TOTAL_MEMORY_KEY = "MemTotal";
  private static final String FREE_MEMORY_KEY = "MemFree";
  private static final String CACHED_MEMORY_KEY = "Cached";

  @Override
  protected CapabilityValue getCapabilityValue(NodeTree nodeTree, Resource resource, MemoryProbeType type) throws ConnectionException, MonitorException {
    CapabilityValue value;
    String memInfo = OcmgUtils.getFileContent(nodeTree, OcmgConstants.MEMORY_DETAILS_FILE);
    Map<String, Long> values = parseValues(memInfo);
    long totalMemory = values.get(TOTAL_MEMORY_KEY);
    long freeMemory = values.get(FREE_MEMORY_KEY);
    long cachedMemory = values.get(CACHED_MEMORY_KEY);
    switch (type) {
      case TOTAL:
        value = new CapabilityValue(totalMemory);
        break;
      case FREE:
        value = new CapabilityValue(freeMemory);
        break;
      case USED:
        value = new CapabilityValue(totalMemory - freeMemory);
        break;
      case CACHED:
        value = new CapabilityValue(cachedMemory);
        break;
      default:
        value = new CapabilityValue(Double.NaN);
    }
    return value;
  }

  private Map<String, Long> parseValues(String memInfo) {
    Map<String, Long> values = new HashMap<String, Long>();
    for (String line : memInfo.split("\n")) {
      String[] keyValue = line.split(":");
      String key = keyValue[0];
      String valueEntry = keyValue[1].trim().toLowerCase();
      Long value = Long.parseLong(keyValue[1].replaceAll("\\D", "").trim());
      if (valueEntry.endsWith("kb")) {
        value *= 1024;
      } else if (valueEntry.endsWith("mb")) {
        value *= 1024 * 1024;
      }
      values.put(key, value);
    }
    return values;
  }

}
