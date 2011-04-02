package pl.edu.agh.semsimmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import org.balticgrid.ocmg.objects.apphierarchy.SiteTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

import java.util.LinkedList;
import java.util.List;


/**
 * @author tkozak
 *         Created at 57:17 24-05-2010
 * @TODO description
 */
public class NodeResourceAgent extends AbstractResourceAgent {

  @Override
  public List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException {
    String siteId = (String) parent.getProperty(ResourcePropertyNames.Cluster.ID);
    List<NodeTree> nodeTrees = null;
    try {
      for (SiteTree siteTree : application.getHierarchy().getSiteTree()) {
        if (siteTree.getSite().getCacheName().equals(siteId)) {
          nodeTrees = siteTree.getNodeTree();
        }
      }
      if (nodeTrees == null) {
        throw new OcmgException("Couldn't find parent site");
      }
      List<Resource> nodes = new LinkedList<Resource>();
      for (NodeTree nodeTree : nodeTrees) {
        final org.balticgrid.ocmg.objects.Node node = nodeTree.getNode();
        node.attach();
        final Resource resource = wrapResource(parent, type, nodeTree.getNode().getCacheName());
        resource.setProperty(ResourcePropertyNames.Node.NAME, node.getInfo().getName());
        nodes.add(resource);
      }
      return nodes;
    } catch (ConnectionException e) {
      throw new OcmgException("Error connecting OCMG");
    } catch (MonitorException e) {
      throw new OcmgException("Error communicationg OCMG");
    }
  }

//  @Override
//  public boolean supportsChildrenType(Resource parent, String type) {
//    return parent.getType().equals(KnowledgeConstants.CLUSTER_URI) && type.equals(KnowledgeConstants.NODE_URI);
//  }
}
