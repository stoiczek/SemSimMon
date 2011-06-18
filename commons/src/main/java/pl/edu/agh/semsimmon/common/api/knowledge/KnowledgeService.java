package pl.edu.agh.semsimmon.common.api.knowledge;

import java.io.IOException;

/**
 *
 * Defines API used by GUI to perform knowledge tasks
 * <p/>
 * Created on: 2011-06-18 11:45
 * <br/>
 * @author Tadeusz Kozak
 */
public interface KnowledgeService {

  /**
   * Reloads ontology using given byte array as ontology source
   * @param ontologyContent gzipped byte array containing ontology source
   */
  void reloadOntology(byte ontologyContent[]) throws IOException;

}
