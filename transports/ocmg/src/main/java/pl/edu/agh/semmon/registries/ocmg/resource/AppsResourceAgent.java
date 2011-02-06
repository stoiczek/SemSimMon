package pl.edu.agh.semmon.registries.ocmg.resource;

import org.balticgrid.ocmg.objects.Application;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semmon.common.vo.core.resource.Resource;
import pl.edu.agh.semmon.registries.ocmg.OcmgConnection;
import pl.edu.agh.semmon.registries.ocmg.OcmgException;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:21 29-08-2010
 */
public class AppsResourceAgent extends AbstractResourceAgent {

  @Override
  public List<Resource> discoverChildResources(Application ocmgApplication, Resource parent, String type) throws OcmgException {
    final OcmgConnection connection = new OcmgConnection(parent);
    connection.connect();
    final List<String > appNames = connection.getApplications();
    final List<Resource> applications = new LinkedList<Resource>();
    for(final String appName : appNames) {
      final Resource application = wrapResource(parent, KnowledgeConstants.APPLICATION_URI, appName);
      application.setProperty(ResourcePropertyNames.Application.NAME, appName);
      applications.add(application);
    }
    return applications;
  }
}
