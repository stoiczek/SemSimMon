package pl.edu.agh.semsimmon.gui.controllers.wizard;

import org.apache.pivot.wtk.Component;
import pl.edu.agh.semsimmon.gui.controllers.BaseController;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:14 05-06-2010
 */
public abstract class BaseWizardPageController<T extends Component> extends BaseController<T> {

  /**
   * Called before page will be displayed. It's good point to initialize any content of page that uses
   * data from previous one.
   */
  public abstract void pageShowing();

  /**
   * Called before hiding given page. Best point to validate data. This method can veto change by throwing any
   * exception. 
   */
  public abstract void pageHiding();


}
