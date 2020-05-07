package edu.stanford.nlp.util;

import org.junit.Assert;
import org.junit.Test;

import static edu.stanford.nlp.util.CommandLineTokenizer.tokenize;

/**
 * Test the CommandLineTokenizer used to parse cli commands into array of strings.
 *
 * @author Julius Willems
 */
public class CommandLineTokenizerTest {

  @Test
  public void testTokenizer() {
    String command = "echo apt-get install \"git\" && echo \"installed git\\\\";
    String[] expectedTokens = new String[]{"echo", "apt-get", "install", "git", "&&", "echo", "installed git\\"};

    String[] actualTokens = tokenize(command);

    Assert.assertEquals(expectedTokens, actualTokens);
  }
}