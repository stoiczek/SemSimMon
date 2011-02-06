package pl.edu.agh.semmon.gui.logic.connection;

import pl.edu.agh.semmon.common.vo.core.resource.Resource;

import java.io.IOException;
import java.util.List;

/**
 * Provides facility to implement managers of connections to core modules.
 *
 * @author tkozak
 *         Created at 14:12 06-06-2010
 */
public interface CoreConnectionsManager {

  /**
   * Returns list containing all active connections.
   *
   * @return list containing all active connections.
   */
  List<CoreConnection> getCoreConnections();

  /**
   * Connects to externally started core module.
   * It uses given serviceHost which represents ip address in std dotted-decimal notation or DNS hostname.
   * Using this info, manager constructs proper service URL and performs necessary initialization of remoting
   *
   * @param serviceHost hostname or ip address of machine to connect
   * @return newly created object representing connection to given core.
   * @throws IOException on error with establishing connection
   */
  CoreConnection connectToExternalCore(String serviceHost) throws IOException;

  /**
   * Starts external core process using SSH tunnel to access given machine
   *
   * @param connectionString connection string in form:
   *                         username@targethost:SSH-port
   * @param password         user's password on given id. leave blank to use default public key architecture
   * @param feedbackSink
   * @return newly created object representing connection to given core.
   */
  CoreConnection startExternalCoreProcess(String connectionString, String password, ExternalCoreFeedbackSink feedbackSink) throws IOException;

  /**
   * Terminates external core process.
   *
   * @param connectionId id of connection to terminate process.
   */
  void stopExternalCoreProcess(String connectionId);


  /**
   * Disconnects from given core.
   *
   * @param connectionId id of connection to given core.
   */
  void disconnectFromExternalCore(String connectionId) throws IOException;

  /**
   * Returns connection by with given id.
   *
   * @param connectionId id of connection to get.
   * @return connection with given id.
   */
  CoreConnection getConnectionById(String connectionId);

  CoreConnection getConnectionOfResource(Resource resource);

  void identifyResource(Resource resource, String connectionId);

}
