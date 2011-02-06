package pl.edu.agh.semmon.registries.ocmg.probe.node;

import org.balticgrid.ocmg.base.*;
import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.OcmgException;
import pl.edu.agh.semmon.registries.ocmg.probe.CapabilityProbe;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static pl.edu.agh.semmon.registries.ocmg.AppHierarchyParsingUtils.getUriParts;


/**
 * @author tkozak
 *         Created at 23:11 28-05-2010
 * Probes for node average load in last 1, 5, and 15 minutes.
 */
public class LoadAvgProbe implements CapabilityProbe {

  public enum Type {
    _1MIN, _5MIN, _15MIN
  }

  private static final String QUERY = ":node_get_proc_loadavg([{0}],0)";

  private Type type;

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application) throws OcmgException {
    final Connection connection = application.getConnection();
    final String[] uriParts = getUriParts(resource);
    final String node = uriParts[uriParts.length - 1];
    final String query = MessageFormat.format(QUERY, node);
    ReplyFragment statusFragment = null;
    try {
      final Reply reply = connection.sendRequest(query);
      statusFragment = reply.getFragmentsList().get(0).get(0);
      statusFragment.checkStatus();
      ReplyFragment resultFragment = reply.getFragmentsList().get(1).get(0);
      // this result contains value in form X/Y
      // where X is running jobs
      // and Y is io waiting jobs
      String[] resultSplit = resultFragment.getResult().split(",");
      int indx = 0;
      switch (type) {
        case _1MIN:
          indx = 0;
          break;
        case _5MIN:
          indx = 1;
          break;
        case _15MIN:
          indx = 2;
          break;
      }
      double value = Double.parseDouble(resultSplit[indx]);
      return new CapabilityValue(value);
    } catch (ConnectionException e) {
      throw new OcmgException("Error comunicating with OCM-G");
    } catch (MonitorException e) {
      throw new OcmgException("Got error result from communicator. Error code: " + statusFragment.getStatus());
    }
  }

  public void setType(Type type) {
    this.type = type;
  }
}
