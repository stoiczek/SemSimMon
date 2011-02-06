package pl.edu.agh.semmon.registries.ocmg.probe.process;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.OcmgException;
import pl.edu.agh.semmon.registries.ocmg.probe.CapabilityProbe;

import static pl.edu.agh.semmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;

/**
 * @author tkozak
 *         Created at 45:13 25-05-2010
 * Probes for process total cpu time.
 */
public class TotalCpuTimeCapabilityProbe implements CapabilityProbe {

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application) throws OcmgException {
    if (!resource.getTypeUri().equals(KnowledgeConstants.PROCESS_URI)) {
      throw new IllegalArgumentException("Invalid uri type");
    }
    try {
      org.balticgrid.ocmg.objects.Process proc = findProcess(resource, application);
      return new CapabilityValue(proc.getDynamicInfo().getTotalTime());
    } catch (MonitorException e) {
      throw new OcmgException(e);
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    }
  }
}
