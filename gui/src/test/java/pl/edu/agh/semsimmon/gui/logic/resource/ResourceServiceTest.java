package pl.edu.agh.semsimmon.gui.logic.resource;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.CoreServiceFacade;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourceEvent;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.common.vo.core.resource.ResourceEventImpl;
import pl.edu.agh.semsimmon.gui.UiFactory;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnection;
import pl.edu.agh.semsimmon.gui.logic.connection.CoreConnectionsManager;
import pl.edu.agh.semsimmon.gui.logic.resource.tree.ResourcesTreeNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;


/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:11 10-06-2010
 */
public class ResourceServiceTest {

  private ResourcesServiceImpl resourcesService;

  @Mock
  private ResourcesListener mockListener;

  @Mock
  private UiFactory mockUiFactory;

  @Mock
  private CoreConnectionsManager mockConnectionsManger;

  @Mock
  private CoreConnection mockCoreConnection;

  @Mock
  private CoreServiceFacade mockCoreServiceFacade;

  @BeforeTest
  public void setup() {
    MockitoAnnotations.initMocks(this);
    resourcesService = new ResourcesServiceImpl();
    resourcesService.setResourcesListeners(Arrays.asList(mockListener));
    resourcesService.setUiFactory(mockUiFactory);
    resourcesService.setCoreConnectionsManager(mockConnectionsManger);
  }

  @Test
  public void addRemoveApplicationTest() {
    final String uri = resourcesService.addSyntheticApplication("test_app");
    assertEquals(uri, "semsimmon://localhost/test_app");
    final Resource r = new Resource(uri, KnowledgeConstants.APPLICATION_URI, Collections.<String, Object>emptyMap());
    resourcesService.removeResource(uri);
    InOrder order = inOrder(mockListener);

    order.verify(mockListener).resourcesAdded(argThat(new BaseMatcher<List<ResourcesTreeNode>>() {
      @Override
      public boolean matches(Object item) {
        if (!(item instanceof List)) {
          return false;
        }
        List list = (List) item;
        if (list.size() != 1) {
          return false;
        }
        ResourcesTreeNode listenedNode = (ResourcesTreeNode) list.get(0);
        return listenedNode.getResource().getUri().equals(uri)
            && listenedNode.getResource().getTypeUri().equals(KnowledgeConstants.APPLICATION_URI);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Some list of RTN");
      }
    }));

    order.verify(mockListener).resourceRemoved(uri);


  }

  @Test
  public void addClusterTest() {
    InOrder order = inOrder(mockListener);
    final String uri = resourcesService.addSyntheticApplication("test_app");
    assertEquals(uri, "semsimmon://localhost/test_app");
    final String clusterUri = resourcesService.addSyntheticCluster(uri, "cluster1");
    order.verify(mockListener, times(1)).resourcesAdded(argThat(new BaseMatcher<List<ResourcesTreeNode>>() {
      @Override
      public boolean matches(Object item) {
        if (!(item instanceof List)) {
          return false;
        }
        List list = (List) item;
        if (list.size() != 1) {
          return false;
        }
        ResourcesTreeNode listenedNode = (ResourcesTreeNode) list.get(0);
        return listenedNode.getResource().getUri().equals(uri)
            && listenedNode.getResource().getTypeUri().equals(KnowledgeConstants.APPLICATION_URI);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Some list of RTN");
      }
    }));

    order.verify(mockListener, times(1)).resourcesAdded(argThat(new BaseMatcher<List<ResourcesTreeNode>>() {
      @Override
      public boolean matches(Object item) {
        if (!(item instanceof List)) {
          return false;
        }
        List list = (List) item;
        if (list.size() != 1) {
          return false;
        }
        ResourcesTreeNode listenedNode = (ResourcesTreeNode) list.get(0);
        return listenedNode.getResource().getUri().equals(clusterUri)
            && listenedNode.getResource().getTypeUri().equals(KnowledgeConstants.CLUSTER_URI);
      }

      @Override
      public void describeTo(Description description) {

      }
    }));

  }

