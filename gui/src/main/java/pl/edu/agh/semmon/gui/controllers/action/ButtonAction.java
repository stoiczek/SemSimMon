package pl.edu.agh.semmon.gui.controllers.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark methods which should be invoked on pushButton pressed.
 * If this annotation marks method which fills following signature:
 * {private||protected|public} void {buttonName}ButtonPressed()
 * It will be automatically assigned with buttonName button's action. In other cases, user of this annotation
 * can specify button, by this annotation's target property.
 *
 * @author tkozak
 *         Created at 18:34 05-06-2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ButtonAction {

  /**
   * Type of action. This can be used to define whether action should be performed on EDT or in background thread.
   */
  public enum Type {

    /**
     * Action will be performed on EDT thread.
     */
    INSTANT,

    /**
     * Action will be performed on background thread.
     */
    BACKGROUND
  }

  /**
   * Returns string defining target button. If value isn't specified, id of button will be guessed from method name.
   * @return target button id.
   */
  public String target() default "";

  /**
   * Returns type of action - background or foreground (EDT).
   * @return
   */
  public Type type() default Type.BACKGROUND;

}
