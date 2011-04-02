package pl.edu.agh.semsimmon.gui.util;

import org.apache.pivot.wtk.content.ButtonData;
import org.apache.pivot.wtk.media.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for default button container. Fixes string-based icon configuration + adds options for radio button groups -
 * enum - based configuration.
 *
 * @author tkozak
 *         Created at 13:06 05-06-2010
 */
public class ButtonDataContainer extends ButtonData {

  private static final Logger log = LoggerFactory.getLogger(ButtonDataContainer.class);

  private final AdditionalUiDataContainer additionalUiDataContainer = new AdditionalUiDataContainer();

  private Object additionalContent;

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

  public Object getAdditionalContent() {
    return additionalContent;
  }

  public void setAdditionalContent(Object additionalContent) {
    this.additionalContent = additionalContent;
  }

}
