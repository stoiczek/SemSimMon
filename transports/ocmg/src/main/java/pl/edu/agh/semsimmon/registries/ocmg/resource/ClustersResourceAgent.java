package pl.edu.agh.semsimmon.registries.ocmg.resource;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.apphierarchy.SiteTree;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author tkozak
 *         Created at 12:16 24-05-2010
 *         Discovers clusters (sites in OCMG nomenclature) from given application.
 */
public class ClustersResourceAgent extends AbstractResourceAgent {


  /**
   * {@inheritDoc}
   */
  @Override
  public List<Resource> discoverChildResources(Application application, Resource parent, String type) throws OcmgException {
    List<Resource> childResources = new LinkedList<Resource>();
    try {
      List<SiteTree> sites = application.getHierarchy().getSiteTree();
      for (SiteTree site : sites) {
        final String siteName = site.getSite().getCacheName();
        site.getSite().attach();
        final Resource resource = wrapResource(parent, type, siteName);
        resource.setProperty(ResourcePropertyNames.Cluster.ID, site.getSite().getCacheName());
        childResources.add(resource);
      }
      return childResources;
    } catch (ConnectionException e) {
      throw new OcmgException("Error connecting OCMG", e);
    } catch (MonitorException e) {
      throw new OcmgException("Error parsing OCMG response", e);
    }
  }

}
