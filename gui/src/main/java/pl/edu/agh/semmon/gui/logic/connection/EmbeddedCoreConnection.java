package pl.edu.agh.semmon.gui.logic.connection;

import pl.edu.agh.semmon.common.api.CoreServiceFacade;

import javax.annotation.PostConstruct;

/**
 * Represents 'connection' to gui's embedded core.
 *
 * @author tkozak
 *         Created at 14:28 06-06-2010
 */
public class EmbeddedCoreConnection extends AbstractCoreConnection {

  public EmbeddedCoreConnection() {
    super(ConnectionType.EMBEDDED);
    label = "Local Core";
  }

  public void setCoreServiceFacade(CoreServiceFacade coreServiceFacade) {
    this.coreServiceFacade = coreServiceFacade;
  }

  @PostConstruct
  public void setupEventMapping() {
    coreServiceFacade.addResourceListener(this);

  }

  
}
