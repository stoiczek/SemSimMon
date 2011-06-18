package pl.edu.agh.semsimmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import org.balticgrid.ocmg.objects.apphierarchy.SiteTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



/**
 * @author tkozak
 *         Created at 57:17 24-05-2010
 * @TODO description
 */
public class NodeResourceAgent extends AbstractResourceAgent {

  private static final Logger log = LoggerFactory.getLogger(NodeResourceAgent.class);

  @Override
  public List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException {
    String siteId = (String) parent.getProperty(ResourcePropertyNames.Cluster.CLUSTER_ID);
    if(siteId == null) {
      log.warn("Cluster ID not specified - cannot discover nodes");
      return new LinkedList<Resource>();
    }
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
        final String cacheName = nodeTree.getNode().getCacheName();
        final String name = node.getInfo().getName();
        final Resource resource = wrapResource(parent, type, name);
        resource.setProperty(ResourcePropertyNames.Node.NAME, name);
        resource.setProperty(ResourcePropertyNames.Node.ID, cacheName);
        nodes.add(resource);
      }
      return nodes;
    } catch (ConnectionException e) {
      throw new OcmgException("Error connecting OCMG");
    } catch (MonitorException e) {
      throw new OcmgException("Error communicationg OCMG");
    }
  }

}
