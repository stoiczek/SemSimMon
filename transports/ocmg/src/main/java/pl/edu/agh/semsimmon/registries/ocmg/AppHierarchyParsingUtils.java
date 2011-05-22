package pl.edu.agh.semsimmon.registries.ocmg;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.*;
import org.balticgrid.ocmg.objects.Process;
import org.balticgrid.ocmg.objects.Thread;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import org.balticgrid.ocmg.objects.apphierarchy.SiteTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.List;

/**
 * @author tkozak
 *         Created at 34:13 25-05-2010
 * @TODO description
 */
public class AppHierarchyParsingUtils {


  public static String[] getUriParts(Resource parent) {
    String parentUri = parent.getUri();
    if (parentUri.endsWith("/")) {
      parentUri = parentUri.substring(0, parentUri.length() - 1);
    }
    String[] uriParts = parentUri.split("/");
    return uriParts;
  }

  public static org.balticgrid.ocmg.objects.Process getProcessById(Application app, int processGlobalId) throws OcmgException {
    List<Process> processes = null;
    try {
      processes = app.getProcessList();

      Process resourceProcess = null;

      for (Process process : processes) {

        if (process.getStaticInfo().getGlobalId() == processGlobalId) {
          resourceProcess = process;
          break;
        }
      }
      if (resourceProcess == null) {
        throw new IllegalArgumentException("Invalid resource - couldn't find corresponding process");
      }
      return resourceProcess;
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }

  }

  public static org.balticgrid.ocmg.objects.Process findProcess(Resource processResource, Application application) throws OcmgException {
    return getProcessById(application, (Integer) processResource.getProperty(ResourcePropertyNames.Process.GLOBAL_ID));
  }

  public static org.balticgrid.ocmg.objects.Thread findThread(int processGlobalId, String token, Application app) throws OcmgException {
    List<Process> processes = null;
        try {
          processes = app.getProcessList();

          Process resourceProcess = null;

          for (Process process : processes) {

            if (process.getStaticInfo().getGlobalId() == processGlobalId) {
              resourceProcess = process;
              break;
            }
          }
          if (resourceProcess == null) {
            throw new IllegalArgumentException("Invalid resource - couldn't find corresponding process");
          }
          Thread foundThread = null;
          for(Thread thread : resourceProcess.getThreads()) {
            if(thread.getToken().getValue().equals(token)) {
              foundThread = thread;
            }
          }
          return foundThread;
        } catch (ConnectionException e) {
          throw new OcmgException(e);
        } catch (MonitorException e) {
          throw new OcmgException(e);
        }

  }

  public static NodeTree findNodeTree(Application application, String site, String node) throws ConnectionException, MonitorException, OcmgException {
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
}
