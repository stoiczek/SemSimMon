package pl.edu.agh.semsimmon.registries.ocmg.probe.mem;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgConstants;
import pl.edu.agh.semsimmon.registries.ocmg.util.OcmgUtils;

/**
 * Created by IntelliJ IDEA.
 * User: tkozak
 * Date: 21.05.11
 * Time: 20:34
 * To change this template use File | Settings | File Templates.
 */
public class VirtualMemoryProbe extends MemoryProbe {

  @Override
  protected CapabilityValue getCapabilityValue(NodeTree nodeTree, Resource resource, MemoryProbeType type) throws ConnectionException, MonitorException {
    final String content = OcmgUtils.getFileContent(nodeTree, OcmgConstants.SWAPS_INFO_FILE);
    long total = 0;
    long used = 0;
    for(String line: content.split("\n")) {
      String[] splited = line.split("\\s+");
      if(splited[0].compareTo(String.valueOf(resource.getProperty(ResourcePropertyNames.VirtualMemory.DEVICE_NAME))) == 0) {
        total = Long.parseLong(splited[2]);
        used = Long.parseLong(splited[3]);
      }
    }
    CapabilityValue value;
     switch (type) {
      case TOTAL:
        value = new CapabilityValue(total);
        break;
      case FREE:
        value = new CapabilityValue(total - used);
        break;
      case USED:
        value = new CapabilityValue(used);
        break;
      case CACHED:
        value = new CapabilityValue(Double.NaN);
        break;
      default:
        value = new CapabilityValue(Double.NaN);
    }
    return value;
  }
}
