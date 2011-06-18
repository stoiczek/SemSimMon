package pl.edu.agh.semsimmon.knowledge.impl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.knowledge.IKnowledge;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.knowledge.ontology.IOntModelProvider;

import java.io.IOException;
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

  @Override
  public void reloadOntology(byte[] ontologyPayload) throws IOException {
    ontModelProvider.reloadOntology(ontologyPayload);
  }

  @Override
  public List<String> getChildrenResourceTypes(String type) {
    return selectObjectsForPropertyOfSubject(type, KnowledgeConstants.HAS_RESOURCE_PROPERTY_URI);
  }

  private List<String> selectObjectsForPropertyOfSubject(String subject, String property) {
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

  public List<String> getUsedCapabilities(String metricURI) {
    return selectObjectsForPropertyOfSubject(metricURI, KnowledgeConstants.USES_CAP_PROPERTY_URI);
  }



  @SuppressWarnings({"UnusedDeclaration"})
  public void setOntModelProvider(IOntModelProvider ontModelProvider) {
    this.ontModelProvider = ontModelProvider;
  }


}
