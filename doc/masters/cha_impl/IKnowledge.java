package pl.edu.agh.semsimmon.common.api.knowledge;

import java.util.List;

public interface IKnowledge {

  String getOntologyURI();

  List<String> getChildrenResourceTypes(String type);

  List<String> getCapabilitiesOfResourceType(String type);

}