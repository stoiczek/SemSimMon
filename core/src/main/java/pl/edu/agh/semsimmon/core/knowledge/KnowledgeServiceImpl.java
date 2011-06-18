/*
 * Copyright (C) SayMama Ltd 2011
 *
 * All rights reserved. Any use, copying, modification, distribution and selling
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package pl.edu.agh.semsimmon.core.knowledge;

import pl.edu.agh.semsimmon.common.api.knowledge.IKnowledge;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeService;

import java.io.IOException;

/**
 * @author Tadeusz Kozak
 * @TODO description
 * <p/>
 * Created on: 2011-06-18 11:58
 * <br/>
 * <a href="http://www.saymama.com">http://www.saymama.com</a>
 */
public class KnowledgeServiceImpl implements KnowledgeService {

  private IKnowledge knowledge;

  @Override
  public void reloadOntology(byte[] ontologyContent) throws IOException {
    knowledge.reloadOntology(ontologyContent);
  }

  public void setKnowledge(IKnowledge knowledge) {
    this.knowledge = knowledge;
  }
}
