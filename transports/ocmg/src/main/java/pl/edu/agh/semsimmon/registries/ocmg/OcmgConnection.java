package pl.edu.agh.semsimmon.registries.ocmg;

import org.balticgrid.ocmg.base.*;
import org.balticgrid.ocmg.mainsm.OCMGMainSMTool;
import org.balticgrid.ocmg.objects.Application;
import org.balticgrid.ocmg.objects.Monitor;
import org.balticgrid.ocmg.objects.Policy;
import org.balticgrid.ocmg.wrappers.ApplicationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.consts.OcmgRegistryConsts;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tkozak
 *         Created at 53:13 19-05-2010
 *         Represents connection with running OCMG instance. Allows reading capability values.
 */
public class OcmgConnection {

  private class ApplicationCacheEntry {

    private ApplicationCacheEntry(Application application) {
      this.application = application;
      usageCount = 0;
    }

    Application application;
    int usageCount;
  }

  private static final Logger log = LoggerFactory.getLogger(OcmgConnection.class.getName());

  /**
   *
   */
  private Resource connectionResource;

  /**
   *
   */
  private Connection connection;

  /**
   *
   */
  private Monitor monitor;

  /**
   *
   */
  private boolean connected;

  /**
   *
   */
  private Map<String, ApplicationCacheEntry> applicationCache = new HashMap<String, ApplicationCacheEntry>();

  /**
   * Constructs connection with given resource
   *
   * @param resource resource to connect to.
   */
  public OcmgConnection(Resource resource) {
    connectionResource = resource;
  }


  public void connect() throws OcmgException {
    final String connectionString = connectionResource.getProperty(OcmgRegistryConsts.MAIN_SM_CONNECTION_STRING).
        toString();
    try {
      InetAddress address = OCMGMainSMTool.parseAddress(connectionString.split(":")[0]);
      int port = OCMGMainSMTool.parsePort(connectionString.split(":")[1]);
      final String connectionType = connectionResource.getProperty(OcmgRegistryConsts.CONNECTION_TYPE).toString();
      log.debug("Initializing connection with: {}", connectionString);
      if (connectionType.equals(OcmgRegistryConsts.CONNECTION_TYPE_SOCKET)) {
        connection = new MCIConnection(address, port);
      } else {
        connection = new GSIConnection(address, port);
      }
      log.debug("Connection created, checking if given application is valid");
      monitor = new Monitor(connection);

      Policy policy = new Policy();
      policy.setTraceThreadStatus(true);
      policy.setTraceValue(true);
      org.balticgrid.ocmg.objects.Thread.setPolicy(policy);

      log.debug("OcmgConnection initialized");
      connected = true;
    } catch (ConnectionException e) {
      log.error("Error during connection initialization: {}", e.getMessage());
      StringWriter writer = new StringWriter(512);
      e.printStackTrace(new PrintWriter(writer));
      throw new OcmgException("Got communication exception: " + e.getMessage() + "\n" + writer.toString());
    } catch (UnknownHostException e) {
      throw new OcmgException("");
    } catch (NullPointerException e) {
      throw new OcmgException("");
    }
  }

  private int getPort(String connectionString) {
    return Integer.parseInt(connectionString.split(":")[1], 16);
  }

  private String getHost(String connectionString) {
    final String hostPart = connectionString.split(":")[0];
    final StringBuilder hostBuilder = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      hostBuilder.append(Integer.parseInt(hostPart.substring(2 * i, 2 * i + 1), 16)).append('.');
    }
    hostBuilder.deleteCharAt(hostBuilder.length() - 1);
    return hostBuilder.toString();
  }

  public List<String> getApplications() throws OcmgException {
    if (!connected) {
      throw new IllegalStateException("Trying to get application without prior connection. Connect first!");
    }
    try {
      return ApplicationWrapper.getApplications(connection);
    } catch (ConnectionException e) {
      throw new OcmgException("Error getting applications list", e);
    } catch (MonitorException e) {
      throw new OcmgException("Error getting applications list", e);
    }
  }

  public void disconnect() throws OcmgException {
    if (!connected) {
      return;
    }
    for(ApplicationCacheEntry entry : applicationCache.values()) {
      try {
        entry.application.detach();
      } catch (ConnectionException e) {
        log.error("Exception detaching", e);
      } catch (MonitorException e) {
        log.error("Exception detaching", e);
      }
    }
    connection.close();
    connected = false;
  }

  public Application attachToApplication(Resource resource) throws OcmgException {
    if (applicationCache.containsKey(resource.getUri())) {
      final OcmgConnection.ApplicationCacheEntry cacheEntry = applicationCache.get(resource.getUri());
      cacheEntry.usageCount++;
      return cacheEntry.application;
    }
    String appName = resource.getProperty(ResourcePropertyNames.Application.NAME).toString();
    appName = appName.substring(appName.lastIndexOf('/') + 1);
    try {
      if (ApplicationWrapper.getApplications(connection).contains(appName)) {
        final Application application = Application.attach(monitor, appName);
        applicationCache.put(resource.getUri(), new ApplicationCacheEntry(application));
        return application;
      } else {
        throw new OcmgException("Given monitor doesn't monitor application with given name: " + appName);
      }
    } catch (ConnectionException e) {
      log.error("Transport error while attaching to application: {}", e.getMessage());
      StringWriter writer = new StringWriter(1024);
      e.printStackTrace(new PrintWriter(writer));
      throw new OcmgException("Got communication exception: " + e.getMessage() + "\n" + writer.toString());
    } catch (MonitorException e) {
      log.error("Transport error while attaching to application: {}", e.getMessage());
      StringWriter writer = new StringWriter(1024);
      e.printStackTrace(new PrintWriter(writer));
      throw new OcmgException("Got communication exception: " + e.getMessage() + "\n" + writer.toString());
    }
  }

  public void detachFromApplication(Resource resource) throws OcmgException {
    if (!applicationCache.containsKey(resource.getUri())) {
      return;
    }
    final ApplicationCacheEntry applicationEntry = applicationCache.get(resource.getUri());
    applicationEntry.usageCount--;
    if (applicationEntry.usageCount == 0) {

      try {
        applicationEntry.application.detach();
        applicationCache.remove(resource.getUri());
      } catch (Exception e) {
        throw new OcmgException(e);
      }
    }
  }

  public boolean hasAttachedApps() {
    return !applicationCache.isEmpty();
  }

}
