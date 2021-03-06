package pl.edu.agh.semsimmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgConstants;
import pl.edu.agh.semsimmon.registries.ocmg.util.OcmgUtils;

import java.util.LinkedList;
import java.util.List;

import static pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames.VirtualMemory.*;

/**
 * Discovery agent for virtual memory - swaps.
 *
 * @author tkozak
 *         Created at 16:56 24-08-2010
 */
public class VirtualMemoryRA extends BaseNodesChildrenRA {

  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    String result = OcmgUtils.getFileContent(nodeTree, OcmgConstants.SWAPS_INFO_FILE);
    String[] lines = result.split("\n");

    List<Resource> swaps = new LinkedList<Resource>();
    for (int i = 1; i < lines.length; i++) {
      final String[] details = lines[i].split("\\s+");
      final String devName = details[0];
      final String swapType = details[1];
      final long size  = Integer.parseInt(details[2]);
      final String resourceName = "VirtualMemory" + devName.replace("/", "_");
      final Resource swap = wrapResource(nodeResource, KnowledgeConstants.VIRTUAL_MEMORY_URI, resourceName);
      swap.setProperty(DEVICE_NAME, devName);
      swap.setProperty(TYPE, swapType);
      swap.setProperty(SIZE, size);
      swaps.add(swap);
    }
    return swaps;
  }
}
