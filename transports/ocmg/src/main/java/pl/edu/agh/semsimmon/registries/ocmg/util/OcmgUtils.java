package pl.edu.agh.semsimmon.registries.ocmg.util;

import org.balticgrid.ocmg.base.*;
import org.balticgrid.ocmg.objects.apphierarchy.NodeTree;

import java.text.MessageFormat;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: tkozak
 * Date: 21.05.11
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
public class OcmgUtils {
  public static final String QUERY_REPL_PATTERN = "#";

  private static final String EXTRACT_DATA_CMD_PATTERN = ":node_get_file_listing([" + OcmgUtils.QUERY_REPL_PATTERN + "],\"/proc/cpuinfo\",0,0)";

  private static final MessageFormat GET_FILE_QUERY = new MessageFormat(":node_get_file_listing([{0}],\"{1}\",0,0)");


  public static String getNodeToken(NodeTree node) {
    return node.getNode().getToken().getValue();
  }

  public static String getFileContent(NodeTree node, String fileName) throws ConnectionException, MonitorException {
    String requestString = GET_FILE_QUERY.format(new Object[]{getNodeToken(node), fileName});
    return executeQuery(node, requestString);
  }

  public static String executeQuery(NodeTree nodeTree, String requestString) throws ConnectionException, MonitorException {
    final Connection conn = nodeTree.getNode().getConnection();
    Reply reply = conn.sendRequest(requestString);
    final int status = reply.getFragmentsList().get(0).get(0).getStatus();
    if(status != ReplyFragment.STATUS_OK) {
      throw new MonitorException("Error getting storage details", status);
    }
    String result = reply.getFragmentsList().get(1).get(0).getResult();
    return result;
  }
}
