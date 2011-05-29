package pl.edu.agh.semsimmon.gui.controllers.wizard;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.json.JSON;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.PushButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semsimmon.gui.controllers.dialog.BaseDialogController;

import java.io.IOException;
import java.util.List;

/**
 * Base class for all wizard controllers. Acts as utility which handles common Cancel, Prev, Next, Finish actions.
 *
 * @author tkozak
 *         Created at 17:09 05-06-2010
 */
public abstract class BaseWizardController extends BaseDialogController {

  private static final Logger log = LoggerFactory.getLogger(BaseWizardController.class.getName());

  private String titleKey;

  private String description;


  protected List<BaseWizardPageController> pages;

  private int currentPageNo = 0;

  @BXML
  private PushButton cancelButton;

  @BXML
  private PushButton backButton;

  @BXML
  private PushButton nextButton;

  @BXML
  private PushButton finishButton;

  @BXML
  private FlowPane pageContainer;


  /**
   * Called on wizard finish - all pages were properly filled.
   */
  protected abstract void wizardFinished() throws Exception;

  @Override
  protected Class getBindableClass() {
    return BaseWizardController.class;
  }

  @ButtonAction
  private void cancelButtonPressed() {
    log.debug("Cancel pressed");
    component.close();
  }

  @ButtonAction(target = "cancelButton", type = ButtonAction.Type.BACKGROUND)
  private void cleanupCancelledWizard() {

  }

  @ButtonAction(type = ButtonAction.Type.BACKGROUND)
  private void backButtonPressed() {
    if (!switchPages(false)) {
      return;
    }
    if (currentPageNo == 0) {
      backButton.setVisible(false);
      cancelButton.setVisible(true);
    }
    if (currentPageNo == pages.size() - 2) {
      finishButton.setVisible(false);
      nextButton.setVisible(true);
    }
  }

  @ButtonAction(type = ButtonAction.Type.BACKGROUND)
  private void nextButtonPressed() {
    if (!switchPages(true)) {
      return;
    }
    if (currentPageNo == pages.size() - 1) {
      nextButton.setVisible(false);
      finishButton.setVisible(true);
    } else if (currentPageNo > 0) {
      cancelButton.setVisible(false);
      backButton.setVisible(true);
    }
    component.repaint(true);
  }

  @ButtonAction(target = "finishButton", type = ButtonAction.Type.INSTANT)
  private void closeOnFinish() {
    component.clear();
    component.close();
  }

  @ButtonAction(type = ButtonAction.Type.BACKGROUND)
  private void finishButtonPressed() {
    pages.get(currentPageNo).pageHiding(true);
    try {
      wizardFinished();
    } catch (Exception e) {
      log.error("Exception occurred during wizard finishing", e);
    }
  }

  @Override
  protected void postBinding() {
    if (pages.isEmpty()) {
      throw new IllegalStateException("No pages defined for wizard");
    }
    if (pages.size() == 1) {
      log.warn("Some kind of idiot doesn't know what WIZARD is, and created one page wizard :/, " +
          "see: http://en.wikipedia.org/wiki/Wizard_(software)");
      nextButton.setVisible(false);
      finishButton.setVisible(true);
    }
    component.setTitle(JSON.<String>get(resources, titleKey));
    pageContainer.add(pages.get(currentPageNo).getComponent());
    log.debug("Opening wizard");
    component.setLocation(100, 100);
    component.open(parentWindow);
    log.debug("AddResources wizard initialized");
  }

  private boolean switchPages(boolean ascending) {
    int newPageNo;
    boolean status;
    try {
      if (ascending) {
        newPageNo = currentPageNo + 1;
      } else {
        newPageNo = currentPageNo - 1;
      }
      BaseWizardPageController oldPage = pages.get(currentPageNo);
      BaseWizardPageController currentPage = pages.get(newPageNo);

      oldPage.pageHiding(ascending);
      pageSwitching(oldPage, currentPage);
      pageContainer.remove(oldPage.getComponent());

      currentPage.pageShowing();
      pageContainer.add(currentPage.getComponent());
      pageSwitched(oldPage, currentPage);
      currentPageNo = newPageNo;
      status = true;
    } catch (VetoException e) {
      log.warn("Page vetoed change by throwing an exception.", e);
      // fail if going forward, proceed if going back
      status = !ascending;
    }
    return status;
  }

  protected void pageSwitching(BaseWizardPageController oldPage, BaseWizardPageController newPage) {

  }

  protected void pageSwitched(BaseWizardPageController oldPage, BaseWizardPageController newPage) {

  }

  protected void wizardCancelled() throws IOException {

  }

  public void setTitleKey(String title) {
    this.titleKey = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public void setPages(List<BaseWizardPageController> pages) {
    this.pages = pages;
  }
}
