package edu.stanford.nlp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Christopher Manning
 * @author John Bauer
 */
public class ArrayUtilsTest {

  private int[] sampleGaps = {1, 5, 6, 10, 17, 22, 29, 33, 100, 1000, 10000, 9999999};
  private int[] sampleBadGaps = {1, 6, 5, 10, 17};

  @Test
  public void testEqualContentsInt() {
    org.junit.Assert.assertTrue(ArrayUtils.equalContents(sampleGaps, sampleGaps));
    org.junit.Assert.assertTrue(ArrayUtils.equalContents(sampleBadGaps, sampleBadGaps));
    org.junit.Assert.assertFalse(ArrayUtils.equalContents(sampleGaps, sampleBadGaps));
  }

  @Test
  public void testGaps() {
    byte[] encoded = ArrayUtils.gapEncode(sampleGaps);
    int[] decoded = ArrayUtils.gapDecode(encoded);
    org.junit.Assert.assertTrue(ArrayUtils.equalContents(decoded, sampleGaps));

    try {
      ArrayUtils.gapEncode(sampleBadGaps);
      throw new RuntimeException("Expected an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // yay, we passed
    }
  }

  @Test
  public void testDelta() {
    byte[] encoded = ArrayUtils.deltaEncode(sampleGaps);
    int[] decoded = ArrayUtils.deltaDecode(encoded);
    org.junit.Assert.assertTrue(ArrayUtils.equalContents(decoded, sampleGaps));

    try {
      ArrayUtils.deltaEncode(sampleBadGaps);
      throw new RuntimeException("Expected an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // yay, we passed
    }
  }

  @Test
  public void testRemoveAt() {
    String[] strings = new String[]{"a", "b", "c"};
    String[] emptyStrings = null;
    strings = (String[]) ArrayUtils.removeAt(strings, 2);
    String[] stringsUnModified = (String[]) ArrayUtils.removeAt(strings, 10);
    int i = 0;
    for (String string : strings) {
      if (i == 0) {
        assertEquals("a", string);
      } else if (i == 1) {
        assertEquals("b", string);
      } else {
        org.junit.Assert.fail("Array is too big!");
      }
      i++;
    }
    assertEquals(null, ArrayUtils.removeAt(emptyStrings, 3));
    assertEquals(strings, ArrayUtils.removeAt(strings, 10));
  }

  @Test
  public void testAsSet() {
    String[] items = {"larry", "moe", "curly"};
    Set<String> set = new HashSet<>(Arrays.asList(items));
    assertEquals(set, ArrayUtils.asSet(items));
  }


  @Test
  public void testgetSubListIndex() {
    String[] t1 = {"this", "is", "test"};
    String[] t2 = {"well","this","is","not","this","is","test","also"};
    assertEquals(4,(ArrayUtils.getSubListIndex(t1, t2).get(0).intValue()));
    String[] t3 = {"cough","increased"};
    String[] t4 = {"i","dont","really","cough"};
    assertEquals(0, ArrayUtils.getSubListIndex(t3, t4).size());
    String[] t5 = {"cough","increased"};
    String[] t6 = {"cough","aggravated"};
    assertEquals(0, ArrayUtils.getSubListIndex(t5, t6).size());
    String[] t7 = {"cough","increased"};
    String[] t8 = {"cough","aggravated","cough","increased","and","cough", "increased","and","cough","and","increased"};
    assertEquals(2, ArrayUtils.getSubListIndex(t7, t8).get(0).intValue());
    assertEquals(5, ArrayUtils.getSubListIndex(t7, t8).get(1).intValue());
    String[] t9 = {"cough","aggravated","cough","increased","and","cough", "increased","and","cough","and","increased"};
    String[] t10 = {"cough","increased"};
    assertEquals(null, ArrayUtils.getSubListIndex(t9, t10));
  }

  @Test
  public void testFlatten() {
    double[][] input = {{1.0}, {2.0}, {3.0, 4.0}};
    double[] flattened = ArrayUtils.flatten(input);
    assertEquals(4, flattened.length);
    assertEquals(3.0, flattened[2], 0.0);
  }

  @Test
  public void testTo2D() {
    double[] input = {1.0, 2.0, 3.0, 4.0};
    double[][] in2d = ArrayUtils.to2D(input, 2);
    assertEquals(2, in2d.length);
  }

  @Test
  public void testRemoveAtDouble() {
    double[] input = {1.0, 2.0, 3.0, 4.0};
    double[] nullInput = null;
    double[] edited = ArrayUtils.removeAt(input, 3);
    double[] nothingToRemove = ArrayUtils.removeAt(input, 100);
    boolean inputWasNotModified = input.length == 4 && input[3] == 4.0;
    assertTrue("Removal should not modify input array", inputWasNotModified);
    assertEquals(3, edited.length);
    assertEquals(4, nothingToRemove.length);
    assertEquals(null, ArrayUtils.removeAt(nullInput, 5));
  }
  
  @Test
  public void testToString() {
    int[][] input = {{2, 3, 5, 7}, {11, 13, 17, 19}};
    assertEquals("[[2, 3, 5, 7],[11, 13, 17, 19]]", ArrayUtils.toString(input));
  }

  @Test
  public void testEqualContent() {
    int[][] input = {{2, 3, 5, 7}, {11, 13, 17, 19}};
    int[][] sameInput = {{2, 3, 5, 7}, {11, 13, 17, 19}};
    int[][] differentContent = {{2, 4, 5, 7}, {11, 13, 17, 19}};
    int[][] differentLength = {{2, 4, 5, 7}, {11, 13, 17, 19}, {200}};
    int[][] emptyInput = null;
    assertFalse(ArrayUtils.equalContents(input, differentContent));
    assertFalse(ArrayUtils.equalContents(input, differentLength));
    assertFalse(ArrayUtils.equalContents(input, emptyInput));
    assertFalse(ArrayUtils.equalContents(emptyInput, input));
    assertTrue(ArrayUtils.equalContents(input, sameInput));
    assertTrue(ArrayUtils.equalContents(null, emptyInput));
  }

  @Test
  public void testEqualsDouble() {
    double[][] input = {{2.0, 3.0, 5.0, 7.0}, {11.0, 13.0, 17.0, 19.0}};
    double[][] sameInput = {{2.0, 3.0, 5.0, 7.0}, {11.0, 13.0, 17.0, 19.0}};
    double[][] differentContent = {{2.0, 4.0, 5.0, 7.0}, {11.0, 13.0, 17.0, 19.0}};
    double[][] differentLength = {{2.0, 4.0, 5.0, 7.0}, {11.0, 13.0, 17.0, 19.0}, {200.0}};
    double[][] emptyInput = null;
    assertFalse(ArrayUtils.equals(input, differentContent));
    assertFalse(ArrayUtils.equals(input, differentLength));
    assertFalse(ArrayUtils.equals(input, emptyInput));
    assertFalse(ArrayUtils.equals(emptyInput, input));
    assertTrue(ArrayUtils.equals(input, sameInput));
    assertTrue(ArrayUtils.equals(null, emptyInput));
  }

  @Test
  public void testEqualsBool() {
    boolean[][] input = {{true, false}, {false, true}};
    boolean[][] sameInput = {{true, false}, {false, true}};
    boolean[][] differentContent = {{true, true}, {true, true}};
    boolean[][] differentLength = {{true, true}, {true, true}, {true}, {true}};
    boolean[][] emptyInput = null;
    assertFalse(ArrayUtils.equals(input, differentContent));
    assertFalse(ArrayUtils.equals(input, differentLength));
    assertFalse(ArrayUtils.equals(input, emptyInput));
    assertFalse(ArrayUtils.equals(emptyInput, input));
    assertTrue(ArrayUtils.equals(input, sameInput));
    assertFalse(ArrayUtils.equals(null, emptyInput));
  }
  
  @Test
  public void testContains() {
    String[] objects = {"hello", "there"};
    assertTrue(ArrayUtils.contains(objects, "there")); 
    assertFalse(ArrayUtils.contains(objects, "hi"));
  }

  @Test
  public void testConcatenate() {
    String[] first = {"hello", "there"};
    String[] second = {"how", "are", "you"};
    String[] expected = {"hello", "there", "how", "are", "you"};
    assertArrayEquals(expected, ArrayUtils.concatenate(first, second)); 
  }

  @Test
  public void testFilter() {
    String[] mixedWords = {"humongous", "and", "short", "words"};
    String[] shortWords = {"ping", "pong"};
    Predicate<String> isShortWord = e -> e.length() < 6;
    String[] filtered = ArrayUtils.filter(mixedWords, isShortWord);
    assertEquals(3, filtered.length);
    filtered = ArrayUtils.filter(shortWords, isShortWord);
    assertEquals(2, filtered.length);
  }
  
  @Test
  public void testAsImmutableSet() {
    String[] words = {"just", "some", "words"};
    String[] word = {"one"};
    String[] empty = {};
    Set<String> collection = ArrayUtils.asImmutableSet(words);
    Set<String> singleton = ArrayUtils.asImmutableSet(word);
    try {
      collection.clear();
      Assert.fail("Should not be possible to mutate unmodifiable Set");
    } catch(Exception e) {
    }
    try {
      singleton.clear();
      Assert.fail("Should not be possible to mutate singleton");
    } catch(Exception e) {
    }
    assertEquals(Collections.emptySet(), ArrayUtils.asImmutableSet(empty));
  }
  
  @Test
  public void testFillDouble2D() {
    double[][] toFill = {{2.0, 2.0}, {2.0}, {2.0}};
    double[][] expected = {{1.0, 1.0}, {1.0}, {1.0}};
    ArrayUtils.fill(toFill, 1.0);
    assertArrayEquals(expected, toFill);
  }

  @Test
  public void testFillDouble3D() {
    double[][][] toFill = {{{2.0, 2.0}}, {{2.0}}, {{2.0}}};
    double[][][] expected = {{{1.0, 1.0}}, {{1.0}}, {{1.0}}};
    ArrayUtils.fill(toFill, 1.0);
    assertArrayEquals(expected, toFill);
  }
  
  @Test
  public void testFillDouble4D() {
    double[][][][] toFill = {{{{2.0, 2.0}}}, {{{2.0}}}, {{{2.0}}}};
    double[][][][] expected = {{{{1.0, 1.0}}}, {{{1.0}}}, {{{1.0}}}};
    ArrayUtils.fill(toFill, 1.0);
    assertArrayEquals(expected, toFill);
  }

  @Test
  public void testFillBool2D() {
    boolean[][] toFill = {{false, true}, {true}, {false}};
    boolean[][] expected = {{false, false}, {false}, {false}};
    ArrayUtils.fill(toFill, false);
    assertArrayEquals(expected, toFill);
  }

  @Test
  public void testFillBool3D() {
    boolean[][][] toFill = {{{false, true}}, {{true}}, {{false}}};
    boolean[][][] expected = {{{false, false}}, {{false}}, {{false}}};
    ArrayUtils.fill(toFill, false);
    assertArrayEquals(expected, toFill);
  }

  @Test
  public void testFillBool4D() {
    boolean[][][][] toFill = {{{{false, true}}}, {{{true}}}, {{{false}}}};
    boolean[][][][] expected = {{{{false, false}}}, {{{false}}}, {{{false}}}};
    ArrayUtils.fill(toFill, false);
    assertArrayEquals(expected, toFill);
  }

  @Test
  public void testToDoubleFloat() {
    float[] input = {1.0f, 2.0f, 3.0f};
    double[] expected = {1.0, 2.0, 3.0};
    assertEquals(expected.length, ArrayUtils.toDouble(input).length);
    assertEquals(expected[1], ArrayUtils.toDouble(input)[1], 0);
  }
  
  @Test
  public void testToDoubleInt() {
    int[] input = {1, 2, 3, 4, 5};
    double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};
    assertEquals(expected.length, ArrayUtils.toDouble(input).length);
    assertEquals(expected[1], ArrayUtils.toDouble(input)[1], 0);
  }

  @Test
  public void testAsList() {
    int[] input = {1, 2, 3, 4, 5};
    List<Integer> list = ArrayUtils.asList(input);
    assertEquals(5, list.size());
  }

  @Test
  public void testAsPrimitiveDoubleArray() {
    List<Double> input = Arrays.asList(1.0, 2.0);
    double[] result = ArrayUtils.asPrimitiveDoubleArray(input);
    assertEquals(2, result.length);
    assertEquals(1.0, result[0], 0);
  }

  @Test
  public void testAsPrimitiveIntArray() {
    List<Integer> input = Arrays.asList(1, 2, 3);
    int[] result = ArrayUtils.asPrimitiveIntArray(input);
    assertEquals(3, result.length);
    assertEquals(3, result[2]);
  }
  
  @Test
  public void testCopyLong() {
    long[] longs = {9223372036854775807l, -9223372036854775808l, 3l};
    long[] emptyLongs = null;
    long[] copied = ArrayUtils.copy(longs);
    assertArrayEquals(longs, copied);
    assertEquals(null, ArrayUtils.copy(emptyLongs));
  }

  @Test
  public void testCopyInt() {
    int[] ints = {-2147483648, 2147483647, 5};
    int[] emptyInts = null;
    int[] copied = ArrayUtils.copy(ints);
    assertArrayEquals(ints, copied);
    assertEquals(null, ArrayUtils.copy(emptyInts));
  }

  @Test
  public void testCopyInt2D() {
    int[][] ints = {{-2147483648, 2147483647}, {5}};
    int[][] emptyInts = null;
    int[][] copied = ArrayUtils.copy(ints);
    assertArrayEquals(ints, copied);
    assertEquals(null, ArrayUtils.copy(emptyInts));
  }
  
  @Test
  public void testCopyDouble() {
    double[] doubles = {123.0, 456.0};
    double[] emptyDoubles = null;
    double[] copied = ArrayUtils.copy(doubles);
    assertArrayEquals(doubles, copied, 0);
    assertEquals(null, ArrayUtils.copy(emptyDoubles));
  }

  @Test
  public void testCopyDouble2D() {
    double[][] doubles = {{-2147483648.0, 2147483647.0}, {5.1}};
    double[][] emptyDoubles = null;
    double[][] copied = ArrayUtils.copy(doubles);
    assertArrayEquals(doubles, copied);
    assertEquals(null, ArrayUtils.copy(emptyDoubles));
  }

  @Test
  public void testCopyDouble3D() {
    double[][][] doubles = {{{-2147483648.0, 2147483647.0}}, {{5.1}}};
    double[][][] emptyDoubles = null;
    double[][][] copied = ArrayUtils.copy(doubles);
    assertArrayEquals(doubles, copied);
    assertEquals(null, ArrayUtils.copy(emptyDoubles));
  }

  @Test
  public void testCopyFloat() {
    float[] floats = {-2147483648f, 123f, 5f};
    float[] emptyFloats = null;
    float[] copied = ArrayUtils.copy(floats);
    assertEquals(floats.length, copied.length);
    assertEquals(floats[0], copied[0], 0);
    assertEquals(floats[2], copied[2], 0);
    assertEquals(null, ArrayUtils.copy(emptyFloats));
  }

  @Test
  public void testCopyFloat2D() {
    float[][] floats = {{-2147483648f, 12133333333f}, {123f}, {5f}};
    float[][] emptyFloats = null;
    float[][] copied = ArrayUtils.copy(floats);
    assertEquals(floats.length, copied.length);
    assertEquals(floats[0][1], copied[0][1], 0);
    assertEquals(floats[2][0], copied[2][0], 0);
    assertEquals(null, ArrayUtils.copy(emptyFloats));
  }

  @Test
  public void testCopyFloat3D() {
    float[][][] floats = {{{-2147483648f, 12133333333f}}, {{123f}}, {{5f}}};
    float[][][] emptyFloats = null;
    float[][][] copied = ArrayUtils.copy(floats);
    assertEquals(floats.length, copied.length);
    assertEquals(floats[0][0][1], copied[0][0][1], 0);
    assertEquals(floats[2][0][0], copied[2][0][0], 0);
    assertEquals(null, ArrayUtils.copy(emptyFloats));
  }

  @Test
  public void testToStringDouble2D() {
    double[][] doubles = {{-2147483648.0, 2147483647.0}, {5.1}};
    assertEquals("[[-2.147483648E9, 2.147483647E9],[5.1]]", ArrayUtils.toString(doubles));
  }  

  @Test
  public void testToStringBool2D() {
    boolean[][] bools = {{true, false}, {true}};
    assertEquals("[[true, false],[true]]", ArrayUtils.toString(bools));
  }  

  @Test
  public void testToPrimitiveLong() {
    Long[] longs = {10l, 2333333333334l};
    Long[] emptyLongs = null;
    long[] primitives = ArrayUtils.toPrimitive(longs);
    assertEquals(longs.length, primitives.length);
    assertEquals(longs[1], primitives[1], 0);
    assertEquals(null, ArrayUtils.toPrimitive(emptyLongs));
  }

  @Test
  public void testToPrimitiveInteger() {
    Integer[] integers = {123332, 344};
    Integer[] emptyIntegers = null;
    int[] primitives = ArrayUtils.toPrimitive(integers);
    assertEquals(integers.length, primitives.length);
    assertEquals(integers[1], primitives[1], 0);
    assertEquals(null, ArrayUtils.toPrimitive(emptyIntegers));
  }

  @Test
  public void testToPrimitiveShort() {
    Short[] shorts = {1232, 344};
    Short[] emptyShorts = null;
    short[] primitives = ArrayUtils.toPrimitive(shorts);
    assertEquals(shorts.length, primitives.length);
    assertEquals(shorts[1], primitives[1], 0);
    assertEquals(null, ArrayUtils.toPrimitive(emptyShorts));
  }

  @Test
  public void testToPrimitiveChar() {
    Character[] chars = {'a', 'b', 'c', 'z'};
    Character[] emptyChars = null;
    char[] primitives = ArrayUtils.toPrimitive(chars);
    assertEquals(chars.length, primitives.length);
    assertTrue(chars[0].equals(primitives[0]));
    assertTrue(chars[3].equals(primitives[3]));
    assertEquals(null, ArrayUtils.toPrimitive(emptyChars));
  }

  @Test
  public void testToPrimitiveDouble() {
    Double[] doubles = {1.01d, 3.14d, 4433.33d, 232d};
    Double[] emptyDoubles = null;
    double[] primitives = ArrayUtils.toPrimitive(doubles);
    assertEquals(doubles.length, primitives.length);
    assertTrue(doubles[0].equals(primitives[0]));
    assertTrue(doubles[3].equals(primitives[3]));
    assertEquals(null, ArrayUtils.toPrimitive(emptyDoubles));
  }
  
  @Test
  public void testCompareArrays() {
    class MockComparable implements Comparable {
      @Override
      public int compareTo(Object o) { return 0; }
    }
    MockComparable[] arr1 = {new MockComparable(), new MockComparable()};
    MockComparable[] arr2 = {new MockComparable(), new MockComparable()};
    int arr3 = ArrayUtils.compareArrays(arr1, arr2);
    assertEquals(0, arr3);
  }

  @Test
  public void testToDoubleArray() {
    String[] input = {"2.022d", "1.00001"};
    double[] expected = {2.022d, 1.00001d};
    assertArrayEquals(expected, ArrayUtils.toDoubleArray(input), 0);
  }
  
  @Test
  public void testNormalize() {
    double[] toNormalize = {1.0d, 3.0d};
    double[] expected = {0.25, 0.75};
    assertArrayEquals(expected, ArrayUtils.normalize(toNormalize), 0);
  }
  
  @Test
  public void testSubArray() {
    Integer[] arr = {1, 2, 3, 4, 5, 6};
    Integer[] empty = null;
    Object[] sub = ArrayUtils.subArray(arr, 3, 5);
    assertEquals(2, sub.length);
    sub = ArrayUtils.subArray(arr, 3, 20);
    assertEquals(3, sub.length);
    sub = ArrayUtils.subArray(arr, -3, 20);
    assertEquals(6, sub.length);
    sub = ArrayUtils.subArray(empty, 3, 5);
    assertEquals(null, sub);
    sub = ArrayUtils.subArray(arr, 10, 20);
    assertEquals(0, sub.length);
    sub = ArrayUtils.subArray(arr, 20, 10);
    assertEquals(0, sub.length);
  }
  
  @Test
  public void testCompareBooleanArray() {
    boolean[] arr1 = {true, true, true, false};
    boolean[] arr2 = {false, true, false, true};
    boolean[] arr3 = {};
    assertEquals(1, ArrayUtils.compareBooleanArrays(arr1, arr2));
    assertEquals(-1, ArrayUtils.compareBooleanArrays(arr2, arr1));
    assertEquals(-1, ArrayUtils.compareBooleanArrays(arr3, arr1));
    assertEquals(1, ArrayUtils.compareBooleanArrays(arr1, arr3));
    assertEquals(0, ArrayUtils.compareBooleanArrays(arr1, arr1));
  }
  
  @Test
  public void testToStringDouble() {
    double[] doubles = {2.0, 3.14, 9.999};
    assertEquals("2.0-3.14-9.999", ArrayUtils.toString(doubles, "-"));
  }
}
