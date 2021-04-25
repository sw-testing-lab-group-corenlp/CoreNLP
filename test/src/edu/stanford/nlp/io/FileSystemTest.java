package edu.stanford.nlp.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static edu.stanford.nlp.io.FileSystem.*;

/**
 * Test the FileSystem class.
 *
 * @author Julius Willems
 */
public class FileSystemTest {

  /**
   * Tests copyFile method by comparing original and copied content.
   *
   * @throws IOException
   */
  @Test
  public void testCopyFileContentMatch() throws IOException {
    String original = "This is the content of file 1.";
    File file1 = new File("file1.txt");
    File file2 = new File("file2.txt");
    FileWriter fw = new FileWriter(file1);
    fw.write(original);
    fw.close();

    copyFile(file1, file2);

    String copy = new Scanner(file2).nextLine();
    file1.delete();
    file2.delete();

    Assert.assertEquals("Content form file 1 and file 2 didn't match.", original, copy);
  }

  /**
   * Test the deletion of a created directory.
   */
  @Test
  public void testDeleteDir() {
    String path = "directory";
    File dir = new File(path);
    dir.mkdir();

    boolean success = deleteDir(dir) && !dir.exists();

    Assert.assertTrue("Deletion of directory failed.", success);
  }

  /**
   * Test the creation of a directory.
   */
  @Test
  public void testMkdirOrFail() {
    String path = "directory";

    mkdirOrFail(path);
    boolean success = new File(path).isDirectory();

    Assert.assertTrue("Creation of directory failed.", success);
  }
}