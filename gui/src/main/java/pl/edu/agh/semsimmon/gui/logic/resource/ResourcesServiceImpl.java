package pl.edu.agh.semsimmon.gui.logic.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.CoreServiceFacade;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEvent;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEventsListener;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.consts.JmxRegistryConsts;
import pl.edu.agh.semsimmon.common.consts.OcmgRegistryConsts;
import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.common.exception.ResourcesException;
import pl.edu.agh.semsimmon.common.util.JmxUrlParsingUtil;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.gui.UiFactory;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnection;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnectionsManager;
import pl.edu.agh.semsimmon.gui.logic.resource.tree.ResourcesTree;
import pl.edu.agh.semsimmon.gui.logic.resource.tree.ResourcesTreeNode;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Implementation of CoreResourcesService. Nothing fancy.
 *
 * @author tkozak
 *         Created at 41:22 03-06-2010
 */
public class ResourcesServiceImpl implements ResourcesService, ResourceEventsListener {

  private static final Logger log = LoggerFactory.getLogger(ResourcesServiceImpl.class.getName());

  private static final String SYNTHETIC_APPS_NAMESPACE = "semsimmon://";

  private UiFactory uiFactory;

  private CoreConnectionsManager coreConnectionsManager;

  Map<String, ResourcesTree> applications = new HashMap<String, ResourcesTree>();

  List<ResourcesListener> resourcesListeners;

