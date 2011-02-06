package pl.edu.agh.semmon.knowledge.impl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.common.api.knowledge.IKnowledge;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semmon.knowledge.ontology.IOntModelProvider;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class KnowledgeImpl implements IKnowledge {

  private static final Logger log = LoggerFactory.getLogger(KnowledgeImpl.class.getName());


  private IOntModelProvider ontModelProvider;


  @Override
  public String getOntologyURI() {
    return KnowledgeConstants.ONTOLOGY_URI;
  }

  @Override
  public List<String> getCapabilitiesOfResourceType(String type) {
    return selectObjectsForPropertyOfSubject(type, KnowledgeConstants.HAS_RESOURCE_CAP_PROPERTY_URI);
  }

  private List<String> selectObjectsForPropertyOfSubject(String subject,
                                                         String property) {
    List<String> retVal = new LinkedList<String>();
    OntModel model = ontModelProvider.getOntModel();

    Resource resource = model.getResource(subject);
    Property propertyInstance = model.getProperty(property);
    StmtIterator statementIterator = model.listStatements(resource, propertyInstance, (RDFNode) null);
    while (statementIterator.hasNext()) {
      final Statement statement = statementIterator.nextStatement();
      String resourceTypeURI = statement.getObject().asNode().getURI();
      retVal.add(resourceTypeURI);
    }

    return retVal;
  }

  @Override
  public List<String> getUsedCapabilities(String metricURI) {
    return selectObjectsForPropertyOfSubject(metricURI, KnowledgeConstants.USES_CAP_PROPERTY_URI);
  }

  @Override
  public List<String> getChildrenResourceTypes(String type) {
    return selectObjectsForPropertyOfSubject(type, KnowledgeConstants.HAS_RESOURCE_PROPERTY_URI);
  }

  @Override
  public String getClassNameForCustomMetric(String uri) {
    // there should be only one class ... in case there are more - we take
    // the first one ;)
    List<String> clazz = selectObjectsForPropertyOfSubject(uri, KnowledgeConstants.HAS_CUSTOM_CLASS_PROPERTY_URI);
    return clazz.get(0);
  }

  @Override
  public boolean isCustomMetric(String uri) {
    List<String> classes = selectObjectsForPropertyOfSubject(uri, KnowledgeConstants.HAS_CUSTOM_CLASS_PROPERTY_URI);
    return classes.size() > 0;
  }

  @Override
  public List<String> getAllAvailableMetrics() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getMetricsForResourceType(String type) {
    Set<String> capabilities = new HashSet<String>();

    capabilities.addAll(selectObjectsForPropertyOfSubject(type, KnowledgeConstants.HAS_RESOURCE_CAP_PROPERTY_URI));

    return getMetricsUsingCapabilitiesForResourceType(type, capabilities);
  }

  @Override
  public List<String> getMetricsSuggestedToStart(String metricURI,
                                                 String resourceURI) {
    throw new RuntimeException("Method unimplemented!");
  }

  private List<String> getMetricsUsingCapabilitiesForResourceType(
      String resourceType, Set<String> capabilities) {

    Set<String> allMetrics = new HashSet<String>();
    for (String capability : capabilities) {
      List<String> metrics = selectObjectsForPropertyOfSubject(capability,
          KnowledgeConstants.CAP_IS_USED_BY_PROPERTY_URI);
      allMetrics.addAll(metrics);
    }

    List<String> retVal = new LinkedList<String>();

    for (String metric : allMetrics) {
      List<String> usedCapabilities = getUsedCapabilities(metric);
      if (capabilities.containsAll(usedCapabilities)) {
        retVal.add(metric);
      }
    }
    return retVal;
  }

  @Override
  public List<String> getMetricsUsingCapabilityForResourceType(
      String resourceType, String capabilityURI) {

    Set<String> capabilities = new HashSet<String>();

    capabilities.add(capabilityURI);

    return getMetricsUsingCapabilitiesForResourceType(resourceType,
        capabilities);
  }


  @SuppressWarnings({"UnusedDeclaration"})
  public void setOntModelProvider(IOntModelProvider ontModelProvider) {
    this.ontModelProvider = ontModelProvider;
  }


}
