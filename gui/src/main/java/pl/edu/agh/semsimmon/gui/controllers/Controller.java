package pl.edu.agh.semsimmon.gui.controllers;

import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 18:50 05-06-2010
 */
public interface Controller<T extends Component> {


  @PostConstruct
  void initialize() throws SerializationException, IOException, IllegalAccessException;
}
