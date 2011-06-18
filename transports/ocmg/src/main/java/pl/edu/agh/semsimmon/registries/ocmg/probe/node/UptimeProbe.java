package pl.edu.agh.semsimmon.registries.ocmg.probe.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;
import pl.edu.agh.semsimmon.registries.ocmg.probe.CapabilityProbe;
import pl.edu.agh.semsimmon.registries.ocmg.util.OcmgUtils;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findNodeTree;

/**
 * Created by IntelliJ IDEA.
 * User: tkozak
 * Date: 21.05.11
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class UptimeProbe implements CapabilityProbe {

  private static final String UPTIME_INFO_FILE = "/proc/uptime";

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application, String capabilityType)
      throws OcmgException {
    try {
      final String site = (String) resource.getProperty(ResourcePropertyNames.Cluster.CLUSTER_ID);
      final String nodeId = (String) resource.getProperty(ResourcePropertyNames.Node.ID);

      final NodeTree nodeTree = findNodeTree(application, site, nodeId);
      String info = OcmgUtils.getFileContent(nodeTree, UPTIME_INFO_FILE);
      double uptime = Double.parseDouble(info.split(" ")[0].trim());
      return new CapabilityValue(uptime);
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    }
  }
}