  @Test
  public void addJmxNodeTest() throws ResourceAlreadyRegisteredException {
    InOrder order = inOrder(mockListener, mockConnectionsManger, mockCoreConnection, mockCoreServiceFacade);
    final String uri = resourcesService.addSyntheticApplication("test_app");
    assertEquals(uri, "semsimmon://localhost/test_app");
    final String clusterUri = resourcesService.addSyntheticCluster(uri, "cluster1");
    String hostUri = "service:jmx:rmi://myhost:8084/jndi/rmi://myhost:8083/server1";
    String connectionId = UUID.randomUUID().toString();
    when(mockConnectionsManger.getConnectionById(connectionId)).thenReturn(mockCoreConnection);
    when(mockCoreConnection.getCoreServiceFacade()).thenReturn(mockCoreServiceFacade);
    String resourceUri = resourcesService.addJmxNode(clusterUri, hostUri, connectionId);
    assertEquals(resourceUri, clusterUri + "/myhost");
    order.verify(mockListener, times(2)).resourcesAdded(any(List.class));
    order.verify(mockConnectionsManger, times(1)).getConnectionById(connectionId);
    order.verify(mockCoreConnection).getCoreServiceFacade();
    order.verify(mockCoreServiceFacade).registerResource(eq(resourceUri), eq(KnowledgeConstants.NODE_URI), anyMap());
  }

  @Test
  public void showAddJmxResourceWizardTest() {
    resourcesService.showAddJmxResourceWizard();
    verify(mockUiFactory).createAddJmxResourceDialog();
  }

  @Test
  public void addResourceEventHandlingTest() throws Exception {
    final String uri = resourcesService.addSyntheticApplication("test_app");
    final String clusterUri = resourcesService.addSyntheticCluster(uri, "cluster1");
    final String hostUri = "service:jmx:rmi://myhost:8084/jndi/rmi://myhost:8083/server1";
    String connectionId = UUID.randomUUID().toString();
    when(mockConnectionsManger.getConnectionById(connectionId)).thenReturn(mockCoreConnection);
    when(mockCoreConnection.getCoreServiceFacade()).thenReturn(mockCoreServiceFacade);
    final String resourceUri = resourcesService.addJmxNode(clusterUri, hostUri, connectionId);
    Resource resource = new Resource(resourceUri, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    ResourceEvent addResEvent = new ResourceEventImpl(ResourceEvent.Type.RESOURCES_ADDED, resource);
    resourcesService.processEvent(addResEvent);

    verify(mockListener).resourcesAdded(argThat(new BaseMatcher<List<ResourcesTreeNode>>() {
      @Override
      public boolean matches(Object item) {
        if (!(item instanceof List)) {
          return false;
        }
        List list = (List) item;
        if (list.size() != 1) {
          return false;
        }
        ResourcesTreeNode listenedNode = (ResourcesTreeNode) list.get(0);
        return listenedNode.getResource().getUri().equals(resourceUri)
            && listenedNode.getResource().getTypeUri().equals(KnowledgeConstants.NODE_URI)
            && listenedNode.getParent().getResource().getUri().equals(clusterUri)
            && listenedNode.getParent().getResource().getTypeUri().equals(KnowledgeConstants.CLUSTER_URI);
      }

      @Override
      public void describeTo(Description description) {

      }
    }));
  }

  @Test
  public void removeResourceEventHandlingTest() throws Exception {
    final String uri = resourcesService.addSyntheticApplication("test_app");
    final String clusterUri = resourcesService.addSyntheticCluster(uri, "cluster1");
    final String hostUri = "service:jmx:rmi://myhost:8084/jndi/rmi://myhost:8083/server1";
    String connectionId = UUID.randomUUID().toString();
    when(mockConnectionsManger.getConnectionById(connectionId)).thenReturn(mockCoreConnection);
    when(mockCoreConnection.getCoreServiceFacade()).thenReturn(mockCoreServiceFacade);
    final String resourceUri = resourcesService.addJmxNode(clusterUri, hostUri, connectionId);
    Resource resource = new Resource(resourceUri, KnowledgeConstants.NODE_URI, Collections.<String, Object>emptyMap());
    ResourceEvent addResEvent = new ResourceEventImpl(ResourceEvent.Type.RESOURCES_ADDED, resource);
    resourcesService.processEvent(addResEvent);
    ResourceEvent removeResEvent = new ResourceEventImpl(ResourceEvent.Type.RESOURCES_REMOVED, resource);
    resourcesService.processEvent(removeResEvent);
    verify(mockListener).resourceRemoved(resourceUri);
  }
}
