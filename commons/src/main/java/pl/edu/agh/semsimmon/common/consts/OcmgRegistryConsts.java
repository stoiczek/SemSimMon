package pl.edu.agh.semsimmon.common.consts;

import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 13:08 29-08-2010
 */
public class OcmgRegistryConsts {

  /**
   * Name of connection type property.
   */
  public final static String CONNECTION_TYPE = ResourcePropertyNames.PROPAGABLE_RESOURCE_NAME_PRFX + ".connection.type";


  /**
   * Globus connection type property value.
   */
  public final static String CONNECTION_TYPE_GLOBUS = "gsi";

  /**
   * Based on Java sockets connection type property value.
   */
  public final static String CONNECTION_TYPE_SOCKET = "mci";

  public static final String MAIN_SM_CONNECTION_STRING = ResourcePropertyNames.PROPAGABLE_RESOURCE_NAME_PRFX + ".connectionString";

}
