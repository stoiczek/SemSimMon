package pl.edu.agh.semsimmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.Thread;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.api.resource.ResourceState;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

import java.util.LinkedList;
import java.util.List;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;
import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findThread;


/**
 * @author tkozak
 *         Created at 39:18 24-05-2010
 *         Manages OCMG threads.
 */
public class ThreadResourceAgent extends AbstractResourceAgent {

  private static final String TOKEN_ID_PARAM = "tokenId";

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
        threadResource.setProperty(TOKEN_ID_PARAM, thread.getToken().getValue());
        children.add(threadResource);
        ResourceState state;

        try {
          if (process.isRunning()) {
            state = ResourceState.RUNNING;
          } else if (process.isTerminated()) {
            state = ResourceState.STOPPED;
          } else {
            state = ResourceState.PAUSED;
          }
        } catch (UnsupportedOperationException e) {
          threadResource.setProperty(ResourcePropertyNames.RESOURCE_PAUSEABLE_PN, false);
          threadResource.setProperty(ResourcePropertyNames.RESOURCE_STOPPABLE_PN, false);
          state = ResourceState.UNKNOWN;
        }
        threadResource.setProperty(ResourcePropertyNames.RESOURCE_STATE, state);
      }
      return children;
    } catch (ConnectionException e) {
      throw new OcmgException("Error connecting OCMG");
    } catch (MonitorException e) {
      throw new OcmgException("Error communicationg OCMG");
    }
  }

  @Override
  public void pause(Application app, Resource resource) throws OcmgException {
    Thread thread = findThread((Integer) resource.getProperty(ResourcePropertyNames.Thread.OWNING_PROCESS_ID),
        (String) resource.getProperty(TOKEN_ID_PARAM), app);
    try {
      thread.resume();
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }


  @Override
  public void resume(Application app, Resource resource) throws OcmgException {
    Thread thread = findThread((Integer) resource.getProperty(ResourcePropertyNames.Thread.OWNING_PROCESS_ID),
        (String) resource.getProperty(TOKEN_ID_PARAM), app);
    try {
      thread.resume();
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }

  @Override
  public void stop(Application app, Resource resource) throws IllegalArgumentException, OcmgException {
    Thread thread = findThread((Integer) resource.getProperty(ResourcePropertyNames.Thread.OWNING_PROCESS_ID),
        (String) resource.getProperty(TOKEN_ID_PARAM), app);
    try {
      thread.resume();
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }
}
