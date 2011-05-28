package pl.edu.agh.semsimmon.common.api.resource;

/**
 * @author tkozak
 *         Created at 46:11 25-05-2010
 *         Class containing ids of constant resources parameters
 */
public final class ResourcePropertyNames {

  public static final String PROPAGABLE_RESOURCE_NAME_PRFX = "propagable";

  /**
   *
   */
  public static final String RESOURCE_PROPERTY_PREFIX = "rp.";

  /**
   *
   */
  public static final String RESOURCE_PAUSEABLE_PN = "pauseable";

  /**
   *
   */
  public static final String RESOURCE_STOPPABLE_PN = "stoppable";

  /**
   *
   */
  public static final String RESOURCE_STATE =  "state";

  /**
   *
   */
  public static final String RESOURCE_REMOVABLE = "removable";


  private ResourcePropertyNames() {
  }


  /**
   * Parameters of Application resource.
   */
  public final class Application {

    private Application() {
    }

    public static final String NAME = RESOURCE_PROPERTY_PREFIX + "app.name";


  }

  /**
   * Parameters of cluster
   */
  public final class Cluster {

    public static final String ID = RESOURCE_PROPERTY_PREFIX + "cluster.id";

    private Cluster() {

    }
  }

  /**
   *
   */
  public final class Node {

    private Node() {
    }

    /**
     *
     */
    public static final String NAME = RESOURCE_PROPERTY_PREFIX + "node.name";
  }

  public final class Os {
    private Os() {
    }

    public static final String RELEASE = RESOURCE_PROPERTY_PREFIX + "os.release";
    public static final String VERSION = RESOURCE_PROPERTY_PREFIX + "os.version";

    public static final String NAME = RESOURCE_PROPERTY_PREFIX + "os.name";
    public static final String ARCHITECTURE = RESOURCE_PROPERTY_PREFIX + "os.arch";
  }

  public final class Process {
    public static final String ARGUMENTS = RESOURCE_PROPERTY_PREFIX + "process.args";
    public static final String GLOBAL_ID = RESOURCE_PROPERTY_PREFIX + "process.globalId";
    public static final String USER_ID = RESOURCE_PROPERTY_PREFIX + "process.userId";
    public static final String GROUP_ID = RESOURCE_PROPERTY_PREFIX + "process.groupId";

    private Process() {
    }

  }

  public final class Function {

    public static final String FILE_NAME = RESOURCE_PROPERTY_PREFIX + "function.fileName";
    public static final String FUNCTION_NAME = RESOURCE_PROPERTY_PREFIX + "function.functionName";
    public static final String START_ADDRESS = RESOURCE_PROPERTY_PREFIX + "function.startAddr";
    public static final String END_ADDRESS = RESOURCE_PROPERTY_PREFIX + "function.endAddr";
  }

  public final class Jvm {
    public static final String BOOT_CLASSPATH = RESOURCE_PROPERTY_PREFIX + "jvm.bootclasspath";
    public static final String CLASSPATH = RESOURCE_PROPERTY_PREFIX + "jvm.classpath";
    public static final String INPUT_ARGS = RESOURCE_PROPERTY_PREFIX + "jvm.inputargs";
    public static final String LIB_PATH = RESOURCE_PROPERTY_PREFIX + "jvm.libpath";
    public static final String NAME = RESOURCE_PROPERTY_PREFIX + "jvm.name";
    public static final String SPEC_NAME = RESOURCE_PROPERTY_PREFIX + "jvm.specname";
    public static final String SPEC_VENDOR = RESOURCE_PROPERTY_PREFIX + "jvm.specvendor";
    public static final String SPEC_VERSION = RESOURCE_PROPERTY_PREFIX + "jvm.specversion";
    public static final String VM_NAME = RESOURCE_PROPERTY_PREFIX + "jvm.vmname";
    public static final String VM_VENDOR = RESOURCE_PROPERTY_PREFIX + "jvm.vmvendor";
    public static final String VM_VERSION = RESOURCE_PROPERTY_PREFIX + "jvm.vmversion";
    public static final String START_TIME = RESOURCE_PROPERTY_PREFIX + "jvm.starttime";
  }

  public final class Thread {
    public static final String ID = RESOURCE_PROPERTY_PREFIX + "thread.id";
    public static final String NAME = RESOURCE_PROPERTY_PREFIX + "thread.name";

    public static final String OWNING_PROCESS_ID = "thread.ownerId";

  }


  public final class GarbageCollector {
    public static final String NAME = RESOURCE_PROPERTY_PREFIX + "gc.name";

  }

  public final class HardDrive {
    public static final String DEVICE_NAME = RESOURCE_PROPERTY_PREFIX + "hd.device";
    public static final String BLOCKS = RESOURCE_PROPERTY_PREFIX + "hd.blocks";
    public static final String MOUNT_POINT = RESOURCE_PROPERTY_PREFIX + "hd.mount";
  }

  public final class NetworkInterface {
    public static final String INTERFACE_NAME = RESOURCE_PROPERTY_PREFIX + "net.ifaceName";
  }

  public final class CPU {

    public static final String ID = RESOURCE_PROPERTY_PREFIX + "cpu.id";
    public static final String VENDOR_ID = RESOURCE_PROPERTY_PREFIX + "cpu.vendorId";
    public static final String FAMILY = RESOURCE_PROPERTY_PREFIX + "cpu.family";
    public static final String MODEL = RESOURCE_PROPERTY_PREFIX + "cpu.model";
    public static final String MODEL_NAME = RESOURCE_PROPERTY_PREFIX + "cpu.modelName";
    public static final String STEPPING = RESOURCE_PROPERTY_PREFIX + "cpu.stepping";
    public static final String CLOCK = RESOURCE_PROPERTY_PREFIX + "cpu.clock";
    public static final String CACHE = RESOURCE_PROPERTY_PREFIX + "cpu.cache";
    public static final String FLAGS = RESOURCE_PROPERTY_PREFIX + "cpu.flags";
    public static final String BOGO_MIPS = RESOURCE_PROPERTY_PREFIX + "cpu.bogoMpis";
  }

  public final class VirtualMemory {

    public static final String DEVICE_NAME = RESOURCE_PROPERTY_PREFIX + "vm.deviceName";
    public static final String TYPE = RESOURCE_PROPERTY_PREFIX + "vm.type";
    public static final String SIZE = RESOURCE_PROPERTY_PREFIX + "vm.size";

  }

  public static final class PhysicalMemory {

    public static final String SIZE = RESOURCE_PROPERTY_PREFIX + "pm.size";

  }
}
