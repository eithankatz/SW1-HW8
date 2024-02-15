package il.ac.tau.cs.sw1.ex8.histogram;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HistogramTester {
    @Test
    public void testAddGetItem() {
        /* Add String value one time */
        final String stringValue = "Chrysalis";
        HashMapHistogram<String> stringHist = new HashMapHistogram<>();
        assertEquals(0, stringHist.getCountForItem(stringValue));
        stringHist.addItem(stringValue);
        assertEquals(1, stringHist.getCountForItem(stringValue));

        /* Add an int value 5 times and another int value 10 times */
        HashMapHistogram<Integer> intHist = new HashMapHistogram<>();
        final int[][] intValues = new int[][]{{1337, 5}, {0x1337, 10}};
        for (int i = 0; i < intValues.length; ++i) {
            for (int j = 0; j < intValues[i][1]; ++j) {
                intHist.addItem(intValues[i][0]);
            }
        }

        for (int i = 0; i < intValues.length; ++i) {
            assertEquals(intValues[i][1], intHist.getCountForItem(intValues[i][0]));
        }
        // get from an empty hist
        HashMapHistogram<Integer> emptyHist = new HashMapHistogram<>();
        assertEquals(emptyHist.getCountForItem(1), 0);
        assertEquals(emptyHist.getCountForItem(null), 0);
    }

    @Test
    public void testAddRemoveItem() {
        /* Add int item 42 times */
        final int intValue = Integer.reverseBytes(0x7F454C46);
        HashMapHistogram<Integer> intHist = new HashMapHistogram<>();
        for (int i = 0; i < 42; ++i) {
            intHist.addItem(intValue);
        }

        /* Remove int item 42 times */
        for (int i = 0; i < 42; ++i) {
            assertDoesNotThrow(
                () -> intHist.removeItem(intValue),
                String.format("Remove item threw exception unexpectedly in iteration #%d", i)
            );
        }
        	
        /* Remove item when it does not exist*/
        assertTrue(intHist.getCountsSum() == 0);

        // Remove from an empty hist
        HashMapHistogram<Integer> hist = new HashMapHistogram<>();
        assertTrue(hist.removeItem(3) == false);

    }

    @Test
    public void testAddAll() {
        HashMapHistogram<Boolean> boolHist = new HashMapHistogram<>();
        List<Boolean> values =
            ("00000000001100010000000000110101000000000011000000000000001101000000000000110110000000000010001100000000001"
            + "000000000010111100000000001011101101100000101110101010000010111011111000000000010000000000101110100000000"
            + "010111100001000001011101010100000101111010000000000000100000000001011101110000000101110100110000010111010"
            + "001000001011110100000000000001000000000010111010000000001011101010100000000001000000000010111011100000001"
            + "011101010000000101111010000000010111100010000001011101100100000101111010010000000000100000000001011101000"
            + "100000101111000010000010111100100000001011110100000000101110110010000010111010100000000000011111100000000"
            + "000010100000010111010000000001011101011000000000001000000000010111010100000001011101000100000101110110010"
            + "000010111010000000001011101010100000000001000000000010111011100000001011110000100000101111001000000010111"
            + "101000000001011101100100000101110110010000010111101010000000000010000000000101110111100000010111010011000"
            + "001011110001000000101110110010000000000100000000001011101010000000101110101110000010111011001000001011101"
            + "100100000101110111010000000000100000000001011101010100000101111010000000010111100100000001011101010100000"
            + "101110100000000010111010100000000000010000000000101111010100000010111010101000001011101101100000101110110"
            + "010000000000101110000000000010000000000101111001000000010111010000000001011110011100000101110110010000010"
            + "111100000000001011101001000000000001000000000010111101010000001011101010100000101110110110000010111011001"
            + "000000000010111000000000000010100000010111100111000001011110100100000101110101000000000000100000000001011"
            + "101110000000101110110010000000000100000000001011101110000000101110101110000010111101001000001011101010100"
            + "000101110100010000000000100000000001011110001000000101110111000000000000100000000001011101000100000101111"
            + "000100000010111011100000000000010000000000101110101110000010111011001000001011101100100000101110111010000"
            + "000000100000000001011101100100000101110101010000010111101010000001011110100000000000001000000000010111011"
            + "110000001011110100000000101111000100000010111011001000001011110100100000000001011000000000000100000000001"
            + "011101010100000101110100010000010111100100000001011110100000000101110110000000000000100000000001011110001"
            + "000000101110111000000000000100000000001011101000100000101111000100000010111011100000000000010000000000101"
            + "110101110000010111011001000001011101100100000101110111010000000000100000000001011101100100000101110101010"
            + "000010111101010000001011110100000000000001000000000010111100100000001011101100000000101111001000000010111"
            + "01100000000101110111110000000000101110").chars().mapToObj(c -> c == '1').collect(Collectors.toList());

        boolHist.addAll(values);
        assertEquals(1691, boolHist.getCountForItem(false));
        assertEquals(869, boolHist.getCountForItem(true));

        boolHist.addAll(Arrays.asList(false, false, true, false, true, true, true, false));
        assertEquals(1695, boolHist.getCountForItem(false));
        assertEquals(873, boolHist.getCountForItem(true));
    }

    @Test
    public void testClear() {
        HashMapHistogram<String> hist = new HashMapHistogram<>();
        hist.addAll(Arrays.asList("To be fair, you have to have a very high IQ to understand Rick and Morty".split(" ")));
        assertEquals(2, hist.getCountForItem("to"));

        hist.clear();
        assertEquals(0, hist.getCountForItem("to"));
        assertEquals(0, hist.getCountForItem("IQ"));
        assertEquals(0, hist.getCountForItem("fair"));
    }

    @Test
    public void testGetItemSet() {
        HashMapHistogram<Integer> hist = new HashMapHistogram<>();
        hist.addAll(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8));
        Set<Integer> items = hist.getItemsSet();
        assertTrue(new HashSet<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9)).equals(items));

        hist.addAll(Arrays.asList(4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5, 0));
        items = hist.getItemsSet();
        assertTrue(new HashSet<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9)).equals(items));

        hist.addAll(Arrays.asList()); // add an empty collection
        items = hist.getItemsSet();
        assertTrue(new HashSet<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9)).equals(items));
    }

   
    @Test
    public void testIterator() {
        HashMapHistogram<Integer> hist = new HashMapHistogram<>();
        /*
         * 0: 8 times
         * 3: 6 times
         * 1: 4 times
         * 2: 4 times
         * 6: 4 times
         * 4: 2 times
         * 5: 2 times
         * 7: 1 time
         * 9: 1 times
         */
        List<Integer> items = Arrays.asList(
            1, 2, 1, 2, 1, 2, 1, 2,
            3, 3, 4, 3, 3, 4, 3, 3,
            0, 0, 0, 0, 0, 0, 0, 0,
            9, 7, 5, 6, 5, 6, 6, 6
        );
        hist.addAll(items);
        
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 8);
        map.put(3, 6);
        map.put(1, 4);
        map.put(2, 4);
        map.put(6, 4);
        map.put(4, 2);
        map.put(5, 2);
        map.put(7, 1);
        map.put(9, 1);
        
        
        Iterator<Map.Entry<Integer, Integer>> iterator = hist.iterator();
        Map.Entry<Integer, Integer> entry = iterator.next();
        assertTrue(entry.getKey() == 0 && entry.getValue() == 8);
        
        entry = iterator.next();
        assertTrue(entry.getKey() == 1 && entry.getValue() == 4);
        
        entry = iterator.next();
        assertTrue(entry.getKey() == 2 && entry.getValue() == 4);
        
        entry = iterator.next();
        assertTrue(entry.getKey() == 3 && entry.getValue() == 6);
        
        entry = iterator.next();
        assertTrue(entry.getKey() == 4 && entry.getValue() == 2);

        entry = iterator.next();
        assertTrue(entry.getKey() == 5 && entry.getValue() == 2);
        
        entry = iterator.next();
        assertTrue(entry.getKey() == 6 && entry.getValue() == 4);
        
        entry = iterator.next();
        assertTrue(entry.getKey() == 7 && entry.getValue() == 1);
        
        entry = iterator.next();
        assertTrue(entry.getKey() == 9 && entry.getValue() == 1);
    }

    @Test
    public void testTeachingAssistantsTest() {
        List<Integer> intLst = Arrays.asList(1, 2, 1, 2, 3, 4, 3, 1);
        IHistogram<Integer> intHist = new HashMapHistogram<>();
        for (int i : intLst) {
            intHist.addItem(i);
        }
        assertEquals(3, intHist.getCountForItem(1));
        assertEquals(0, intHist.getCountForItem(5));

        Iterator<Map.Entry<Integer, Integer>> intHistIt = intHist.iterator();
        List<Map.Entry<Integer, Integer>> tmpList = new ArrayList<>();
        while (intHistIt.hasNext()) {
            tmpList.add(intHistIt.next());
        }
        assertEquals(1, tmpList.get(0).getKey());
        assertEquals(4, tmpList.size());

        IHistogram<String> stringHist = new HashMapHistogram<>();
        IHistogram<String> anotherHist = new HashMapHistogram<>();

        stringHist.addItem("abc");
        stringHist.addItem("bb");
        stringHist.addItem("abc");
        stringHist.addItem("de");
        stringHist.addItem("abc");
        stringHist.addItem("aa");
        stringHist.addItem("de");
        assertEquals(3, stringHist.getCountForItem("abc"));

        assertTrue(stringHist.removeItem("abba") == false);


        assertEquals(2, stringHist.getCountForItem("de"));

        Iterator<Map.Entry<String, Integer>> it = stringHist.iterator();
        /*
         * the order of the returned items should be: "aa", "bb", "de", "abc" aa
         * " and "bb" both appear 1 time, so in this case we sort by the
         * natural order of the elements "aa" and "bb". This is why "aa" should
         * appear before "bb"
         */
        assertTrue(it.next().getKey().equals("aa"));
        assertTrue(it.next().getKey().equals("abc"));
        assertTrue(it.next().getKey().equals("bb"));
        assertTrue(it.next().getKey().equals("de"));

    }
}