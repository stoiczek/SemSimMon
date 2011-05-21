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
 *         Created at 20:54 22-08-2010
 */
public class NetIfaceResourceAgent extends BaseNodesChildrenRA {

  private static final String IFACE_DETAILS_FILE = "/proc/net/dev";

  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    String result = OcmgUtils.getFileContent(nodeTree, IFACE_DETAILS_FILE);
    String[] lines = result.split("\n");
    LinkedList<Resource> interfaces = new LinkedList<Resource>();
    // skip first 2 lines
    for(int i= 2;i<lines.length;i++) {
      final String line = lines[i];
      String devName = line.substring(0, line.indexOf(':'));
      devName = devName.trim();
      final Resource iface = wrapResource(nodeResource, KnowledgeConstants.NETWORK_INTERFACE_URI, devName);
      iface.setProperty(ResourcePropertyNames.NetworkInterface.INTERFACE_NAME, devName);
      interfaces.add(iface);
    }
    return interfaces;
  }
}
