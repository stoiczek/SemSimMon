package pl.edu.agh.semsimmon.knowledge;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeUtils;
import pl.edu.agh.semsimmon.knowledge.impl.KnowledgeImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.testng.Assert.assertEquals;

/**
 * TODO description
 *
 * @author tkozak
 *         Created at 15:23 10-07-2010
 */

@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class KnowledgeImplTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private KnowledgeImpl knowledge;


  @Test
  public void nodeResourcesTest() {
    List<String> children = knowledge.getChildrenResourceTypes(KnowledgeConstants.NODE_URI);
    assertEquals(8, children.size());
  }

  @Test
  public void reloadTest() throws IOException {
    final Resource resource = applicationContext.getResource("semsimmon-test-onto.owl");
    knowledge.reloadOntology(KnowledgeUtils.serializeOntology(resource.getInputStream()));
    List<String> children = knowledge.getChildrenResourceTypes(KnowledgeConstants.NODE_URI);
    assertEquals(children.size(), 5);
  }

  @Test
  public void serializationTest() throws IOException {
    String input = "snjfkelmfgnjkrkmtfdjgtrfkl";
    String output = IOUtils.toString( KnowledgeUtils.deserializeOntology(KnowledgeUtils.serializeOntology(IOUtils.toInputStream(input))));
    assertEquals(output, input);
  }


  public void setKnowledge(KnowledgeImpl knowledge) {
    this.knowledge = knowledge;
  }
}
