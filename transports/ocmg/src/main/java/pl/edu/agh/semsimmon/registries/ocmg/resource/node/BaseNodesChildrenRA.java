package pl.edu.agh.semsimmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;
import pl.edu.agh.semsimmon.registries.ocmg.resource.AbstractResourceAgent;

import java.util.List;

/**
 * @author tkozak
 *         Created at 27:12 25-05-2010
 * @TODO description
 */
public abstract class BaseNodesChildrenRA extends AbstractResourceAgent {

  @Override
  public List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException {
    final String site = (String) parent.getProperty(ResourcePropertyNames.Cluster.CLUSTER_ID);
    final String nodeId = (String) parent.getProperty(ResourcePropertyNames.Node.ID);
    try {
      final NodeTree nodeTree = AppHierarchyParsingUtils.findNodeTree(application, site, nodeId);
      return doDiscover(nodeTree, parent, type);
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }

  protected abstract List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException;

}
