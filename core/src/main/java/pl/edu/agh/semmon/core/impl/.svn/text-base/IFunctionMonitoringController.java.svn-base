package pl.edu.agh.semmon.core.impl;

import pl.edu.agh.semmon.common.vo.core.resource.Resource;

/**
 * @author tkozak
 *         Created at 12:08 28-05-2010
 *         Manges functions monitoring.
 */
public interface IFunctionMonitoringController {

  /**
   * Adds given function name to set of interesting functions in context of given resource.
   * Interesting funtions are functions which might be monitored by user and thus require collecting statistical
   * usage data.
   *
   * @param resource     context of interesting functions - all calls whithin given resource and it's child resources
   *                     will be monitored.
   * @param functionName name of function to be monitored.
   */
  void addFunctionOfInterest(Resource resource, String functionName);

  /**
   * TODO
   * Checks whether given function is in iterest of resource scope
   *
   * @param resource     resource
   * @param functionName fun
   * @return true/false
   */
  boolean functionInterestingForResource(Resource resource, String functionName);
}
