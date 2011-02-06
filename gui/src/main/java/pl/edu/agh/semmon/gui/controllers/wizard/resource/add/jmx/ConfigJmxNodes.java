package pl.edu.agh.semmon.gui.controllers.wizard.resource.add.jmx;

import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtkx.WTKX;
import org.apache.pivot.wtkx.WTKXSerializer;
import org.springframework.core.io.Resource;
import pl.edu.agh.semmon.gui.controllers.action.ButtonAction;
import pl.edu.agh.semmon.gui.controllers.wizard.BaseWizardPageController;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 17:53 05-06-2010
 */
public class ConfigJmxNodes extends BaseWizardPageController {

  private static final String REMOVE_BUTTON_ID = "removeButton";

  private static final String NODE_SERVICE_URI_ID = "nodeUriTextInput";


  @WTKX
  private PushButton addButton;

  @WTKX
  private TextInput firstNodeUriTextInput;

  @WTKX
  private BoxPane nodesPane;

  private List<TextInput> nodeUrisTextInputs = new LinkedList<TextInput>();

  private Resource nodeDefinitionResource;

  private List<String> nodeUris = new LinkedList<String>();

  @Override
  public void pageShowing() {
    nodeUris.clear();
  }

  @Override
  public void pageHiding() {
    for (TextInput input : nodeUrisTextInputs) {
      nodeUris.add(input.getText());
    }

  }

  @ButtonAction
  private void addButtonPressed() throws IOException, SerializationException {
    final FlowPane newNodePane = (FlowPane) serializer.readObject(nodeDefinitionResource.getURL());
    final PushButton removeButton = (PushButton) serializer.get(REMOVE_BUTTON_ID);
    final TextInput nodeServiceTextInput = (TextInput) serializer.get(NODE_SERVICE_URI_ID);
    nodeUrisTextInputs.add(nodeServiceTextInput);
    removeButton.setAction(new Action() {
      @Override
      public void perform(Component source) {
        nodesPane.remove(newNodePane);
        nodeUrisTextInputs.remove(nodeServiceTextInput);
      }
    });
    nodesPane.add(newNodePane);
  }

  public List<String> getNodeUris() {
    return nodeUris;
  }

  @Override
  protected Class getBindableClass() {
    return ConfigJmxNodes.class;
  }

  @Override
  protected void postBinding() {
    nodeUrisTextInputs.add(firstNodeUriTextInput);

  }

  public void setNodeDefinitionResource(Resource nodeDefinitionResource) {
    this.nodeDefinitionResource = nodeDefinitionResource;
  }
}
