package pl.edu.agh.semmon.gui.controllers;


import org.apache.pivot.beans.BXML;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.PushButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semmon.gui.controllers.action.ButtonAction;

public class SampleController extends BaseController<BoxPane> {

  public static final Logger log = LoggerFactory.getLogger(SampleController.class);

  @BXML
  private PushButton cancelButton;

  @BXML
  private Label label;

  private int i = 0;

  @ButtonAction(type = ButtonAction.Type.INSTANT)
  private void cancelButtonPressed() {
    label.setText("Sample content " + i++);
  }

  @ButtonAction(target = "cancelButton", type = ButtonAction.Type.BACKGROUND)
  private void backgroundTaskofCancelButton() throws InterruptedException {
    log.debug("Starting background task");
    Thread.sleep(5000);
    log.debug("Background task done");
  }


  @Override
  protected Class getBindableClass() {
    return SampleController.class;
  }


}
