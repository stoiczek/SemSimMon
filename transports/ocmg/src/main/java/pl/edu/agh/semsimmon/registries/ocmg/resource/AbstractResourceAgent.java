package pl.edu.agh.semsimmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import org.balticgrid.ocmg.objects.apphierarchy.SiteTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

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
      if (entry.getKey().startsWith(ResourcePropertyNames.PROPAGABLE_RESOURCE_NAME_PRFX)) {
        childParams.put(entry.getKey(), entry.getValue());
      }
    }
    final Resource resource = new Resource(parent.getUri() + "/" + resourceName, type, childParams);
    return resource;
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
