package pl.edu.agh.semsimmon.registries.jmx.discovery.node;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.resource.ResourcePropertyNames;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.jmx.discovery.DiscoveryAgent;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 15:01 11-07-2010
 */
public class JvmDiscoveryAgent implements DiscoveryAgent {

  @Override
  public List<Resource> discoveryChildren(MBeanServerConnection connection, Resource parent, String type) throws IOException {
    if (!parent.getTypeUri().equals(KnowledgeConstants.NODE_URI)) {
      throw new IllegalArgumentException("Got invalid parent resource type: " + parent.getTypeUri());
    }
    final RuntimeMXBean runtimeProxy = ManagementFactory.newPlatformMXBeanProxy
        (connection, ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
    final Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(ResourcePropertyNames.Jvm.BOOT_CLASSPATH, runtimeProxy.getBootClassPath());
    properties.put(ResourcePropertyNames.Jvm.CLASSPATH, runtimeProxy.getClassPath());
    properties.put(ResourcePropertyNames.Jvm.INPUT_ARGS, runtimeProxy.getInputArguments());
    properties.put(ResourcePropertyNames.Jvm.LIB_PATH, runtimeProxy.getLibraryPath());
    final String instanceName = runtimeProxy.getName();
    properties.put(ResourcePropertyNames.Jvm.NAME, instanceName);
    properties.put(ResourcePropertyNames.Jvm.SPEC_NAME, runtimeProxy.getSpecName());
    properties.put(ResourcePropertyNames.Jvm.SPEC_VENDOR, runtimeProxy.getSpecVendor());
    properties.put(ResourcePropertyNames.Jvm.SPEC_VERSION, runtimeProxy.getSpecVersion());
    properties.put(ResourcePropertyNames.Jvm.VM_NAME, runtimeProxy.getVmName());
    properties.put(ResourcePropertyNames.Jvm.VM_VENDOR, runtimeProxy.getVmVendor());
    properties.put(ResourcePropertyNames.Jvm.VM_VERSION, runtimeProxy.getVmVersion());
    properties.put(ResourcePropertyNames.Jvm.START_TIME, new Date(runtimeProxy.getStartTime()));
    final Resource jvmResource = new Resource(parent.getUri() + "/JVM_" + instanceName, KnowledgeConstants.JVM_URI, properties);
    return Arrays.asList(jvmResource);
  }
}
