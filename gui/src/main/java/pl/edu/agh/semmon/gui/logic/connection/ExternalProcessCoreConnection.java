package pl.edu.agh.semmon.gui.logic.connection;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteConnectFailureException;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents connection to core process which has been started using current gui application.
 *
 * @author tkozak
 *         Created at 14:29 06-06-2010
 */
public class ExternalProcessCoreConnection extends ExternalCoreConnection {

  private static final Logger log = LoggerFactory.getLogger(ExternalProcessCoreConnection.class);

  private static final String FEEDBACK_LOG_PREFIX = "wizards.resources.add.externalCoreMsgs.";

  private static final int TIMEOUT = 10000;

  /**
   * System property of current user's name
   */
  private static final String USER_NAME_PROP_NAME = "user.name";

  /**
   * Default SSH server port
   */
  private static final int DEF_SSH_PORT = 22;

  /**
   * Core resource bundle url
   */
  private static final String CORE_RESOURCE = "core-app-0.1-SNAPSHOT.tar.gz";

  /**
   * Delimiter used to fetch end of execued command output
   */
  private static final String CMD_DELIMITER = "########################";

  /**
   * Command that generated delimiter - called after each command invoked on remote host
   */
  private static final String APPEND_DELIMITER_CMD = "echo \"########################\"";

  /**
   * Name od directory with core installed
   */
  private static final String SEMMON_CORE_DIR_NAME = ".semmon_core";

  /**
   * Directory where core should be installed
   */
  private static final String SEMMON_CORE_PARENT_DIR = "~";

  /**
   * Path to installed core (in thery absolut path, but uses '~' )
   */
  private static final String SEMMON_CORE_LOCATION = SEMMON_CORE_PARENT_DIR + "/" + SEMMON_CORE_DIR_NAME;

  /**
   * Name of script used for starting
   */
  private static final String SEMMON_CORE_SCRIPT_NAME = "semmon-core.sh";

  /**
   * Absolute path to core control script.
   */
  private static final String SEMMON_CORE_SCRIPT_LOCATION = SEMMON_CORE_LOCATION + "/" + SEMMON_CORE_SCRIPT_NAME;

  /**
   * Jsch controls.
   */
  private Session session;
  private ChannelShell shell;

  /**
   * Shell I/O.
   */
  private PrintWriter shellWriter;
  private BufferedReader shellReader;

  /**
   * Credentials.
   */
  private String connectionString;
  private String password;

  private ExternalCoreFeedbackSink feedbackSink;

  /**
   * Default public constructor. Doesn't initiate connection or start core - just stores credentials
   *
   * @param connectionString connection string in form : "[username@]host[:port]". If username is omitted - current user
   *                         name will be used (System.getProperty("user.name")). If port is ommited, then default SSH port will be used (22)
   * @param password         user's password on given host
   */
  public ExternalProcessCoreConnection(String connectionString, String password) {
    super(ConnectionType.EXTERNAL_PROCESS);
    this.connectionString = connectionString;
    this.password = password;
  }

  /**
   * Additional constructor that takes also ExternalCoreFeedbackSink to notify about new events.
   *
   * @param connectionString connection string to be used for connection
   * @param password         password - either for key, or for password based auth
   * @param feedbackSink     sink for feedbacks. Might be null.
   */
  public ExternalProcessCoreConnection(String connectionString, String password,
                                       ExternalCoreFeedbackSink feedbackSink) {
    this(connectionString, password);
    this.feedbackSink = feedbackSink;
  }

  /**
   * Starts core process. Most of this class's "magic" is performed here:
   * - initialization of SSH channel
   * - verification if known semmon-core is installed on remote host
   * - upload if above verification fails
   * - start of actual semmon-core process
   *
   * @throws IOException on any error with transport - upload, remote commands invocation
   */
  public void startCore() throws IOException {
    log.debug("Starting remote core instance");
    appendExternalFeedbackLog("startingCore");
    final String userName = getUserName(connectionString);
    final int port = getPort(connectionString);
    final String host = getHost(connectionString);
    setServiceHost(host);
    try {
      initSSHConnection(userName, port, host);
      if (!isCoreInstalled()) {
        uploadCore();
      } else if (feedbackSink != null) {
        feedbackSink.setBytesToUpload(10);
        feedbackSink.setBytesUploaded(10);
      }
      doStartCore();
    } catch (Exception e) {
      shell.disconnect();
      session.disconnect();
      if (e instanceof IOException) {
        throw (IOException) e;
      } else {
        throw new IOException(e);
      }
    }
  }

