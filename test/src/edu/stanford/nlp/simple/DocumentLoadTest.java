package edu.stanford.nlp.simple;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentLoadTest {

  private String sentence;
  private Document document;
  private long baseLine;

  @Before
  public void setUp() {
    this.sentence = "Lucy is in the sky with diamonds.";
    this.document = new Document(sentence);

    long start = System.nanoTime();
    document.sentences();
    long end = System.nanoTime();

    this.baseLine = end - start;
  }

  @Test
  public void testSentencesLessThanLinear() {
    String input = this.sentence;
    long epsilon = 1000;

    for (int i = 1; i <= 100; i++) {
      Document document = new Document(input);
      input += this.sentence;

      long start = System.nanoTime();
      document.sentences();
      long end = System.nanoTime();
      long elapsed = end - start;

      Assert.assertTrue((elapsed + epsilon) < this.baseLine * i);
    }
  }
}