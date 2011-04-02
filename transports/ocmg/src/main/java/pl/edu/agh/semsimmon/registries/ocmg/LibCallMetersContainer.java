package pl.edu.agh.semsimmon.registries.ocmg;

import org.balticgrid.ocmg.meters.LibCallMeter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tkozak
 *         Created at 30:14 25-05-2010
 * @TODO description
 */
public class LibCallMetersContainer {

  /**
   *
   */
  private Map<String, LibCallMeter> metersCache = new HashMap<String, LibCallMeter>();

  public void addMeter(String resourceURI, LibCallMeter meter) {
    metersCache.put(resourceURI, meter);
  }

  public LibCallMeter getMeter(String resourceURI) {
    return metersCache.get(resourceURI);
  }

  public void removeMeter(String resourceURI) {
    metersCache.remove(resourceURI);
  }


}
