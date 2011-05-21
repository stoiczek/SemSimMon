package pl.edu.agh.semsimmon.registries.ocmg.probe.mem;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;

/**
* Created by IntelliJ IDEA.
* User: tkozak
* Date: 21.05.11
* Time: 20:34
* To change this template use File | Settings | File Templates.
*/
public enum MemoryProbeType {

  TOTAL, FREE, USED, CACHED;

  static MemoryProbeType parseFromUri(String uri) {
    if(uri.compareTo(KnowledgeConstants.FREE_MEM_CAP) == 0) {
      return  FREE;
    } else if(uri.compareTo(KnowledgeConstants.USED_MEM_CAP) == 0) {
      return USED;
    } else if(uri.compareTo(KnowledgeConstants.CACHED_MEM_CAP) == 0) {
      return CACHED;
    } else if(uri.compareTo(KnowledgeConstants.TOTAL_MEM_CAP) == 0) {
      return TOTAL;
    }
    throw new IllegalArgumentException("Unsupported type");
  }

}