  /**
   * Stops core - also disconnects if needed. This method also closes SSH channel.
   *
   * @throws IOException
   */
  public void stopCore() throws IOException {
    log.debug("Stopping remote core instance");
    disconnect();
    executeCmd(SEMMON_CORE_SCRIPT_LOCATION + " stop");
    shell.disconnect();
    session.disconnect();
  }

  @Override
  public void connect() throws IOException {
    final int maxTries = 15;
    int i = 0;
    for (i = 0; i < maxTries; i++) {
      try {
        appendExternalFeedbackLog("coreConnecting", Integer.toString(i));
        super.connect();
        break;
      } catch (Exception e) {
        appendExternalFeedbackLog("coreConnectingError");
        log.warn("Error connecting - core haven't initialized properly");
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e1) {
          log.error("Impossible", e1);
        }
      }
    }
    if (i == maxTries) {
      throw new IOException("Error connecting to started core - timeout reached");
    }

  }

  private void doStartCore() throws IOException {
    appendExternalFeedbackLog("startingCoreProcess");
    String cmd = SEMMON_CORE_SCRIPT_LOCATION + " start";
    List<String> result = executeCmd(cmd);
    if (result.isEmpty()) {
      cmd = SEMMON_CORE_SCRIPT_LOCATION + " start";
      executeCmd(cmd);
      throw new IOException("Error starting external core process.");
    }
    if (!result.get(result.size() - 1).equals("0")) {
      StringBuilder msgBuilder = new StringBuilder("Error starting external core process. Script's output:\n");
      for (String line : result) {
        msgBuilder.append(line).append('\n');
      }
      cmd = SEMMON_CORE_SCRIPT_LOCATION + " start";
      executeCmd(cmd);
      throw new IOException(msgBuilder.toString());
    }
  }

  private void initSSHConnection(String userName, int port, String host) throws JSchException, IOException, InterruptedException {
    JSch jsch = new JSch();
    String privateSSHKeyFilePath = System.getProperty("user.home") + File.separatorChar + ".ssh" +
        File.separatorChar + "id_rsa";
    if (new File(privateSSHKeyFilePath).exists()) {
      appendExternalFeedbackLog("addingId", privateSSHKeyFilePath);
      jsch.addIdentity(privateSSHKeyFilePath, password);
    }
    session = jsch.getSession(userName, host, port);
    session.setUserInfo(new CoreConnectionUserInfo(password));
    appendExternalFeedbackLog("establishingSSH");
    session.connect(TIMEOUT);
    appendExternalFeedbackLog("openingShell");
    shell = (ChannelShell) session.openChannel("shell");
    final OutputStream outputStream = shell.getOutputStream();
    shellWriter = new PrintWriter(outputStream);
    final InputStream shellInput = shell.getInputStream();
    shellReader = new BufferedReader(new InputStreamReader(shellInput));
    shell.connect();
    shell.start();
    Thread.sleep(1000);
    shellInput.skip(shellInput.available());
    appendExternalFeedbackLog("shellInitialised");

  }

  private boolean isCoreInstalled() throws IOException {
    log.debug("Checking whether core is installed on remote machine");
    appendExternalFeedbackLog("checkingCoreInstalled");
    String cmd = "ls -a " + SEMMON_CORE_PARENT_DIR + " | grep " + SEMMON_CORE_DIR_NAME;
    List<String> lines = executeCmd(cmd);
    log.debug("Output: {}", lines);
    boolean containsCoreDir = false;
    for (String line : lines) {
      if (line.contains(SEMMON_CORE_DIR_NAME)) {
        containsCoreDir = true;
        break;
      }
    }
    if (!containsCoreDir) {
      return false;
    }
    cmd = "ls " + SEMMON_CORE_LOCATION;
    lines = executeCmd(cmd);
    for (String line : lines) {
      if (line.contains(SEMMON_CORE_SCRIPT_NAME)) {
        return true;
      }
    }
    return false;
  }

  private void uploadCore() throws JSchException, SftpException, IOException {
    log.debug("Uploading core module to remote host");
    log.debug("Opening channel");
    appendExternalFeedbackLog("uploading");
    ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
    sftp.connect();
    log.debug("Creating dir: {}", SEMMON_CORE_LOCATION);
    executeCmd("mkdir -p " + SEMMON_CORE_LOCATION);
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    URL coreBundleResourceURL = cl.getResource(CORE_RESOURCE);
    if (coreBundleResourceURL == null) {
      throw new IOException("Cannot find resource containing core bundle: " + CORE_RESOURCE + ". Check classpath");
    }
    final long bytes = new File(coreBundleResourceURL.getFile()).length();
    final InputStream coreResourceInputStream = coreBundleResourceURL.openStream();
    log.debug("Uploading data - {} bytes", bytes);
    sftp.cd(SEMMON_CORE_DIR_NAME);
    final String packageFile = "core.tar.gz";
    appendExternalFeedbackLog("uploadingData", Long.toString(bytes));
    if (feedbackSink != null) {
      feedbackSink.setBytesToUpload(bytes);
    }
    long startTime = System.currentTimeMillis();
    sftp.put(coreResourceInputStream, packageFile, new LoggingSFTPMonitor(bytes));
    long endTime = System.currentTimeMillis();
    long uploadTime = (endTime - startTime) / 1000;
    appendExternalFeedbackLog("uploadDone", Long.toString(uploadTime));
    appendExternalFeedbackLog("untaring");

    List<String> result = executeCmd("tar -x -f " + SEMMON_CORE_LOCATION + "/" +
        packageFile + " -C " + SEMMON_CORE_LOCATION);
    log.debug("Adding execute rights to startup script");
    log.debug("Result: {}", result);
    result = executeCmd("chmod u+x " + SEMMON_CORE_SCRIPT_LOCATION);
    log.debug("Result: {}", result);
    log.debug("Core uploaded");
    appendExternalFeedbackLog("uploadDoneTotal");
  }

  private List<String> executeCmd(String cmd) throws IOException {
    log.debug("Executing cmd: {}", cmd);
    shellWriter.println(cmd);
    shellWriter.println(APPEND_DELIMITER_CMD);
    shellWriter.flush();
    List<String> lines = new LinkedList<String>();
    while (true) {
      String line = shellReader.readLine();
      if (line.equals(CMD_DELIMITER)) {
        break;
      }
      lines.add(line);
    }
    // skip echoed cmd and delimiter print
    final List<String> result = lines.subList(1, lines.size() - 1);
    log.debug("Got {} lines of result", result.size());
    return result;
  }

  private String getHost(String connectionString) {
    final boolean userSpecified = connectionString.contains("@");
    final boolean portSpecified = connectionString.contains(":");
    if (userSpecified && portSpecified) {
      return connectionString.substring(connectionString.indexOf('@') + 1, connectionString.indexOf(':'));
    } else if (userSpecified) {
      return connectionString.substring(connectionString.indexOf('@') + 1, connectionString.length());
    } else if (portSpecified) {
      return connectionString.substring(0, connectionString.indexOf(':'));
    } else {
      return connectionString;
    }
  }

  private int getPort(String connectionString) {
    if (connectionString.indexOf(':') > 0) {
      return Integer.parseInt(connectionString.substring(connectionString.indexOf(':') + 1 ));
    } else {
      return DEF_SSH_PORT;
    }
  }

  private String getUserName(String connectionString) {
    if (connectionString.indexOf('@') > 0) {
      return connectionString.substring(0, connectionString.indexOf('@'));
    } else {
      return System.getProperty(USER_NAME_PROP_NAME);
    }
  }

  private void appendExternalFeedbackLog(String key, String... args) {
    if (feedbackSink != null) {
      feedbackSink.appendLogStatus(FEEDBACK_LOG_PREFIX + key, args);
    }
  }

  private class LoggingSFTPMonitor implements SftpProgressMonitor {

    private long sent;
    private long lastLogTime;
    private final long bytes;

    public LoggingSFTPMonitor(long bytes) {
      this.bytes = bytes;
      sent = 0;
      lastLogTime = 0;
    }

    @Override
    public void init(int op, String src, String dest, long max) {
      log.debug("Uploading data to: {}", dest);
      lastLogTime = System.currentTimeMillis();
    }

    @Override
    public boolean count(long count) {
      sent += count;
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastLogTime > 1000) {
        log.debug("Uploaded {} - {}%", sent, (sent * 100) / bytes);
        lastLogTime = currentTime;
        if (feedbackSink != null) {
          feedbackSink.setBytesUploaded(sent);
        }
      }
      return true;
    }

    @Override
    public void end() {
      log.debug("Upload finished");
    }
  }
}

class CoreConnectionUserInfo implements UserInfo {

  private static final Logger log = LoggerFactory.getLogger(CoreConnectionUserInfo.class);

  private String password;

  CoreConnectionUserInfo(String password) {
    this.password = password;
  }

  @Override
  public String getPassphrase() {
    return password;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean promptPassword(String message) {
    return true;
  }

  @Override
  public boolean promptPassphrase(String message) {
    return true;
  }

  @Override
  public boolean promptYesNo(String message) {
    return true;
  }

  @Override
  public void showMessage(String message) {
    log.debug("Got message from jsch: {}", message);
  }
}
