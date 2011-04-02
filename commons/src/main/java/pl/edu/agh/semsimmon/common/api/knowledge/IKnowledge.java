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

  List<String> getUsedCapabilities(String metricURI);

  boolean isCustomMetric(String uri);

  String getClassNameForCustomMetric(String uri);

  List<String> getAllAvailableMetrics();

  List<String> getMetricsForResourceType(String type);

  List<String> getMetricsSuggestedToStart(String metricURI, String resourceURI);

  List<String> getMetricsUsingCapabilityForResourceType(String resourceURI,
                                                        String capabilityURI);

}