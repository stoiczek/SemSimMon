package pl.edu.agh.semsimmon.gui.controllers.tab;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.json.JSON;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.content.ButtonData;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.api.resource.ResourceState;
import pl.edu.agh.semsimmon.common.exception.MeasurementException;
import pl.edu.agh.semsimmon.common.exception.ResourcesException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semsimmon.gui.logic.metric.MeasurementService;
import pl.edu.agh.semsimmon.gui.logic.resource.ResourcesListener;
import pl.edu.agh.semsimmon.gui.logic.resource.ResourcesService;
import pl.edu.agh.semsimmon.gui.logic.resource.tree.ResourcesTreeNode;
import pl.edu.agh.semsimmon.gui.util.TreeBranchContainer;
import pl.edu.agh.semsimmon.gui.util.TreeNodeContainer;

import java.io.IOException;
import java.util.*;

/**
 * Controller responsible for handling actions related to resources tab.
 *
 * @author tkozak
 *         Created at 19:54 04-06-2010
 */
public class ResourcesTabController extends BaseTabController implements ResourcesListener {

  private static final Logger log = LoggerFactory.getLogger(ResourcesTabController.class.getName());

  private static final String RESUME_BUTTON_KEY = "tabs.resources.resume";
  private static final String PAUSE_BUTTON_KEY = "tabs.resources.pause";
  private static final String RES_TYPE_KEY = "rp.type";


  private ResourcesService resourcesService;

  private MeasurementService measurementService;

  @Override
  protected Class getBindableClass() {
    return ResourcesTabController.class;
  }

  @BXML
  private Menu.Item addJmxResourceButton;

  @BXML
  private Menu.Item addOcmgResourceButton;

  @BXML
  private PushButton addResourceButton;

  @BXML
  private PushButton addMeasurementButton;

  @BXML
  private PushButton pauseResumeButton;

  @BXML
  private PushButton stopButton;

  @BXML
  private PushButton refreshCapabilitiesButton;

  @BXML
  private PushButton removeResourceButton;

  @BXML
  private TreeView resourcesTree;

  @BXML
  private Label noResourcesLabel;

  @BXML
  private TablePane.Column treeColumn;

  @BXML
  private Border treeBorder;

  @BXML
  private TablePane resourceAttributes;

  @BXML
  private TablePane resourceCapabilities;

  private TreeBranchContainer treeData = new TreeBranchContainer();

  private Map<String, Object> nodesMap = new HashMap<String, Object>();

  private Map<String, org.springframework.core.io.Resource> iconsMap;

  private Set<String> addedResources = new HashSet<String>();

  private Resource currentlySelectedResource;

  private boolean capabilitiesRefreshing = false;

  @ButtonAction
  private void addJmxResourceButtonPressed() {
    resourcesService.showAddJmxResourceWizard();
  }

  @ButtonAction
  private void addOcmgResourceButtonPressed() {
    resourcesService.showAddOcmgResourceWizard();
  }


  @ButtonAction
  private void addMeasurementButtonPressed() throws IOException {
    final Object node = resourcesTree.getSelectedNode();
    Resource resource;
    if (node instanceof TreeBranchContainer) {
      resource = (Resource) ((TreeBranchContainer) node).getContent();
    } else if (node instanceof TreeNodeContainer) {
      resource = (Resource) ((TreeNodeContainer) node).getContent();
    } else {
      log.error("Got impossible = given tree node is from invalid class: {}", node.getClass().getName());
      return;
    }
    resourcesService.showAddMeasurementDialog(resource);
  }

  @ButtonAction
  private void pauseResumeButtonPressed() throws ResourcesException {
    log.debug("Pause/Resume button pressed");
    ResourceState state = (ResourceState) currentlySelectedResource.getProperty(ResourcePropertyNames.RESOURCE_STATE);
    if (state == null) {
      log.error("Cannot pause/resume as state property isn't configured for resource {}", currentlySelectedResource);
    } else {
      String buttonText;
      if (state == ResourceState.PAUSED) {
        resourcesService.resumeResource(currentlySelectedResource);
        buttonText = JSON.get(resources, PAUSE_BUTTON_KEY);
      } else {
        resourcesService.pauseResource(currentlySelectedResource);
        buttonText = JSON.get(resources, RESUME_BUTTON_KEY);
      }
      ButtonData data = (ButtonData) pauseResumeButton.getButtonData();
      data.setText(buttonText);
    }
  }

