package pl.edu.agh.semsimmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.util.OcmgUtils;

import java.util.*;

import static pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames.CPU.*;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 21:25 22-08-2010
 */
public class CpuResourceAgent extends BaseNodesChildrenRA {


  private static final Map<String, String> PROPERTY_TO_KEY_MAPPING;

  private static final String CPU_INFO_FILE = "/proc/cpuinfo";

  static {
    Map<String, String> prop2KeyMappingTemp = new HashMap<String, String>();
    prop2KeyMappingTemp.put("processor", ID);
    prop2KeyMappingTemp.put("vendor_id", VENDOR_ID);
    prop2KeyMappingTemp.put("cpu family", FAMILY);
    prop2KeyMappingTemp.put("model", MODEL);
    prop2KeyMappingTemp.put("model name", MODEL_NAME);
    prop2KeyMappingTemp.put("stepping", STEPPING);
    prop2KeyMappingTemp.put("cpu MHz", CLOCK);
    prop2KeyMappingTemp.put("cache size", CACHE);
    prop2KeyMappingTemp.put("flags", FLAGS);
    prop2KeyMappingTemp.put("bogomips", BOGO_MIPS);


    PROPERTY_TO_KEY_MAPPING = Collections.unmodifiableMap(prop2KeyMappingTemp);
  }


  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    String result = OcmgUtils.getFileContent(nodeTree, CPU_INFO_FILE);
    List<Resource> cpus = new LinkedList<Resource>();
    List<Map<String, String>> cpusDetails = getCpusDetails(result);
    for (Map<String, String> cpuDetails : cpusDetails) {
      final Resource cpu = parseCpu(nodeResource, cpuDetails);
      cpus.add(cpu);
    }
    return cpus;
  }

  private Resource parseCpu(Resource nodeResource, Map<String, String> cpuDetails) {
    final String cpuId = cpuDetails.get("processor");
    final Resource cpuRes = wrapResource(nodeResource, KnowledgeConstants.CPU_URI, "CPU_" + cpuId);
    for (Map.Entry<String, String> entry : PROPERTY_TO_KEY_MAPPING.entrySet()) {
      cpuRes.setProperty(entry.getValue(), cpuDetails.get(entry.getKey()));
    }
    return cpuRes;
  }

  private List<Map<String, String>> getCpusDetails(String result) {
    String[] lines = result.split("\n");
    List<Map<String, String>> details = new LinkedList<Map<String, String>>();
    Map<String, String> currentCPU = new HashMap<String, String>();
    for (String line : lines) {
      if (line.startsWith("processor")) {
        currentCPU = new HashMap<String, String>();
        details.add(currentCPU);
      }
      String[] splited = line.split(":");
      if (splited.length != 2) {
        continue;
      }
      currentCPU.put(splited[0].trim(), splited[1].trim());
    }
    return details;
  }
}
