package pl.edu.agh.semsimmon.registries.ocmg.probe.process;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;
import pl.edu.agh.semsimmon.registries.ocmg.probe.CapabilityProbe;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;

/**
 * @author tkozak
 *         Created at 45:13 25-05-2010
 * Probes for process total cpu time.
 */
public class TotalCpuTimeCapabilityProbe implements CapabilityProbe {

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application, String capabilityType) throws OcmgException {
    try {
      org.balticgrid.ocmg.objects.Process process = findProcess(resource, application);
      return new CapabilityValue(process.getDynamicInfo().getTotalTime());
    } catch (MonitorException e) {
      throw new OcmgException(e);
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    }
  }
}
