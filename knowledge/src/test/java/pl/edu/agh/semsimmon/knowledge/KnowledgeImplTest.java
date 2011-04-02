package pl.edu.agh.semsimmon.knowledge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeConstants;
import pl.edu.agh.semsimmon.knowledge.impl.KnowledgeImpl;

import java.util.List;

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

  public void setKnowledge(KnowledgeImpl knowledge) {
    this.knowledge = knowledge;
  }
}
