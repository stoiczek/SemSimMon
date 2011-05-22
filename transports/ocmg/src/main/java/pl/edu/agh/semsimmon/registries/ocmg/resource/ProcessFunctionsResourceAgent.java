package pl.edu.agh.semsimmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.meters.LibCallMeter;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.context.InvalidParameterException;
import org.balticgrid.ocmg.wrappers.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.LibCallMetersContainer;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;


/**
 * @author tkozak
 *         Created at 48:18 24-05-2010
 * @TODO description
 */
public class ProcessFunctionsResourceAgent extends AbstractResourceAgent {

  private static final Logger log = LoggerFactory.getLogger(ProcessFunctionsResourceAgent.class);

  private LibCallMetersContainer metersContainer;

  @Override
  public List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException {
    try {
      log.debug("Discovering functions of process: {}", parent);
      org.balticgrid.ocmg.objects.Process process = findProcess(parent, application);
      List<Resource> children = new LinkedList<Resource>();
      final List<Function> functions = process.getFunctions();
      if (functions == null) {
        return Collections.<Resource>emptyList();
      }
      final List<org.balticgrid.ocmg.objects.Thread> threadList = process.getThreads();
      for (Function function : functions) {
        if(!function.getFunctionName().toLowerCase().matches(".*send.*|.*recv.*|.*gather.*|.*scatter.*|.*barrier.*|.*bcast.*")) {
          continue;
        }
        log.debug("Processing function: {}" , function.getFunctionName());
        final String functionId = function.getFileName() + ":" + function.getFunctionName();

        LibCallMeter callMeter = new LibCallMeter(application.getMonitor(), null, function.getFunctionName(),
            null, null, LibCallMeter.Type.TYPE_COUNT_TIME);
        final Resource resource = wrapResource(parent, type, functionId);
        metersContainer.addMeter(resource.getUri(), callMeter);
        resource.setProperty(ResourcePropertyNames.Function.FILE_NAME, function.getFileName());
        resource.setProperty(ResourcePropertyNames.Function.FUNCTION_NAME, function.getFunctionName());
        resource.setProperty(ResourcePropertyNames.Function.START_ADDRESS, function.getStartAddress());
        resource.setProperty(ResourcePropertyNames.Function.END_ADDRESS, function.getEndAddress());
        children.add(resource);
      }
      return children;
    } catch (ConnectionException e) {
      throw new OcmgException("Error connecting OCMG");
    } catch (MonitorException e) {
      throw new OcmgException("Error communicationg OCMG");
    } catch (InvalidParameterException e) {
      throw new OcmgException("Error Creating LibCallMeter", e);
    }
  }


  public void setMetersContainer(LibCallMetersContainer metersContainer) {
    this.metersContainer = metersContainer;
  }
}
