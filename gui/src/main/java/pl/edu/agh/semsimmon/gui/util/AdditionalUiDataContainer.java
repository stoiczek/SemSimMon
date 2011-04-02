package pl.edu.agh.semsimmon.gui.util;

public class AdditionalUiDataContainer {


  private String id;

  private String enumClass;

  private String enumName;

  private Object additionalContent;


  public AdditionalUiDataContainer() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Object getAdditionalContent() {
    return additionalContent;
  }

  public void setAdditionalContent(Object additionalContent) {
    this.additionalContent = additionalContent;
  }

  public void setEnumClass(String enumClass) {
    this.enumClass = enumClass;
  }

  public void setEnumName(String enumName) {
    this.enumName = enumName;
  }

  public Enum getEnumValue() {
    Class itemClass;
    try {
      itemClass = Class.forName(enumClass);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Cannot find given class in classpath: " + enumClass);
    }
    if (!itemClass.isEnum()) {
      throw new IllegalArgumentException("Given class doesn't represent enum");
    }
    Class<Enum> enumClass = itemClass;
    return Enum.valueOf(enumClass, enumName);
  }
}