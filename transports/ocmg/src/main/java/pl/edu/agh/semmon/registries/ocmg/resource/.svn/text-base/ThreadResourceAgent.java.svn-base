package pl.edu.agh.semmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.OcmgException;

import java.util.LinkedList;
import java.util.List;

import static pl.edu.agh.semmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;


/**
 * @author tkozak
 *         Created at 39:18 24-05-2010
 * Manages OCMG threads.
 */
public class ThreadResourceAgent extends AbstractResourceAgent {

  @Override
  public List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException {
    try {
      org.balticgrid.ocmg.objects.Process process = findProcess(parent, application);
      List<Resource> children = new LinkedList<Resource>();
      for (org.balticgrid.ocmg.objects.Thread thread : process.getThreads()) {
        final Resource threadResource = wrapResource(parent, type, thread.getCacheName());
        threadResource.setProperty(ResourcePropertyNames.RESOURCE_PAUSEABLE_PN, true);
        threadResource.setProperty(ResourcePropertyNames.RESOURCE_STOPPABLE_PN, true);
        threadResource.setProperty(ResourcePropertyNames.Thread.OWNING_PROCESS_ID,
            process.getStaticInfo().getGlobalId());
        children.add(threadResource);

      }
      return children;
    } catch (ConnectionException e) {
      throw new OcmgException("Error connecting OCMG");
    } catch (MonitorException e) {
      throw new OcmgException("Error communicationg OCMG");
    }
  }

  @Override
  public void resume(Application app, Resource resource) throws OcmgException {
    super.stop(app, resource);
  }

  @Override
  public void pause(Application app, Resource resource) throws OcmgException{
    super.stop(app, resource);
  }

  @Override
  public void stop(Application app, Resource resource) throws OcmgException {
    super.stop(app, resource);
  }
}
