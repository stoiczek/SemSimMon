package pl.edu.agh.semsimmon.registries.ocmg.probe.mem;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;
import pl.edu.agh.semsimmon.registries.ocmg.probe.CapabilityProbe;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findNodeTree;

/**
 * Created by IntelliJ IDEA.
 * User: tkozak
 * Date: 21.05.11
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class MemoryProbe implements CapabilityProbe {


  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application, String capabilityType) throws OcmgException {
    try {
      final String site = (String) resource.getProperty(ResourcePropertyNames.Cluster.CLUSTER_ID);
      final String nodeId = (String) resource.getProperty(ResourcePropertyNames.Node.ID);
      final NodeTree nodeTree = findNodeTree(application, site, nodeId);
      return getCapabilityValue(nodeTree, resource, MemoryProbeType.parseFromUri(capabilityType));
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }

  protected abstract CapabilityValue getCapabilityValue(NodeTree nodeTree, Resource resource, MemoryProbeType type) throws ConnectionException, MonitorException;
}
