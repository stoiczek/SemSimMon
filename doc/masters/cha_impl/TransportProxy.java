package pl.edu.agh.semsimmon.common.api.transport;

import pl.edu.agh.semsimmon.common.api.resource.IResourceDiscoveryListener;
import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;

import java.util.List;
import java.util.Map;

public interface TransportProxy {

  CapabilityValue getCapabilityValue(Resource resource, String capabilityType)
      throws TransportException;

  Map<String, CapabilityValue> getCapabilityValues(Resource resource, 
												   List<String> capabilityUris)
      throws TransportException;

  boolean hasCapability(Resource resource, String capabilityType)
      throws TransportException;

  void addResourceDiscoveryListener(IResourceDiscoveryListener listener);

  void removeTransportProxyListener(IResourceDiscoveryListener listener);

  void registerResource(Resource resource) throws TransportException;

  void unregisterResource(Resource resource) throws TransportException;

  boolean isResourceSupported(Resource resource);

  void discoverChildren(Resource resource, List<String> types) throws TransportException;

  List<Resource> discoverDirectChildren(Resource resource, String type) 
	  throws TransportException;

  void stopResource(Resource resource) throws TransportException;

  void pauseResource(Resource resource) throws TransportException;

  void resumeResource(Resource resource) throws TransportException;

}
