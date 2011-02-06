package pl.edu.agh.semmon.knowledge.impl;

import pl.edu.agh.semmon.common.api.knowledge.IKnowledge;
import pl.edu.agh.semmon.common.api.knowledge.KnowledgeProvider;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeProviderImpl implements KnowledgeProvider {

  private Map<String, IKnowledge> knowledgeSources = new HashMap<String, IKnowledge>();


  @Override
  public IKnowledge getDefaultKnowledgeSource() {
    return knowledgeSources.values().iterator().next();
  }

  @Override
  public IKnowledge getKnowledgeSourceForMetircURI(String metricUri) {
    IKnowledge retVal = null;
    int index = metricUri.indexOf('#');

    if (index > 0) {
      String uri = metricUri.substring(0, index);
      retVal = knowledgeSources.get(uri);
    }

    return retVal;
  }

  @Override
  public IKnowledge getKnowledgeSourceForURI(String uri) {
    return knowledgeSources.get(uri);
  }

}
