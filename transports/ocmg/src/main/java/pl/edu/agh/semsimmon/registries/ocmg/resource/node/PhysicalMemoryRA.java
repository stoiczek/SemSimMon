package pl.edu.agh.semsimmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.Arrays;
import java.util.List;

/**
 * @author tkozak
 *         Created at 18:12 24-08-2010
 */
public class PhysicalMemoryRA extends BaseNodesChildrenRA {

  public static final String GET_MEMORY_DETAILS_QUERY = ":node_get_file_listing([#],\"/proc/meminfo\",0,0)";

  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    String result = executeQuery(nodeTree, GET_MEMORY_DETAILS_QUERY);
    String[] lines = result.split("\n");
    String memorySize = lines[0].split(":")[1].trim();
    final Resource physicalMemResource =
        wrapResource(nodeResource, KnowledgeConstants.PHYSICAL_MEMORY_URI, "PhysicalMemory");
    physicalMemResource.setProperty(ResourcePropertyNames.PhysicalMemory.SIZE, memorySize);
    return Arrays.asList(physicalMemResource);
  }
}
