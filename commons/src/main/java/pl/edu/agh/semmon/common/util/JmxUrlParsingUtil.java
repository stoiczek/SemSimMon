package pl.edu.agh.semmon.common.util;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 15:19 11-07-2010
 */
public class JmxUrlParsingUtil {

  protected JmxUrlParsingUtil() {

  }

  public static int getServicePort(String serviceUri) {
    final int colonPos = serviceUri.lastIndexOf(':');
    String portString = serviceUri.substring(colonPos + 1, serviceUri.length());
    portString = portString.substring(0, portString.indexOf('/'));
    return Integer.parseInt(portString);
  }

  public static String getServiceHost(String serviceUri) {
    final int colonPos = serviceUri.lastIndexOf(':');
    String host = serviceUri.substring(0, colonPos);
    final int slashPos = host.lastIndexOf('/');
    return host.substring(slashPos + 1, host.length());
  }
}