  @ButtonAction
  private void stopButtonPressed() throws ResourcesException {
    log.debug("Stop button pressed");
    resourcesService.stopResource(currentlySelectedResource);
  }

  @ButtonAction
  private void removeButtonPressed() {
    log.debug("Remove resource button pressed");
    resourcesService.removeResource(currentlySelectedResource.getUri());
  }

  @ButtonAction(type = ButtonAction.Type.BACKGROUND)
  private void refreshCapabilitiesButtonPressed() throws MeasurementException {
    log.debug("Refreshing resource capability values");
    synchronized (this) {
      if (capabilitiesRefreshing) {
        return;
      }
      capabilitiesRefreshing = true;
    }
    try {
      Object node = resourcesTree.getSelectedNode();
      Resource resource;
      if (node instanceof TreeBranchContainer) {
        TreeBranchContainer container = (TreeBranchContainer) node;
        resource = (Resource) container.getContent();
      } else if (node instanceof TreeNodeContainer) {
        resource = (Resource) ((TreeNodeContainer) node).getContent();
      } else {
        log.warn("Got invalid selected node object: " + node);
        return;
      }

      final Map<String, String> values = measurementService.getAllCapabilities(resource);
      final TablePane.RowSequence capRows = resourceCapabilities.getRows();
      capRows.remove(0, capRows.getLength());
      for (Map.Entry<String, String> entry : values.entrySet()) {
        TablePane.Row row = new TablePane.Row();
        row.add(new Label(getLabelForURI(entry.getKey())));
        row.add(new Label(entry.getValue()));
        capRows.add(row);
      }
      resourceCapabilities.repaint(true);
    } catch (IllegalArgumentException e) {
      log.warn("Got synthetic resource");
    } finally {
      capabilitiesRefreshing = false;
    }
  }

  @Override
  protected void postBinding() throws IOException {
    log.debug("Bind ResourceTabController.");
    resourcesTree.setTreeData(treeData);
    treeBorder.getComponentMouseListeners().add(new TreeColumnResizingMouseListener());
    resourcesTree.getTreeViewSelectionListeners().add(new ResourcesSelectionListener());
  }

  /*
  =================================================================================
  ResourcesListener implementation
  =================================================================================
  */

