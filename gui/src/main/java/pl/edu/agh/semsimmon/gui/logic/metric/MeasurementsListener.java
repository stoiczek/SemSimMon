package pl.edu.agh.semsimmon.gui.logic.metric;

/**
 * Interface of listeners listening on various measurements events (e.g created/removed)
 *
 * @author tkozak
 *         Created at 17:27 14-08-2010
 */
public interface MeasurementsListener {

  void measurementCreated(Measurement measurement);

  void measurementRemoved(Measurement measurement);
}
