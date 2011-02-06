package pl.edu.agh.semmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import org.balticgrid.ocmg.wrappers.NodeInfo;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.util.Arrays;
import java.util.List;

/**
 * @author tkozak
 *         Created at 57:11 25-05-2010
 * @TODO description
 */
public class OSResourceAgent extends BaseNodesChildrenRA {
  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    final NodeInfo nodeInfo = nodeTree.getNode().getInfo();
    final Resource osResource = wrapResource(nodeResource, KnowledgeConstants.OS_URI, "OperatingSystem");
    osResource.setProperty(ResourcePropertyNames.Os.NAME, nodeInfo.getOsName());
    osResource.setProperty(ResourcePropertyNames.Os.RELEASE, nodeInfo.getOsRelease());
    osResource.setProperty(ResourcePropertyNames.Os.VERSION, nodeInfo.getOsVersion());
    return Arrays.asList(osResource);
  }

  //  @Override
//  public boolean supportsChildrenType(Resource parent, String type) {
//    return false;  //To change body of implemented methods use File | Settings | File Templates.
//  }
}
