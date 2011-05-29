/**
 *
 */
package pl.edu.agh.semsimmon.core.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.knowledge.IKnowledge;
import pl.edu.agh.semsimmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semsimmon.common.api.resource.*;
import pl.edu.agh.semsimmon.common.api.transport.TransportException;
import pl.edu.agh.semsimmon.common.api.transport.TransportProxy;
import pl.edu.agh.semsimmon.common.exception.MeasurementException;
import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.common.exception.ResourceNotRegisteredException;
import pl.edu.agh.semsimmon.common.exception.ResourcesException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.common.vo.core.resource.ResourceEventImpl;
import pl.edu.agh.semsimmon.core.remote.CoreRemoteService;
import pl.edu.agh.semsimmon.core.transport.TransportProxiesManager;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author koperek
 * @author tkozak
 */
public class CoreResourcesServiceImpl implements CoreResourcesService, IResourceDiscoveryListener {

  private static final Logger log = LoggerFactory.getLogger(CoreResourcesServiceImpl.class);


  /**
   *
   */
  private Map<String, List<String>> resourcesTree = new HashMap<String, List<String>>();

  /**
   *
   */
  private Map<String, Resource> resources = new HashMap<String, Resource>();

  /**
   *
   */
  private List<ResourceEventsListener> resourceListeners = new LinkedList<ResourceEventsListener>();

  /**
   *
   */
  private Map<String, List<String>> resourceCapabilities = new HashMap<String, List<String>>();


  /**
   *
   */
  private IKnowledge knowledge;

  /**
   *
   */
  private TransportProxiesManager transportProxiesManager;

  private CoreMeasurementService coreMeasurementService;


