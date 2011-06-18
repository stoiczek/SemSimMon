package pl.edu.agh.semsimmon.knowledge.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeUtils;
import pl.edu.agh.semsimmon.knowledge.impl.KnowledgeImpl;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Initializes and provides ontology model.
 *
 * @author koperek
 * @author tkozak
 * @author kupisz
 */
public class OntModelProviderImpl implements IOntModelProvider {

  private static final Logger log = LoggerFactory.getLogger(KnowledgeImpl.class.getName());

  private OntModel ontModel;

  private Resource defaultOntology;

  @Override
  public OntModel getOntModel() {
    log.debug("Getting ontology model");
    if (ontModel == null) {
      throw new IllegalStateException("Cannot return ontology model, as it hasn't been initialized");
    }
    return ontModel;
  }

  @Override
  public void reloadOntology(byte[] ontologyPayload) throws IOException {
    log.debug("Reloading ontology.");
    initializeOntologyFromInput(KnowledgeUtils.deserializeOntology(ontologyPayload));
  }

  @PostConstruct
  public void init() throws IOException {
    log.debug("Initializing ontology model provider.");
    InputStream inputStream = defaultOntology.getInputStream();
    initializeOntologyFromInput(inputStream);

  }

  private void initializeOntologyFromInput(InputStream inputStream) {
    ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
    ontModel = ModelFactory.createOntologyModel(
        PelletReasonerFactory.THE_SPEC, ontModel);
    log.debug("Reading ontology");
    ontModel.read(inputStream, "");
  }

  public void setDefaultOntology(Resource defaultOntology) {
    this.defaultOntology = defaultOntology;
  }
}
