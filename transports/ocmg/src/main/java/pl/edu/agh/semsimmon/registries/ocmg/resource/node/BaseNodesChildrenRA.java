package pl.edu.agh.semsimmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.*;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;
import pl.edu.agh.semsimmon.registries.ocmg.resource.AbstractResourceAgent;

import java.util.List;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.getUriParts;

/**
 * @author tkozak
 *         Created at 27:12 25-05-2010
 * @TODO description
 */
public abstract class BaseNodesChildrenRA extends AbstractResourceAgent {

  @Override
  public List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException {
    final String[] uriParts = getUriParts(parent);
    final String site = uriParts[uriParts.length - 2];
    final String nodeId = uriParts[uriParts.length - 1];
    try {
      final NodeTree nodeTree = findNodeTree(application, site, nodeId);
      return doDiscover(nodeTree, parent, type);
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }

  protected abstract List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException;

  protected String executeQuery(NodeTree nodeTree, String pattern) throws ConnectionException, MonitorException {
    final Connection conn = nodeTree.getNode().getConnection();
    String requestString = pattern.replace("#", nodeTree.getNode().getToken().getValue());
    Reply reply = conn.sendRequest(requestString);
    final int status = reply.getFragmentsList().get(0).get(0).getStatus();
    if(status != ReplyFragment.STATUS_OK) {
      throw new MonitorException("Error getting storage details", status);
    }
    String result = reply.getFragmentsList().get(1).get(0).getResult();
    return result;
  }
}
