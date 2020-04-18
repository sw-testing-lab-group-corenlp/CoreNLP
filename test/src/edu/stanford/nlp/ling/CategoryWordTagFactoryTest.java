package edu.stanford.nlp.ling;

import junit.framework.TestCase;
import org.junit.Test;

public class CategoryWordTagFactoryTest extends TestCase {

  public void testNewLabelMethodReturnsInstanceOfCategoryWordTag(){
    CategoryWordTag tag = new CategoryWordTag("A", "B", "C");
    CategoryWordTagFactory lf = new CategoryWordTagFactory();
    Label label = lf.newLabel(tag);

    assertTrue(label instanceof CategoryWordTag);
  }

  @Test
  public void testNewLabelMethodReturnsInstanceOfCategoryWordTag(){
      CategoryWordTag tag = new CategoryWordTag("A", "B", "C");
      CategoryWordTagFactory lf = new CategoryWordTagFactory();
      Label label = lf.newLabel(tag);
      assertTrue(label instanceof CategoryWordTag);
  }
}
