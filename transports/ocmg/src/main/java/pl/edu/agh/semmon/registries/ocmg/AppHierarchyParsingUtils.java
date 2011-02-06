package pl.edu.agh.semmon.registries.ocmg;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.Process;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;

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
}
