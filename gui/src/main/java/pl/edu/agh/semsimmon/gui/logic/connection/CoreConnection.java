package pl.edu.agh.semsimmon.gui.logic.connection;

import pl.edu.agh.semsimmon.common.api.CoreServiceFacade;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 14:53 06-06-2010
 */
public interface CoreConnection {
  String getId();

  CoreServiceFacade getCoreServiceFacade();

  String getLabel();

  void setLabel(String label);

  ConnectionType getConnectionType();
}
