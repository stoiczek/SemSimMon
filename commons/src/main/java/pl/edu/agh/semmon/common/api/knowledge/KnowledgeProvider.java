package pl.edu.agh.semmon.common.api.knowledge;

import pl.edu.agh.semmon.common.api.knowledge.IKnowledge;

public interface KnowledgeProvider {
  IKnowledge getDefaultKnowledgeSource();

  IKnowledge getKnowledgeSourceForURI(String uri);

  IKnowledge getKnowledgeSourceForMetircURI(String metricUri);
}
