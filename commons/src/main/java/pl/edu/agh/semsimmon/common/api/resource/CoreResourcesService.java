package pl.edu.agh.semsimmon.common.api.resource;

import pl.edu.agh.semsimmon.common.api.transport.TransportException;
import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.common.exception.ResourceNotRegisteredException;
import pl.edu.agh.semsimmon.common.exception.ResourcesException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Manages resource instances
 *
 * @author koperek
 * @author tkozak
 */
public interface CoreResourcesService {

  /**
   * @return
   */
  Collection<String> getAllRegisteredResources();

  /**
   * @param uri
   * @return
   */
  String getResourceType(String uri);

  /**
   * @param uri
   * @return
   */
  boolean isResourceRegistered(String uri);

  /**
   * @param uri
   * @param type
   * @param parameters
   * @throws pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException
   *
   */
  void registerResource(String uri, String type, Map<String, Object> parameters)
      throws ResourceAlreadyRegisteredException;

  void registerResource(Resource resource)
      throws ResourceAlreadyRegisteredException;


  /**
   * @param uri
   */
  void unregisterResource(String uri);

  /**
   * @param uri
   * @return
   */
  Resource getResourceForURI(String uri) throws ResourceNotRegisteredException;


  /**
   * @param uri
   * @return
   * @throws pl.edu.agh.semsimmon.common.exception.ResourceNotRegisteredException
   *
   */
  List<String> getResourceCapabilities(String uri)
      throws ResourceNotRegisteredException;


  /**
   * @param resource
   * @return
   * @throws pl.edu.agh.semsimmon.common.exception.ResourceNotRegisteredException
   *
   */
  List<String> getResourceCapabilities(Resource resource)
      throws ResourceNotRegisteredException;

  /**
   * Discovers given resource's direct children (one level beneath) of given type.
   * @param resource parent resource. Might be null if discovering root resources
   * @param childrenType type of children to discover
   * @return list of child resources.
   */
  List<Resource> discoverDirectChildren(Resource resource, String childrenType);

  /**
   * @param resourceListener
   */
  void addResourceListener(ResourceEventsListener resourceListener);

  /**
   * @param resourceListener
   */
  void removeResourceListener(ResourceEventsListener resourceListener);

  /**
   *
   * @param resource
   * @throws pl.edu.agh.semsimmon.common.api.transport.TransportException
   */
  void stopResource(Resource resource) throws ResourcesException;

  /**
   *
   * @param resource
   * @throws TransportException
   */
  void pauseResource(Resource resource) throws ResourcesException;

  /**
   *
   * @param resource
   * @throws TransportException
   */
  void resumeResource(Resource resource) throws ResourcesException;
}