  /**
   * {@inheritDoc}
   */
  @Override
  public void resourcesAdded(List<ResourcesTreeNode> resourceNodes) {
    log.debug("Got new resources ({})", resourceNodes.size());
    boolean treeCreated = false;
    if (treeData.isEmpty()) {
      treeCreated = true;
    }
    for (ResourcesTreeNode node : resourceNodes) {
      try {
        final Resource resource = node.getResource();
        final String resourceUri = resource.getUri();
        if (addedResources.contains(resourceUri)) {
          continue;
        }
        addedResources.add(resourceUri);
        log.debug("Adding resource {}", resourceUri);
        if (resource.getTypeUri().equals(KnowledgeConstants.APPLICATION_URI)) {
          addApplicationNode(node);
        } else {
          String parentUri = node.getParent().getResource().getUri();
          Object parentObject = nodesMap.get(parentUri);
          if (parentObject == null) {
            log.warn("Parent with URI: " + parentUri + " not found in registered nodes");
            continue;
          }
          TreeBranchContainer<Resource> parent;
          if (parentObject instanceof TreeBranchContainer) {
            //noinspection unchecked
            parent = (TreeBranchContainer<Resource>) parentObject;
          } else {
            parent = makeBranchFromNode(parentObject);
          }

          addResourceNode(parent, node);
        }
      } catch (IOException e) {
        log.error("Error adding resource", e);
      }
    }

    if (treeCreated) {
      log.debug("Displaying tree");
      noResourcesLabel.setVisible(false);
      resourcesTree.setVisible(true);
    }
    resourcesTree.setTreeData(treeData);
    resourcesTree.getParent().repaint(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resourceRemoved(String resourceUri) {
    removeNode((TreeNode) nodesMap.get(resourceUri));
    resourcesTree.getParent().repaint(true);
  }


/*
=================================================================================
private utilities
=================================================================================
*/


  private TreeBranchContainer<Resource> makeBranchFromNode(Object parentObject) {
    TreeBranchContainer<Resource> parent;
    parent = new TreeBranchContainer<Resource>();
    @SuppressWarnings({"unchecked"})
    TreeNodeContainer<Resource> parentNode = (TreeNodeContainer<Resource>) parentObject;
    parent.setContent(parentNode.getContent());
    parent.setText(parentNode.getText());
    parent.setIcon(parentNode.getIcon());
    TreeBranch grandpa = parentNode.getParent();
    if (grandpa != null) {
      grandpa.remove(parentNode);
      grandpa.add(parent);
    }
    nodesMap.put(parent.getContent().getUri(), parent);
    return parent;
  }


  private void addResourceNode(TreeBranchContainer<Resource> parent, ResourcesTreeNode node) throws IOException {
    log.debug("Adding resource node");
    if (node.getChildren().isEmpty()) {
      TreeNodeContainer<Resource> treeNode = new TreeNodeContainer<Resource>();
      treeNode.setContent(node.getResource());
      String label = getNodeLabel(node);
      if (label == null || label.isEmpty()) {
        log.warn("Cannot get name of resource");
        label = "name_unknown";
      }
      treeNode.setText(label);
      if (iconsMap.containsKey(node.getResource().getTypeUri())) {
        treeNode.setIcon(iconsMap.get(node.getResource().getTypeUri()).getURL());
      }
      parent.add(treeNode);
      nodesMap.put(node.getResource().getUri(), treeNode);
    } else {
      TreeBranchContainer<Resource> treeBranch = new TreeBranchContainer<Resource>();
      treeBranch.setContent(node.getResource());
      treeBranch.setText(getNodeLabel(node));
      parent.add(treeBranch);
      nodesMap.put(node.getResource().getUri(), treeBranch);
      for (ResourcesTreeNode child : node.getChildren()) {
        addResourceNode(treeBranch, child);
      }
    }
    log.debug("Resource node added");
    resourcesTree.repaint();
  }


  private String getNodeLabel(ResourcesTreeNode node) {
    String uri = node.getResource().getUri();
    if (uri.endsWith("/")) {
      uri = uri.substring(0, uri.length() - 1);
    }
    return uri.substring(uri.lastIndexOf('/') + 1, uri.length());
  }

  private void addApplicationNode(ResourcesTreeNode node) throws IOException {
    log.debug("Adding application node");
    TreeBranchContainer<Resource> appContainer = new TreeBranchContainer<Resource>();
    appContainer.setContent(node.getResource());
    appContainer.setText(getNodeLabel(node));
    appContainer.setIcon(iconsMap.get(KnowledgeConstants.APPLICATION_URI).getURL());
    treeData.add(appContainer);
    nodesMap.put(node.getResource().getUri(), appContainer);
    for (ResourcesTreeNode child : node.getChildren()) {
      addResourceNode(appContainer, child);
    }
    log.debug("Application ndoe added.");

  }

  private void removeNode(TreeNode node) {
    if (node instanceof TreeBranchContainer) {
      TreeBranchContainer branch = (TreeBranchContainer) node;
      for (TreeNode child : branch) {
        removeNode(child);
      }
    }
    String uri = "";
    if (node instanceof TreeBranchContainer) {
      TreeBranchContainer<Resource> branchContainer = (TreeBranchContainer<Resource>) node;
      uri = branchContainer.getContent().getUri();
      if (branchContainer.getParent() != null) {
        branchContainer.getParent().remove(branchContainer);
      }
    } else if (node instanceof TreeNodeContainer) {
      TreeNodeContainer<Resource> nodeContainer = (TreeNodeContainer<Resource>) node;
      uri = nodeContainer.getContent().getUri();
      if (nodeContainer.getParent() != null) {
        nodeContainer.getParent().remove(nodeContainer);
      }
    }
    nodesMap.remove(uri);
  }

  private void updateResourceDetails(Resource resource) {
    log.debug("Updating resource details of resource: {}", resource);
    final TablePane.RowSequence attrRows = resourceAttributes.getRows();
    attrRows.remove(0, attrRows.getLength());
    {
      TablePane.Row row = new TablePane.Row();
      row.add(new Label(JSON.<String>get(resources, RES_TYPE_KEY)));
      row.add(new Label(getLabelForURI(resource.getTypeUri())));
      attrRows.add(row);

    }
    int i = 0;
    for (Map.Entry<String, Object> entry : resource.getProperties().entrySet()) {
      if (!entry.getKey().startsWith(ResourcePropertyNames.RESOURCE_PROPERTY_PREFIX)) {
        continue;
      }
      TablePane.Row row = new TablePane.Row();
      row.add(new Label(JSON.<String>get(resources, entry.getKey())));
      row.add(new Label(entry.getValue().toString()));
      attrRows.add(row);
      i++;
    }
    resourceAttributes.repaint();
    final TablePane.RowSequence capRows = resourceCapabilities.getRows();
    capRows.remove(0, capRows.getLength());
    int j = 0;
    try {
      List<String> capabilitieName = measurementService.getAllCapabilitiesNames(resource);
      for (String capName : capabilitieName) {
        TablePane.Row row = new TablePane.Row();
        row.add(new Label(getLabelForURI(capName)));
        row.add(new Label("NaN"));
        capRows.add(row);
        j++;
      }
    } catch (IllegalArgumentException e) {
      log.warn("Got IAE while getting attribute names - given resource is synthetic.");
    }
    resourceCapabilities.repaint();
    log.debug("Details updated. Added {} attributes and {} capabilities.", new Object[]{i, j});
  }


/*
=================================================================================
IoC setup
=================================================================================
*/

  public void setResourcesService(ResourcesService resourcesService) {
    this.resourcesService = resourcesService;
  }


  public void setIconsMap(Map<String, org.springframework.core.io.Resource> iconsMap) {
    this.iconsMap = iconsMap;
  }

  public void setMeasurementService(MeasurementService measurementService) {
    this.measurementService = measurementService;
  }

  /*
  =================================================================================
  Private classes
  =================================================================================
  */


  private class TreeColumnResizingMouseListener implements ComponentMouseListener {

    @Override
    public boolean mouseMove(Component component, int x, int y) {
      return true;
    }

    @Override
    public void mouseOver(Component component) {
      log.debug("Mouse over tree border - expanding");
      treeColumn.setWidth(350);
    }

    @Override
    public void mouseOut(Component component) {
      log.debug("Mouse out from tree border - shrinking");
      treeColumn.setWidth(150);
    }

  }

  private class ResourcesSelectionListener implements TreeViewSelectionListener {

    @Override
    public void selectedNodeChanged(TreeView treeView, Object previousSelectedNode) {

    }

    @Override
    public void selectedPathAdded(TreeView treeView, Sequence.Tree.Path path) {

    }

    @Override
    public void selectedPathRemoved(TreeView treeView, Sequence.Tree.Path path) {

    }

    @Override
    public void selectedPathsChanged(TreeView treeView, Sequence<Sequence.Tree.Path> previousSelectedPaths) {
      log.debug("Got new selected resource");
      Object node = treeView.getSelectedNode();
      Resource resource;
      if (node instanceof TreeBranchContainer) {
        TreeBranchContainer container = (TreeBranchContainer) node;
        resource = (Resource) container.getContent();
      } else if (node instanceof TreeNodeContainer) {
        resource = (Resource) ((TreeNodeContainer) node).getContent();
      } else {
        log.warn("Got invalid selected node object: " + node);
        return;
      }
      updateResourceDetails(resource);
      currentlySelectedResource = resource;
      ResourceState state = (ResourceState) resource.getProperty(ResourcePropertyNames.RESOURCE_STATE);
      if (state == null) {
        log.error("State for given resource {} not configured. Stop/Pause/Resume facilities will be disabled");
        pauseResumeButton.setEnabled(false);
        stopButton.setEnabled(false);
      } else {
        if ((Boolean) resource.getProperty(ResourcePropertyNames.RESOURCE_PAUSEABLE_PN)) {
          ButtonData buttonData = (ButtonData) pauseResumeButton.getButtonData();
          pauseResumeButton.setEnabled(true);
          switch (state) {
            case PAUSED:
              buttonData.setText(JSON.<String>get(resources, RESUME_BUTTON_KEY));
              break;
            case RUNNING:
              buttonData.setText(JSON.<String>get(resources, PAUSE_BUTTON_KEY));
              break;
            default:
              pauseResumeButton.setEnabled(false);
          }
        }
        if ((Boolean) resource.getProperty(ResourcePropertyNames.RESOURCE_STOPPABLE_PN) &&
            state != ResourceState.STOPPED) {
          stopButton.setEnabled(true);
        } else {
          stopButton.setEnabled(false);
        }
      }
      removeResourceButton.setEnabled(true);
      addMeasurementButton.setEnabled(true);
    }

  }
}
