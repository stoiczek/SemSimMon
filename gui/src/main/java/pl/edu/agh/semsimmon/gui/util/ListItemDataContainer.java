package pl.edu.agh.semsimmon.gui.util;

import org.apache.pivot.wtk.content.ListItem;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 15:45 06-06-2010
 */
public class ListItemDataContainer extends ListItem {

  private final AdditionalUiDataContainer additionalUiDataContainer = new AdditionalUiDataContainer();

  public void setId(String id) {
    additionalUiDataContainer.setId(id);
  }

  public void setEnumClass(String enumClass) {
    additionalUiDataContainer.setEnumClass(enumClass);
  }


  public void setEnumName(String enumName) {
    additionalUiDataContainer.setEnumName(enumName);
  }

  public Enum getEnumValue() {
    return additionalUiDataContainer.getEnumValue();
  }

  public String getId() {
    return additionalUiDataContainer.getId();
  }

  public Object getAdditionalContent() {
    return additionalUiDataContainer.getAdditionalContent();
  }

  public void setAdditionalContent(Object additionalContent) {
    additionalUiDataContainer.setAdditionalContent(additionalContent);
  }

  @Override
  public String toString() {
    return getText();
  }
}
