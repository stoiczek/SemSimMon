package pl.edu.agh.semmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import org.balticgrid.ocmg.objects.apphierarchy.SiteTree;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.OcmgException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tkozak
 *         Created at 55:16 24-05-2010
 * @TODO description
 */
public abstract class AbstractResourceAgent implements ResourceAgent {

  protected Resource wrapResource(Resource parent, String type, String resourceName) {
    final Map<String, Object> childParams = new HashMap<String, Object>();
    for (Map.Entry<String, Object> entry : parent.getProperties().entrySet()) {
      if (!entry.getKey().startsWith(ResourcePropertyNames.RESOURCE_PROPERTY_PREFIX)) {
        childParams.put(entry.getKey(), entry.getValue());
      }
    }
    final Resource resource = new Resource(parent.getUri() + "/" + resourceName, type, childParams);
    return resource;
  }


  protected NodeTree findNodeTree(Application application, String site, String node) throws ConnectionException, MonitorException, OcmgException {
    NodeTree resourceNodeTree = null;
    for (SiteTree siteTree : application.getHierarchy().getSiteTree()) {
      if (siteTree.getSite().getCacheName().equals(site)) {
        for (NodeTree nodeTree : siteTree.getNodeTree()) {
          if (nodeTree.getNode().getCacheName().equals(node)) {
            resourceNodeTree = nodeTree;
          }
        }
      }
    }

    if (resourceNodeTree == null) {
      throw new OcmgException("Could find given parent resource");
    }
    return resourceNodeTree;
  }

  @Override
  public void pause(Application app, Resource resource) throws IllegalArgumentException, OcmgException {
    throw new IllegalArgumentException("Given resource isn't pauseable");
  }

  @Override
  public void resume(Application app, Resource resource) throws IllegalArgumentException, OcmgException {
    throw new IllegalArgumentException("Given resource isn't pauseable");
  }

  @Override
  public void stop(Application app, Resource resource) throws IllegalArgumentException, OcmgException {
    throw new IllegalArgumentException("Given resource isn't stoppable");
  }


}
