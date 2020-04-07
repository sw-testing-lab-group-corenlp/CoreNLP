package edu.stanford.nlp.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.Generics;
import junit.framework.TestCase;

/**
 * Makes sure that the dependencies generated from the parser output are indexed starting at 1.
 */
public class DependencyIndexITest {

  @Test
  public void testPositions() {
    try {
      // System.err.println();
      // System.err.println("One.");
      // check a tree loaded from a reader, using StringLabelFactory
      Tree tree = (new PennTreeReader(new StringReader("(S (NP (NNP Mary)) (VP (VBD had) (NP (DT a) (JJ little) (NN lamb))) (. .))"), new LabeledScoredTreeFactory(new StringLabelFactory()))).readTree();
      //System.out.println(tree.pennString());
      checkTree(tree);

      // System.err.println("Two.");
      // check a tree created using Tree.valueOf()
      tree = Tree.valueOf("(S (NP (NNP Mary)) (VP (VBD had) (NP (DT a) (JJ little) (NN lamb))) (. .))");
      //System.out.println(tree.pennString());
      checkTree(tree);

      // System.err.println("Three.");
      // check a tree loaded from a reader, using CoreLabelFactory
      tree = (new PennTreeReader(new StringReader("(S (NP (NNP Mary)) (VP (VBD had) (NP (DT a) (JJ little) (NN lamb))) (. .))"), new LabeledScoredTreeFactory(CoreLabel.factory()))).readTree();
      //System.out.println(tree.pennString());
      checkTree(tree);

      // System.err.println("Four.");
      // check a tree generated by the parser
      LexicalizedParser parser = LexicalizedParser.loadModel();
      tree = parser.parse("Mary had a little lamb .");
      // System.out.println(tree.pennString());
      tree.indexLeaves();
      checkTree(tree);

    } catch (IOException e) {
      // this should never happen
      fail("IOException shouldn't happen.");
    }
  }

  private static void checkTree(Tree tree) {
    List<Tree> leaves = tree.getLeaves();
    for (Tree leaf: leaves){
      CoreLabel l = null;
      if (leaf.label() instanceof CoreLabel) l = (CoreLabel) leaf.label();
      if (l != null) {
        // System.err.println(l + " " + l.get(CoreAnnotations.IndexAnnotation.class));
        int index = l.get(CoreAnnotations.IndexAnnotation.class);
        String text = l.get(CoreAnnotations.TextAnnotation.class);
        if(text.equals("Mary")) assertEquals(1, index);
        else if(text.equals("had")) assertEquals(2, index);
        else if(text.equals("a")) assertEquals(3, index);
        else if(text.equals("little")) assertEquals(4, index);
        else if(text.equals("lamb")) assertEquals(5, index);
        else if (text.equals(".")) assertEquals(6, index);
      } else {
        // System.err.println(leaf + " is not a CoreLabel.");
      }
    }
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

    GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
    Collection<TypedDependency> deps = gs.typedDependenciesCCprocessed(GrammaticalStructure.Extras.MAXIMAL);
    // System.out.println(deps);

    // collect all nodes in deps
    Set<IndexedWord> nodes = Generics.newHashSet();
    for (TypedDependency dep: deps) {
      nodes.add(dep.gov());
      nodes.add(dep.dep());
    }

    // check the indices for all nodes
    for (IndexedWord n: nodes) {
      String text = n.value();
      int index = n.get(CoreAnnotations.IndexAnnotation.class);
      if (text.equals("Mary")) assertEquals(1, index);
      else if(text.equals("had")) assertEquals(2, index);
      else if(text.equals("a")) assertEquals(3, index);
      else if(text.equals("little")) assertEquals(4, index);
      else if(text.equals("lamb")) assertEquals(5, index);
      else if (text.equals(".")) assertEquals(6, index);
    }
  }

  public static void main(String[] args) {
    DependencyIndexITest dt = new DependencyIndexITest();
    dt.testPositions();
  }

}
