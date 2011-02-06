package pl.edu.agh.semmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.Process;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import org.balticgrid.ocmg.wrappers.ProcessStaticInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.api.resource.ResourceState;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.OcmgException;

import java.util.LinkedList;
import java.util.List;

import static pl.edu.agh.semmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;

/**
 * @author tkozak
 *         Created at 21:18 24-05-2010
 *         Manages OCMG processes.
 */
public class ProcessResourceAgent extends BaseNodesChildrenRA {

  private static final Logger log = LoggerFactory.getLogger(ProcessResourceAgent.class);

  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    List<org.balticgrid.ocmg.objects.Process> processes = nodeTree.getProcessList();
    org.balticgrid.ocmg.objects.Process.getFunctions(nodeTree.getNode().getMonitor(), processes, false);
    List<Resource> children = new LinkedList<Resource>();
    for (org.balticgrid.ocmg.objects.Process process : processes) {
      process.attach();
      final Resource resource = wrapResource(nodeResource, type, process.getCacheName());
      final ProcessStaticInfo staticInfo = process.getStaticInfo();

      resource.setProperty(ResourcePropertyNames.Process.ARGUMENTS, staticInfo.getArguments());
      resource.setProperty(ResourcePropertyNames.Process.GLOBAL_ID, staticInfo.getGlobalId());
      resource.setProperty(ResourcePropertyNames.Process.USER_ID, staticInfo.getUserId());
      resource.setProperty(ResourcePropertyNames.Process.GROUP_ID, staticInfo.getGroupId());
      resource.setProperty(ResourcePropertyNames.RESOURCE_PAUSEABLE_PN, true);
      resource.setProperty(ResourcePropertyNames.RESOURCE_STOPPABLE_PN, true);
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
        log.warn("Caught exception during process state gathering. Returning state unknown");
        resource.setProperty(ResourcePropertyNames.RESOURCE_PAUSEABLE_PN, false);
        resource.setProperty(ResourcePropertyNames.RESOURCE_STOPPABLE_PN, false);
        state = ResourceState.UNKNOWN;
      }
      resource.setProperty(ResourcePropertyNames.RESOURCE_STATE, state);
      children.add(resource);
    }
    return children;
  }

  @Override
  public void pause(Application app, Resource resource) throws OcmgException {
    Process proc = findProcess(resource, app);
    try {
      proc.suspend();
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }


  @Override
  public void resume(Application app, Resource resource) throws OcmgException {
    Process proc = findProcess(resource, app);
    try {
      proc.resume();
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }

  @Override
  public void stop(Application app, Resource resource) throws IllegalArgumentException, OcmgException {
    Process proc = findProcess(resource, app);
    try {
      proc.resume();
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }
}
