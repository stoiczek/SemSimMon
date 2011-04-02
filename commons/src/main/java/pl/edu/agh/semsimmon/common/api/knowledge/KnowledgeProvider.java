package pl.edu.agh.semsimmon.common.api.knowledge;

public interface KnowledgeProvider {
  IKnowledge getDefaultKnowledgeSource();

  IKnowledge getKnowledgeSourceForURI(String uri);

  IKnowledge getKnowledgeSourceForMetircURI(String metricUri);
}