  /*
 =================================================================================
 CoreResourcesService implementation
 =================================================================================
  */

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerResource(Resource resource) throws ResourceAlreadyRegisteredException {
    final String uri = resource.getUri();
    log.debug("Registering Resource: {}", resource);
    final TransportProxy resourceProxy = transportProxiesManager.findProxyForResource(resource);
    try {
      resourceProxy.registerResource(resource);
      fireResourceAddedEvent(Arrays.asList(resource));
      // Discovery of children has to be last step here, as it gives us Breadth-first search algorithm instead
      // of deep-first, which is better for upper layers to handle events in such order.
      List<String> types = knowledge.getChildrenResourceTypes(resource.getTypeUri());
      if (resources.containsKey(uri)) {
        Resource registered = resources.get(uri);
        registered.merge(resource);
      } else {
        resources.put(uri, resource);
      }
      resourcesTree.put(uri, null);
      resourceProxy.discoverChildren(resource, types);
      log.debug("Resource registered");
    } catch (TransportException e) {
      log.error("Got Transport exception during node registration", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerResource(String uri, String type, Map<String, Object> properties)
      throws ResourceAlreadyRegisteredException {
    // TODO add optimization to first register all resources and then fire event.
    Resource resource = new Resource(uri, type, properties);
    registerResource(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isResourceRegistered(String uri) {
    return this.resourcesTree.containsKey(uri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterResource(String uri) {
    log.debug("Trying to remove resource with uri: " + uri);
    if (!resources.containsKey(uri)) {
      return;
    }
    Resource resource = resources.get(uri);
    final TransportProxy proxy = transportProxiesManager.findProxyForResource(resource);
    try {
      if (proxy.isResourceSupported(resource)) {
        final boolean branchPruned = proxy.unregisterResource(resource);
        removeChildResources(uri, branchPruned);
      }
    } catch (TransportException e) {
      log.error("Got error whilie unregistering resource: " + resource, e);
    }
    resourcesTree.remove(uri);
    resources.remove(uri);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void addResourceListener(ResourceEventsListener resourceListener) {
    this.resourceListeners.add(resourceListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeResourceListener(ResourceEventsListener resourceListener) {
    this.resourceListeners.remove(resourceListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResourceType(String uri) {
    if (resources.containsKey(uri)) {
      return resources.get(uri).getTypeUri();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getAllRegisteredResources() {
    return new HashSet<String>(resourcesTree.keySet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getResourceCapabilities(String uri)
      throws ResourceNotRegisteredException {
    checkResourceRegistered(uri);
    return getResourceCapabilities(resources.get(uri));

  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getResourceCapabilities(Resource resource) throws ResourceNotRegisteredException {
    log.debug("Getting resource capabilities for resource: ");
    String uri = resource.getUri();
    if (!resourceCapabilities.containsKey(uri)) {
      List<String> capabilities = knowledge.getCapabilitiesOfResourceType(resource.getTypeUri());
      resourceCapabilities.put(uri, capabilities);
    }
    final List<String> capabilities = resourceCapabilities.get(uri);
    log.debug("Returning list containing {} entries.", capabilities.size());
    return capabilities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Resource> discoverDirectChildren(Resource resource, String childrenType) {
    log.debug("Getting direct children of resource: {}. Children type: {}", resource, childrenType);
    final List<TransportProxy> proxies = transportProxiesManager.getAllProxies();
    List<Resource> children = new LinkedList<Resource>();
    for (TransportProxy proxy : proxies) {
      if (proxy.isResourceSupported(resource)) {
        try {
          final List<Resource> resourceChildren = proxy.discoverDirectChildren(resource, childrenType);
          mergeResources(children, resourceChildren);
        } catch (TransportException e) {
          log.error("Error fetching children of resource: " + resource + " and type: " + childrenType, e);
        }
      }
    }
    log.debug("Returning list containing {} items.", children.size());
    return children;
  }

  @Override
  public void stopResource(Resource resource) throws ResourcesException {
    log.debug("Stopping resource: {}.", resource);
    final List<TransportProxy> proxies = transportProxiesManager.getAllProxies();
    boolean succeded = false;
    for (TransportProxy proxy : proxies) {
      if (proxy.isResourceSupported(resource)) {
        try {
          proxy.stopResource(resource);
          succeded = true;
        } catch (TransportException e) {
          log.error("Error stopping resource");
        }
      }
    }
    if (!succeded) {
      throw new ResourcesException("Cannot find proxy capable to stop given resource");
    }
  }

  @Override
  public void pauseResource(Resource resource) throws ResourcesException {
    log.debug("Pausing resource: {}.", resource);
    final List<TransportProxy> proxies = transportProxiesManager.getAllProxies();
    boolean succeded = false;
    for (TransportProxy proxy : proxies) {
      if (proxy.isResourceSupported(resource)) {
        try {
          proxy.pauseResource(resource);
          succeded = true;
        } catch (TransportException e) {
          log.error("Error pausing resource");
        }
      }
    }
    if (!succeded) {
      throw new ResourcesException("Cannot find proxy capable to pause given resource");
    }
  }

  @Override
  public void resumeResource(Resource resource) throws ResourcesException {
    log.debug("Resuming resource: {}.", resource);
    final List<TransportProxy> proxies = transportProxiesManager.getAllProxies();
    boolean succeded = false;
    for (TransportProxy proxy : proxies) {
      if (proxy.isResourceSupported(resource)) {
        try {
          proxy.resumeResource(resource);
          succeded = true;
        } catch (TransportException e) {
          log.error("Error resuming resource");
        }
      }
    }
    if (!succeded) {
      throw new ResourcesException("Cannot find proxy capable to resume given resource");
    }

  }

  /*
 =================================================================================
 Private utilities.
 =================================================================================
  */

  private void mergeResources(List<Resource> mergeTo, List<Resource> mergeFrom) {
    for (Resource child1 : mergeFrom) {
      for (Resource child2 : mergeTo) {
        if (child2.getUri().equals(child1.getUri())) {
          child2.merge(child1);
          break;
        }
      }
      mergeTo.add(child1);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Resource getResourceForURI(String uri) throws ResourceNotRegisteredException {
    checkResourceRegistered(uri);
    return resources.get(uri);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void processEvent(IResourceDiscoveryEvent event) {
    switch (event.getEventType()) {
      case RESOURCES_REMOVED:
        try {
          removeResources(event.getResources());
        } catch (ResourcesException e) {
          log.error("Error removing resources", e);
        }
        break;
      case NEW_RESOURCES_DISCOVERED:
        try {
          for (Resource resource : event.getResources()) {
            addChildResource(event.getParentResource(), resource);
          }
        } catch (ResourceNotRegisteredException e) {
          log.error("Event contains parent URI which is not registered!", e);
        }
        break;
    }

  }

  private void removeResources(List<Resource> resources) throws ResourcesException {
    log.debug("Removing {} resources", resources.size());
    List<Resource> resourcesWithChildren = new LinkedList<Resource>();
    for (Resource resource : resources) {
      addChildren(resource, resourcesWithChildren);
    }
    log.debug("Removing {} resources with children", resourcesWithChildren.size());
    try {
      for (Resource resource : resources) {
        coreMeasurementService.removeMeasurementsOfResource(resource);
        this.resources.remove(resource.getUri());
      }
      fireResourcesRemovedEvent(resourcesWithChildren);
    } catch (MeasurementException e) {
      throw new ResourcesException(e);
    }
  }

  private void removeChildResources(String uri, boolean branchPruned) {
    if (resourcesTree.containsKey(uri) && resourcesTree.get(uri) != null) {


      for (String child : resourcesTree.get(uri)) {
        if (branchPruned) {
          removeChildResources(child, branchPruned);
        } else {
          unregisterResource(uri);
        }
      }
    }
  }

  private void addChildren(Resource resource, List<Resource> resourcesWithChildren) {
    final String rUri = resource.getUri();
    if (resourcesTree.containsKey(rUri) && resourcesTree.get(rUri) != null) {
      for (String childUri : resourcesTree.get(rUri)) {
        resourcesWithChildren.add(resources.get(childUri));
      }
    }
    resourcesWithChildren.add(resource);
  }

  private void checkResourceRegistered(String uri) throws ResourceNotRegisteredException {
    if (!resources.containsKey(uri)) {
      throw new ResourceNotRegisteredException("Resource with given URI haven't been registered. URI: " + uri);
    }
  }

  private void fireResourceAddedEvent(List<Resource> resources) {
    for (ResourceEventsListener listener : resourceListeners) {
      try {
        listener.processEvent(new ResourceEventImpl(ResourceEvent.Type.RESOURCES_ADDED, resources));
      } catch (Exception e) {
        log.error("Error passing resources added event", e);
      }
    }
  }

  private void fireResourcesRemovedEvent(List<Resource> resources) {
    for (ResourceEventsListener listener : resourceListeners) {
      try {
        listener.processEvent(new ResourceEventImpl(ResourceEvent.Type.RESOURCES_REMOVED, resources));
      } catch (Exception e) {
        log.error("Error passing resources added event", e);
      }
    }
  }

  private void addChildResource(String parentUri, String uri, String type, Map<String, Object> parameters)
      throws ResourceNotRegisteredException {
    if (resourcesTree.containsKey(parentUri)) {
      try {
        registerResource(uri, type, parameters);
      } catch (ResourceAlreadyRegisteredException e) {
        // nothing bad happened - we were just adding a child which was
        // added before
      }

      List<String> children = resourcesTree.get(parentUri);
      if (children == null) {
        children = new ArrayList<String>();
      }

      children.add(uri);
      resourcesTree.put(parentUri, children);
    } else {
      throw new ResourceNotRegisteredException(parentUri);
    }
  }

  private void addChildResource(Resource parentResource, Resource childResource) throws ResourceNotRegisteredException {
    addChildResource(parentResource.getUri(), childResource.getUri(), childResource.getTypeUri(),
        childResource.getProperties());
  }


  @SuppressWarnings({"UnusedDeclaration"})
  public void setKnowledge(IKnowledge knowledge) {
    this.knowledge = knowledge;
  }

  public void setTransportProxiesManager(TransportProxiesManager transportProxiesManager) {
    this.transportProxiesManager = transportProxiesManager;
  }

  public void setCoreRemoteService(CoreRemoteService coreRemoteService) {
    resourceListeners.add(coreRemoteService);
  }

  public void setCoreMeasurementService(CoreMeasurementService coreMeasurementService) {
    this.coreMeasurementService = coreMeasurementService;
  }

  @PostConstruct
  public void setupResourceEventListening() {
    for (TransportProxy proxy : transportProxiesManager.getAllProxies()) {
      proxy.addResourceDiscoveryListener(this);
    }

  }


}
