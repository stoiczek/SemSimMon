package pl.edu.agh.semmon.gui.logic.connection;

/**
 * Used to gather feedback from setting up remote core connection.
 * This is really helpful to the end user, since this process may take several seconds to complete.
 *
 * @author tkozak
 *         Created at 18:05 12-09-2010
 */
public interface ExternalCoreFeedbackSink {

  /**
   * Called to append log message
   *
   * @param msg msg to append
   * @param args additional args to be used.
   */
  void appendLogStatus(String msg, String... args);

  /**
   * Configures amount of bytes to be uploaded - has to be called before setBytesUploaded
   *
   * @param bytesToUpload total amount of bytes that has to be uploaded to remote host.
   */
  void setBytesToUpload(long bytesToUpload);

  /**
   * Notifies about new amounts of bytes transferred
   *
   * @param bytesUploaded amount of bytes transferred.
   */
  void setBytesUploaded(long bytesUploaded);
}
