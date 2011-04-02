package pl.edu.agh.semsimmon.gui.action;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.gui.Main;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 20:30 19-09-2010
 */
public class CloseAction extends Action {

  private static final Logger log = LoggerFactory.getLogger(CloseAction.class);

  @Override
  public void perform(Component source) {
    log.debug("Performing close action");
    try {
      Main.getINSTANCE().shutdown(false);
      System.exit(0);
    } catch (Exception e) {
      log.error("Got error during application close");
    }
  }

}
