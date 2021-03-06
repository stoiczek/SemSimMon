package pl.edu.agh.semsimmon.knowledge.ontology;

import com.hp.hpl.jena.ontology.OntModel;

import java.io.IOException;

/**
 * Provides Jena OntModel
 *
 * @author koperek
 */
public interface IOntModelProvider {

  /**
   * @return
   */
  OntModel getOntModel();

  void reloadOntology(byte ontologyPayload[]) throws IOException;


}
