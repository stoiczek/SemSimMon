package pl.edu.agh.semsimmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.util.OcmgUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 19:29 22-08-2010
 */
public class StorageResourceAgent extends BaseNodesChildrenRA {

  private static final String GET_STORAGE_DEVICES_QUERY_PATTERN = ":node_get_disk_free([#],35)";

  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    final String requestString = GET_STORAGE_DEVICES_QUERY_PATTERN.replace("#", OcmgUtils.getNodeToken(nodeTree));
    final String result = OcmgUtils.executeQuery(nodeTree, requestString);
    String[] parts = result.split(",");
    int itemsCount = Integer.valueOf(parts[0]);
    parts[1] = parts[1].substring(1);
    parts[parts.length - 1] = parts[parts.length - 1].substring(0, parts[parts.length - 1].length() - 1);
    List<Resource> storageResources = new LinkedList<Resource>();
    for (int i = 0; i < itemsCount; i++) {
      final int index = 1 + (i * 3);
      final String mountPoint = parts[index + 2];
      final String device = parts[index];
      final String blocks = parts[index + 1];
      String label;
      if (device.equalsIgnoreCase("none")) {
        label = "System: " + mountPoint.replace("/dev/", "").replace("/", "_");
      } else {
        label = device.replace("/dev/", "").replace("/", "_");
      }
      final Resource resource = wrapResource(nodeResource, KnowledgeConstants.HARD_DRIVE_URI, label);
      resource.setProperty(ResourcePropertyNames.HardDrive.DEVICE_NAME, device);
      resource.setProperty(ResourcePropertyNames.HardDrive.BLOCKS, Long.parseLong(blocks));
      resource.setProperty(ResourcePropertyNames.HardDrive.MOUNT_POINT, mountPoint);
      storageResources.add(resource);
    }
    return storageResources;
  }

}
