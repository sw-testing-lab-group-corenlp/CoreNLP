package edu.stanford.nlp.io;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Random;

import static edu.stanford.nlp.io.IOUtils.*;

/**
 * Generate randomized input data for testing I/O operations.
 *
 * @author Julius Willems
 */
public class IOUtilsFuzzTest {

  private static String filePath;
  private static final String[] fileTypes = new String[]{"txt", "json", "csv"};
  private static final int upperBound = 64;
  private Random random = new Random();
  private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String digits = "0123456789";
  private static final String lower = upper.toLowerCase();
  private static final String alphanum = upper + lower + digits;
  private static char[] symbols = alphanum.toCharArray();

  /**
   * Generates a randomized filePath.
   *
   * @throws Exception
   */
  @Before
  public void setUp() {
    int fileNameLength = this.random.nextInt(this.upperBound);
    String fileName = this.generateRandomString(fileNameLength, this.symbols);
    String fileType = this.fileTypes[this.random.nextInt(this.fileTypes.length)];
    this.filePath = String.join(".", fileName, fileType);
  }

  /**
   * Deletes (if exists) any file with the given filePath.
   *
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    try {
      Files.deleteIfExists(Paths.get(this.filePath));
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Generates a random string of a given length.
   *
   * @param length  {int}
   * @param symbols {char[]} the set of characters, the string is composed by.
   * @return {String}
   */
  private String generateRandomString(int length, char[] symbols) {
    if (length <= 0) {

      return "test";
    }

    char[] buffer = new char[length];
    for (int i = 0; i < length; i++) {
      int j = this.random.nextInt(symbols.length);
      buffer[i] = this.symbols[j];
    }

    return new String(buffer);
  }

  /**
   * Writes a randomly generated string to a randomly named file.
   */
  @Test
  public void testWriteObjectToNewFile() throws IOException {
    String content = this.generateRandomString(this.random.nextInt(64), this.symbols);

    File file = writeObjectToFile(content, this.filePath);
    boolean exists = Files.exists(Paths.get(this.filePath)) && file != null;

    Assert.assertTrue(exists);
  }

  /**
   * Writes a randomly generated string to an existing file.
   */
  @Test
  public void testWriteObjectToExistingFile() throws IOException {
    String content = this.generateRandomString(this.random.nextInt(64), this.symbols);
    File file = new File(this.filePath);

    file = writeObjectToFile(content, file);
    boolean exists = Files.exists(Paths.get(this.filePath)) && file != null;

    Assert.assertTrue(exists);
  }
}