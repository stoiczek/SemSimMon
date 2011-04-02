package pl.edu.agh.semsimmon.registries.ocmg.resource.node;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.Arrays;
import java.util.List;

/**
 * @author tkozak
 *         Created at 35:12 25-05-2010
 * @TODO description
 */
public class BasicHardwareResourceAgent extends BaseNodesChildrenRA {

  private String childResourceName;

  @Override
  protected List<Resource> doDiscover(NodeTree nodeTree, Resource nodeResource, String type) throws ConnectionException, MonitorException {
    return Arrays.asList(wrapResource(nodeResource, type, childResourceName));
  }

  public void setChildResourceName(String childResourceName) {
    this.childResourceName = childResourceName;
  }
}