  /*
  =================================================================================
  ResourcesService implementation
  =================================================================================
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public void showAddJmxResourceWizard() {
    log.debug("Showing 'Add JMX resource' wizard");
    uiFactory.createAddJmxResourceDialog();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showAddOcmgResourceWizard() {
    log.debug("Showing 'Add OCM-G resource' wizard");
    uiFactory.createAddOcmgResourceDialog();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showAddMeasurementDialog(Resource resource) throws IOException {
    log.debug("Showing 'Add Measurement' dialog");
    uiFactory.createAddMeasurementController(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String addSyntheticApplication(String appLabel) {
    log.debug("Adding synthetic application with id: {}", appLabel);
    final String appUri = getAppUriPrefix() + appLabel;
    if (applications.containsKey(appUri)) {
      log.warn("Application alredy exists. skipping");
      // TODO think about it
      return appUri;
    }
    final Resource applicationResource = new Resource(appUri, KnowledgeConstants.APPLICATION_URI, new HashMap<String, Object>());
    applicationResource.setProperty(ResourcePropertyNames.RESOURCE_REMOVABLE, true);
    final ResourcesTreeNode appTreeRoot = addApplicationResource(applicationResource);
    fireResourceAddedEvent(Arrays.asList(appTreeRoot));
    log.debug("Application added");
    return appUri;
  }

  private ResourcesTreeNode addApplicationResource(Resource applicationResource) {
    final ResourcesTreeNode appNode = new ResourcesTreeNode(applicationResource, null);
    final ResourcesTree appTree = new ResourcesTree(appNode);
    applications.put(applicationResource.getUri(), appTree);
    return appNode;
  }

  /**
   * {@inheritDoc}
   *
   * @param application
   */
  @Override
  public void addOcmgApplication(Resource application) throws ResourceAlreadyRegisteredException {
    final CoreConnection connection = coreConnectionsManager.getConnectionOfResource(application);
    connection.getCoreServiceFacade().registerResource(application);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String addSyntheticCluster(String appId, String clusterLabel) {
    log.debug("Adding syntethic cluster {} to application: {}", new Object[]{clusterLabel, appId});
    final String clusterUri = appId + "/" + clusterLabel;
    final Resource clusterResource = new Resource(clusterUri, KnowledgeConstants.CLUSTER_URI, new HashMap<String, Object>());
    clusterResource.setProperty(ResourcePropertyNames.RESOURCE_REMOVABLE, true);
    final ResourcesTreeNode resourceNode = applications.get(appId).addResource(appId, clusterResource);
    fireResourceAddedEvent(Arrays.asList(resourceNode));
    log.debug("Cluster added");
    return clusterUri;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String addJmxNode(String clusterId, String serviceUri, String connectionId) throws ResourceAlreadyRegisteredException {
    log.debug("Adding jmx node to cluster {}.", clusterId);
    final CoreServiceFacade coreService = coreConnectionsManager.getConnectionById(connectionId).getCoreServiceFacade();
    final Map<String, Object> props = Collections.<String, Object>singletonMap(JmxRegistryConsts.SERVICE_URL_PROPERTY, serviceUri);
    final String nodeUri;
    nodeUri = clusterId + "/" + JmxUrlParsingUtil.getServiceHost(serviceUri);
    coreService.registerResource(nodeUri, KnowledgeConstants.NODE_URI, props);
    log.debug("Node added");
    return nodeUri;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void removeResource(String uri) throws IllegalArgumentException {
    log.debug("Removing resource");
    final String appUri = getApplicationUri(uri);
    final ResourcesTree appTree = applications.get(appUri);
    Resource resource = appTree.getResourceTreeNode(uri).getResource();
    CoreConnection connectionOfResource = coreConnectionsManager.getConnectionOfResource(resource);
    connectionOfResource.getCoreServiceFacade().unregisterResource(uri);

    log.debug("Resource removed");
  }

  @Override
  public List<Resource> getOcmgApplications(String connectionString, String coreConnectionId) {
    log.debug("Getting ocmg applications, monitored by MainSM: {}. Connection used: {}",
        connectionString, coreConnectionId);
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(OcmgRegistryConsts.MAIN_SM_CONNECTION_STRING, connectionString);
    properties.put(OcmgRegistryConsts.CONNECTION_TYPE, OcmgRegistryConsts.CONNECTION_TYPE_SOCKET);
    final Resource rootResource = new Resource(SYNTHETIC_APPS_NAMESPACE + "localhost", "", properties);
    final CoreServiceFacade facade = coreConnectionsManager.getConnectionById(coreConnectionId).getCoreServiceFacade();
    final List<Resource> resourceList = facade.discoverDirectChildren(rootResource, KnowledgeConstants.APPLICATION_URI);
    for (Resource app : resourceList) {
      coreConnectionsManager.identifyResource(app, coreConnectionId);
    }
    return resourceList;
  }



  @Override
  public void stopResource(Resource resource) throws ResourcesException {
    CoreConnection connection = coreConnectionsManager.getConnectionOfResource(resource);
    connection.getCoreServiceFacade().stopResource(resource);
  }

  @Override
  public void pauseResource(Resource resource) throws ResourcesException {
    CoreConnection connection = coreConnectionsManager.getConnectionOfResource(resource);
    connection.getCoreServiceFacade().pauseResource(resource);
  }

  @Override
  public void resumeResource(Resource resource) throws ResourcesException {
    CoreConnection connection = coreConnectionsManager.getConnectionOfResource(resource);
    connection.getCoreServiceFacade().resumeResource(resource);
  }

  /*
 =================================================================================
 ResourceEventsListener implementation
 =================================================================================
  */

  @Override
  public void processEvent(ResourceEvent event) throws Exception {
    final List<Resource> resources = event.getResources();
    switch (event.getType()) {
      case RESOURCES_ADDED:
        log.debug("Got new resources ({})", event.getResources().size());
        List<ResourcesTreeNode> treeNodes = new LinkedList<ResourcesTreeNode>();
        for (Resource r : resources) {
          treeNodes.add(addResource(r));
        }
        fireResourceAddedEvent(treeNodes);
        break;
      case RESOURCES_REMOVED:
        log.debug("Got new resources removed event");
        for (Resource r : resources) {
          final String uri = r.getUri();
          ResourcesTree appTree = applications.get(getApplicationUri(uri));
          appTree.removeResource(uri);
          fireResourceRemovedEvent(uri);
        }
        break;
    }

  }

/*
=================================================================================
private helpers
=================================================================================
*/

  private ResourcesTreeNode addResource(Resource r) {
    log.debug("Adding resource {} to resource tree", r);
    if (r.getTypeUri().equals(KnowledgeConstants.APPLICATION_URI)) {
      return addApplicationResource(r);
    } else {
      final String rUri = r.getUri();
      final String parentUri = rUri.substring(0, rUri.lastIndexOf("/"));
      final String appUri = getApplicationUri(rUri);
      if (!applications.containsKey(appUri)) {
        throw new IllegalArgumentException("Application of given resource haven't been registered with this " +
            "CoreResourcesService");
      }
      final ResourcesTree app = applications.get(appUri);
      return app.addResource(parentUri, r);
    }

  }

  private void fireResourceRemovedEvent(String resourceURI) {
    for (final ResourcesListener listener : resourcesListeners) {
      listener.resourceRemoved(resourceURI);
    }
  }


  private void fireResourceAddedEvent(List<ResourcesTreeNode> resources) {
    log.debug("Dispatching resources added event to listeners ({})", resourcesListeners.size());
    for (final ResourcesListener listener : resourcesListeners) {
      listener.resourcesAdded(resources);
    }
  }

  private String getApplicationUri(String resourceUri) {
    // skip sleshes in: whatever://host:port/app
    int index = 0;
    for (int i = 0; i < 4; i++) {
      index = resourceUri.indexOf('/', index);
      index++;
    }
    index--;
    if (index > 0) {
      return resourceUri.substring(0, index);
    } else {
      return resourceUri;
    }
  }

  private String getAppUriPrefix() {
    try {
      return SYNTHETIC_APPS_NAMESPACE + Inet4Address.getLocalHost().getHostName() + '/';
    } catch (UnknownHostException e) {
      log.error("Impossible - got UnknownHostException while getting localhost o_O", e);
    }
    return SYNTHETIC_APPS_NAMESPACE + "localhost/";
  }


/*
=================================================================================
initialization
=================================================================================
*/

  public void setUiFactory(UiFactory uiFactory) {
    this.uiFactory = uiFactory;
  }

  public void setCoreConnectionsManager(CoreConnectionsManager coreConnectionsManager) {
    this.coreConnectionsManager = coreConnectionsManager;
  }

  public void setResourcesListeners(List<ResourcesListener> resourcesListeners) {
    this.resourcesListeners = resourcesListeners;
  }
}
