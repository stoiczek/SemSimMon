package pl.edu.agh.semsimmon.registries.ocmg.resource;

import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

import java.util.List;

/**
 * @author tkozak
 *         Created at 59:11 24-05-2010
 *         Interface providing facility to implement resources resource classes.
 */
public interface ResourceAgent {

  /**
   * Lookups child resources from given type in given parent resource
   *
   * @param application application object used in resource
   * @param parent      parent resource to lookup children from
   * @param type        type of children to lookup
   * @return list containing children resources.
   */
  List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException;

  /**
   * @param resource
   */
  void pause(Application app, Resource resource) throws IllegalArgumentException, OcmgException;

  /**
   * @param resource
   */
  void resume(Application app, Resource resource) throws IllegalArgumentException, OcmgException;

  /**
   * @param resource
   */
  void stop(Application app, Resource resource) throws IllegalArgumentException, OcmgException;

}
