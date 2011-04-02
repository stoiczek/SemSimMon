package pl.edu.agh.semsimmon.common.api.knowledge;

import java.util.List;

/**
 * Knowledge service interface - facade for this component
 *
 * @author koperek
 */
public interface IKnowledge {

  String getOntologyURI();

  List<String> getChildrenResourceTypes(String type);

  List<String> getCapabilitiesOfResourceType(String type);


}