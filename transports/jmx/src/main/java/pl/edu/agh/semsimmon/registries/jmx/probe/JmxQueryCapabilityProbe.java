package pl.edu.agh.semsimmon.registries.jmx.probe;

import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.util.Map;

/**
 * @author tkozak
 *         Created at 20:43 16-07-2010
 */
public class JmxQueryCapabilityProbe implements CapabilityProbe {


  /**
   *
   */
  private Map<String, JmxQuery> queries;

  @Override
  public CapabilityValue getCapabilityValue(Resource resource, String capabilityUri, MBeanServerConnection connection)
      throws IOException {
    if (!queries.containsKey(capabilityUri)) {
      throw new IllegalArgumentException("Given resource type and capability pair are unsupported");
    }
    final JmxQuery query = queries.get(capabilityUri);
    Object result = null;
    try {
      result = connection.getAttribute(ObjectName.getInstance(query.getObjectName()), query.getAttributeName());
    } catch (Exception e) {
      throw new IOException(e);
    }
    CapabilityValue value;
    if (result instanceof Long) {
      value = new CapabilityValue((Long) result);
    } else if (result instanceof Integer) {
      value = new CapabilityValue((Integer) result);
    } else if (result instanceof Byte) {
      value = new CapabilityValue((Byte) result);
    } else if(result instanceof Float) {
      value = new CapabilityValue((Float) result);
    } else if(result instanceof Double) {
      value = new CapabilityValue((Double) result);
    } else {
      throw new IllegalArgumentException("Got result of type which isn't numeric type: " + result.getClass());
    }
    return value;
  }

  public void setQueries(Map<String, JmxQuery> queries) {
    this.queries = queries;
  }
}
