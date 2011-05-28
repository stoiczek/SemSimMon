package pl.edu.agh.semsimmon.common.api.transport;

import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryListener;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.List;
import java.util.Map;

/**
 * Engine holding information about resources attached with specific transport
 * adapter.
 *
 * @author koperek
 */
public interface TransportProxy {

  /**
   * Acquires and returns value of given resource's capability.
   *
   * @param resource       resource to get capability value of.
   * @param capabilityType type of capability
   * @return current value of given resource capability.
   * @throws TransportException on value reading error.
   */
  CapabilityValue getCapabilityValue(Resource resource, String capabilityType)
      throws TransportException;

  /**
   * Gathers capability values of resource and all capabilities in given list.
   *
   * @param resource       resource to get capability value of.
   * @param capabilityUris list of capability uris to get values of
   * @return map with results, in form: capabilityUri -> capabilityValue
   * @throws TransportException on any error while reading value
   */
  Map<String, CapabilityValue> getCapabilityValues(Resource resource, List<String> capabilityUris)
      throws TransportException;

  /**
   * Checks whether given resource has capability of given type.
   *
   * @param resource       resource to check
   * @param capabilityType value to check
   * @return <code>true</code> if given resource has given capability, <code>false</code> otherwise.
   * @throws TransportException on any error with checks.
   */
  boolean hasCapability(Resource resource, String capabilityType)
      throws TransportException;

  /**
   * Adds transport proxy litener.
   *
   * @param listener listener to add.
   */
  void addResourceDiscoveryListener(IResourceDiscoveryListener listener);

  /**
   * Removes given proxy listener.
   *
   * @param listener listener to remove.
   */
  void removeTransportProxyListener(IResourceDiscoveryListener listener);

  /**
   * Registers given resource.
   * One should check if given resource is supported using <code>isUriSupported</code> method.
   *
   * @param resource resource to register
   * @throws TransportException on any error with registration (e.g. when given resource isn't supported by TransportProxy implementation
   * @see TransportProxy#isResourceSupported(pl.edu.agh.semsimmon.common.vo.core.resource.Resource)
   */
  void registerResource(Resource resource) throws TransportException;

  /**
   *
   * @param resource resource to unregister (remove)
   * @returns true if whole branch was pruned and child resources don't have to be explicitly removed,
   *          false otherwise (child resources needs to be explicitly removed)
   * @throws TransportException
   */
  boolean unregisterResource(Resource resource) throws TransportException;

  /**
   * @param resource
   * @return
   */
  boolean isResourceSupported(Resource resource);

  /**
   * @param resource
   * @param types
   * @throws TransportException
   */
  void discoverChildren(Resource resource, List<String> types) throws TransportException;

  /**
   * @param resource
   * @param type
   * @return
   * @throws TransportException
   */
  List<Resource> discoverDirectChildren(Resource resource, String type) throws TransportException;

  /**
   *
   * Stops given resource. After calling stop, given resource is terminated and won't be available for application
   * @param resource resource to be stopped
   * @throws TransportException on any error
   */
  void stopResource(Resource resource) throws TransportException;

  /**
   * Pauses given resource.
   * @param resource resource to be paused.
   * @throws TransportException on any error
   */
  void pauseResource(Resource resource) throws TransportException;

  /**
   * Resumes given, already paused resource.
   * @param resource resource to be resumed
   * @throws TransportException
   */
  void resumeResource(Resource resource) throws TransportException;

}
