package pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.BaseAddResourceWizardCtrl;
import pl.edu.agh.semsimmon.gui.controllers.wizard.resource.add.ConfigMonitorPage;

import java.io.IOException;

/**
 * Controls process of adding new JMX resources.
 *
 * @author tkozak
 *         Created at 17:48 05-06-2010
 */
public class AddJMXResourceWizardController extends BaseAddResourceWizardCtrl {

  private static final Logger log = LoggerFactory.getLogger(AddJMXResourceWizardController.class);

  /**
   * {@inheritDoc}
   */
  @Override
  protected void wizardFinished() throws IOException {
    log.debug("Add Jmx resource wizard finished");
    final ConfigMonitorPage monitorPage = (ConfigMonitorPage) pages.get(0);
    final ConfigHierarchyPage hierarchyPage = (ConfigHierarchyPage) pages.get(1);
    final ConfigJmxNodes nodesPage = (ConfigJmxNodes) pages.get(2);
    final String connectionId = initConnection(monitorPage);
    final String applicationId = resourcesService.addSyntheticApplication(hierarchyPage.getApplication());
    final String clusterId = resourcesService.addSyntheticCluster(applicationId, hierarchyPage.getClusterId());
    for (String nodeService : nodesPage.getNodeUris()) {
      try {
        log.debug("Adding jmx node with URL: {}", nodeService);
        resourcesService.addJmxNode(clusterId, nodeService, connectionId);
      } catch (ResourceAlreadyRegisteredException e) {
        log.warn("Got resource already registered exception", e);
      }
    }

  }

}
