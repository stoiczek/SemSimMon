package pl.edu.agh.semsimmon.knowledge.ontology;

import com.hp.hpl.jena.ontology.OntModel;

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
}
