package pl.edu.agh.semsimmon.gui.logic.resource;

import pl.edu.agh.semsimmon.common.exception.ResourceAlreadyRegisteredException;
import pl.edu.agh.semsimmon.common.exception.ResourcesException;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.io.IOException;
import java.util.List;

/**
 * Provides services related to resources.
 *
 * @author tkozak
 *         Created at 20:59 04-06-2010
 */
public interface ResourcesService {

  /**
   * Shows wizard which allows user to add JMX resources.
   */
  void showAddJmxResourceWizard();

  /**
   * Shows wizard allowing user to add OCM-G resources.
   */
  void showAddOcmgResourceWizard();

  /**
   * Creates and shows Add measurement dialog, configuring it initially, to add measurement to given resource
   *
   * @param resource resource to configure add measurement dialog with
   */
  void showAddMeasurementDialog(Resource resource) throws IOException;

  /**
   * Adds synthetic (not being real resource - e.g. in JMX) application.
   *
   * @param appLabel label of application;
   * @return generated application id.
   */
  String addSyntheticApplication(String appLabel);

  /**
   * @param application
   */
  void addOcmgApplication(Resource application) throws ResourceAlreadyRegisteredException;

  /**
   * Adds synthetic (not being real resource - e.g. in JMX) cluster as child of given application.
   *
   * @param appId        id of parent application.
   * @param clusterLabel label of cluster to be created.
   * @return created cluster's id.
   */
  String addSyntheticCluster(String appId, String clusterLabel);

  /**
   * Adds node to measurements tree.
   *
   * @param clusterId    id of parent cluster (cluster to which this node will belong)
   * @param serviceUri   uri of node's JMX agent
   * @param connectionId id of connection used to extract node's data (child resources,capability values)
   * @return nodes id.
   */
  String addJmxNode(String clusterId, String serviceUri, String connectionId) throws ResourceAlreadyRegisteredException;

  /**
   * Removes resource with given URI.
   *
   * @param uri uri of resource.
   * @throws IllegalArgumentException if given resource cannot be removed.
   */
  void removeResource(String uri) throws IllegalArgumentException;

  /**
   * @param connectionString
   * @return
   */
  List<Resource> getOcmgApplications(String connectionString, String coreConnectionId);

  /**
   * @param resource
   * @throws pl.edu.agh.semsimmon.common.api.transport.TransportException
   *
   */
  void stopResource(Resource resource) throws ResourcesException;

  /**
   * @param resource
   * @throws pl.edu.agh.semsimmon.common.api.transport.TransportException
   *
   */
  void pauseResource(Resource resource) throws ResourcesException;

  /**
   * @param resource
   * @throws pl.edu.agh.semsimmon.common.api.transport.TransportException
   *
   */
  void resumeResource(Resource resource) throws ResourcesException;

}
