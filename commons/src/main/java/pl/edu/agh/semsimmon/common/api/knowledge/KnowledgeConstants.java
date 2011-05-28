package pl.edu.agh.semsimmon.common.api.knowledge;

/**
 * @author tkozak
 *         Created at 56:20 21-05-2010
 *         Interface contains common constants related to SemMon ontology - e.g. ontology URI, some resource's URIs.
 */
public interface KnowledgeConstants {

  /**
   *
   */
  static final String ONTOLOGY_URI = "http://www.icsr.agh.edu.pl/semmon_2.owl";

  /**
   *
   */
  static final String APPLICATION_URI = ONTOLOGY_URI + "#Application";

  /**
   *
   */
  static final String CLUSTER_URI = ONTOLOGY_URI + "#Cluster";

  /**
   *
   */
  static final String NODE_URI = ONTOLOGY_URI + "#Node";


  /**
   *
   */
  static final String OS_URI = ONTOLOGY_URI + "#OperatingSystem";

  /**
   *
   */
  static final String CPU_URI = ONTOLOGY_URI + "#CPU";

  /**
   *
   */
  static final String PHYSICAL_MEMORY_URI = ONTOLOGY_URI + "#PhysicalMemory";

  /**
   *
   */
  static final String VIRTUAL_MEMORY_URI = ONTOLOGY_URI + "#VirtualMemory";


  /**
   *
   */
  static final String HARD_DRIVE_URI = ONTOLOGY_URI + "#HardDrive";

  /**
   *
   */
  static final String NETWORK_INTERFACE_URI = ONTOLOGY_URI + "#NetworkInterface";


  /**
   *
   */
  static final String PROCESS_URI = ONTOLOGY_URI + "#Process";

  /**
   *
   */
  static final String JVM_URI = ONTOLOGY_URI + "#JVM";


  /**
   *
   */
  static final String THREAD_URI = ONTOLOGY_URI + "#Thread";

  /**
   *
   */
  static final String FUNCTION_URI = ONTOLOGY_URI + "#Function";

  /**
   *
   */
  static final String OBJECT_URI = ONTOLOGY_URI + "#Object";

  /**
   *
   */
  static final String METHOD_URI = ONTOLOGY_URI + "#Method";

  /**
   *
   */
  static final String CLASS_URI = ONTOLOGY_URI + "#Class";


  /**
   *
   */
  static final String CLASS_LOADER_URI = ONTOLOGY_URI + "#ClassLoader";


  /**
   *
   */
  static final String GC_URI = ONTOLOGY_URI + "#GarbageCollector";

  /**
   *
   */
  static final String HAS_RESOURCE_PROPERTY_URI = ONTOLOGY_URI + "#hasResource";

  /**
   *
   */
  static final String HAS_RESOURCE_CAP_PROPERTY_URI = ONTOLOGY_URI + "#hasResourceCapability";

  /**
   *
   */
  static final String HAS_CUSTOM_CLASS_PROPERTY_URI = ONTOLOGY_URI + "#hasCustomClass";

  /**
   *
   */
  static final String CAP_IS_USED_BY_PROPERTY_URI = ONTOLOGY_URI + "#capabilityIsUsedBy";

  /**
   *
   */
  static final String USES_CAP_PROPERTY_URI = ONTOLOGY_URI + "#usesCapability";

  /**
   *
   */
  static final String UPTIME_CAP = ONTOLOGY_URI + "#UptimeCapability";

  static final String FREE_MEM_CAP = ONTOLOGY_URI + "#FreeMemoryCapability";
  static final String TOTAL_MEM_CAP = ONTOLOGY_URI + "#TotalMemoryCapability";
  static final String USED_MEM_CAP = ONTOLOGY_URI + "#UsedMemoryCapability";
  static final String CACHED_MEM_CAP = ONTOLOGY_URI + "#CachedMemoryCapability";

  static final String LOAD_1MIN_CAP = ONTOLOGY_URI + "#Load1minCapability";
  static final String LOAD_5MIN_CAP = ONTOLOGY_URI + "#Load5minCapability";
  static final String LOAD_15MIN_CAP = ONTOLOGY_URI + "#Load15minCapability";


  static final String LIVE_THREADS_CAP = ONTOLOGY_URI + "#LiveThreadsCountCapability";
  static final String STARTED_THREADS_CAP = ONTOLOGY_URI + "#TotalStartedThreadsCountCapability";
  static final String OPEN_FILE_DESCRIPTOR_CAP = ONTOLOGY_URI + "#OpenFileDescriptorCountCapability";
  static final String TOTAL_CPU_TIME_CAP = ONTOLOGY_URI + "#TotalCPUTimeCapability";

  static final String THREAD_BLOCKED_COUNT_CAP = ONTOLOGY_URI + "#ThreadBlockedCountCapability";
  static final String THREAD_BLOCKED_TIME_CAP = ONTOLOGY_URI + "#ThreadBlockedTimeCapability";
  static final String THREAD_WAITED_COUNT_CAP = ONTOLOGY_URI + "#ThreadWaitedCountCapability";
  static final String THREAD_WAITED_TIME_CAP = ONTOLOGY_URI + "#ThreadWaitedTimeCapability";

  static final String THREAD_CPU_TIME_CAP = ONTOLOGY_URI + "#ThreadCPUTimeCapability";
  static final String THREAD_USER_TIME_CAP = ONTOLOGY_URI + "#ThreadUserTimeCapability";

  static final String CLASSES_LOADED_TOTAL_CAP = ONTOLOGY_URI + "#TotalLoadedClassesCapability";
  static final String CLASSES_UNLOADED_TOTAL_CAP = ONTOLOGY_URI + "#TotalUnloadedClassesCapability";
  static final String CLASSES_LOADED_CAP = ONTOLOGY_URI + "#LoadedClassesCapability";
  static final String HEAP_USAGE_CAP = ONTOLOGY_URI + "#HeapUsageCapability";
  static final String NON_HEAP_USAGE_CAP = ONTOLOGY_URI + "#NonHeapUsageCapability";

  static final String GC_COUNT_CAP = ONTOLOGY_URI + "#CollectionCountCapability";
  static final String GC_TIME_CAP = ONTOLOGY_URI + "#CollectionTimeCapability";
  static final String TOTAL_COMPILATION_TIME_CAP = ONTOLOGY_URI + "#TotalCompilationTimeCapability";


  static final String TOTAL_CALL_TIMES = ONTOLOGY_URI + "#TotalCallTimeCapability";
  static final String TOTAL_CALLS_COUNT = ONTOLOGY_URI + "#TotalCallCountCapability";



}
