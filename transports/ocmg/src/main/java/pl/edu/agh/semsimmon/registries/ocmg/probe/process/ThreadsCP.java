package pl.edu.agh.semsimmon.registries.ocmg.probe.process;

import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.Thread;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;
import pl.edu.agh.semsimmon.registries.ocmg.probe.CapabilityProbe;

import java.util.List;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;


/**
 * @author tkozak
 *         Created at 32:13 25-05-2010
 * @TODO description
 */
public class ThreadsCP implements CapabilityProbe {

  public enum ThreadsCapabilityProbeType {
    ACTIVE, BLOCKED, TOTAL
  }

  private ThreadsCapabilityProbeType type;

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application, String capabilityType) throws OcmgException {
    org.balticgrid.ocmg.objects.Process process = findProcess(resource, application);
    List<Thread> threads = process.getThreads();
    int blockedThreadsCount = 0;
    try {
    switch (type) {
      case ACTIVE:
        for (Thread thread : threads) {
          if (thread.isRunning()) {
            blockedThreadsCount++;
          }
        }
        break;
      case BLOCKED:
        for (Thread thread : threads) {
          if (!thread.isRunning()) {
            blockedThreadsCount++;
          }
        }
        break;
      case TOTAL:
        blockedThreadsCount = threads.size();
        break;
    }
    return new CapabilityValue(blockedThreadsCount);
    } catch (UnsupportedOperationException e) {
      return new CapabilityValue(Double.NaN);
    }
  }

  public void setType(ThreadsCapabilityProbeType type) {
    this.type = type;
  }
}


