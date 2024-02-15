package il.ac.tau.cs.sw1.ex8.tfIdf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileTester {
    public static final String INPUT_FOLDER = "c:/resources/additionalFiles";
    FileIndex fIndex = new FileIndex();
    boolean indexed = false;

    private void index() {
        if (!this.indexed) {
            fIndex.indexDirectory(INPUT_FOLDER);
            this.indexed = true;
        }
    }


    @Test
    public void testGetCountInFile() {
        this.index();

        assertDoesNotThrow(() -> assertEquals(3, fIndex.getCountInFile(
                "gregor", "die_verwandlung1.txt"
        )));

        assertDoesNotThrow(() -> assertEquals(28, fIndex.getCountInFile(
                "er", "die_verwandlung1.txt"
        )));

        assertDoesNotThrow(() -> assertEquals(23, fIndex.getCountInFile(
                "und", "die_verwandlung1.txt"
        )));

        assertDoesNotThrow(() -> assertEquals(0, fIndex.getCountInFile(
                "lena", "die_verwandlung1.txt")));

        assertDoesNotThrow(() -> assertEquals(1, fIndex.getCountInFile(
                "a", "just_a.txt"
        )));

        assertDoesNotThrow(() -> assertEquals(0, fIndex.getCountInFile(
                "samsa", "just_a.txt"
        )));

        assertThrows(
            FileIndexException.class,
            () -> fIndex.getCountInFile(
                "יב. קל לראות", "2022b-final-test-answers.txt"
            )
        );
    }

    @Test
    public void testGetNumOfUniqueWordsInFile() {
        this.index();
        assertDoesNotThrow(() -> assertEquals(1498, fIndex.getNumOfUniqueWordsInFile("brave_new_world.txt")));
        assertDoesNotThrow(() -> assertEquals(460, fIndex.getNumOfUniqueWordsInFile("die_verwandlung1.txt")));
        assertDoesNotThrow(() -> assertEquals(457, fIndex.getNumOfUniqueWordsInFile("die_verwandlung2.txt")));
        assertDoesNotThrow(() -> assertEquals(457, fIndex.getNumOfUniqueWordsInFile("die_verwandlung3.txt")));
        assertDoesNotThrow(() -> assertEquals(1, fIndex.getNumOfUniqueWordsInFile("just_a.txt")));

        assertThrows(
                FileIndexException.class,
                () -> fIndex.getNumOfUniqueWordsInFile("2022b-final-test-answers.txt")
        );
    }

    @Test
    public void testGetNumOfFiles() {
        this.index();
        assertEquals(5, fIndex.getNumOfFilesInIndex());
    }

    @Test
    public void testGetTfIdf() {
        this.index();
        assertDoesNotThrow(() -> assertEquals(0.013, fIndex.getTFIDF("und", "die_verwandlung1.txt"), 0.001));
        assertDoesNotThrow(() -> assertEquals(0.017, fIndex.getTFIDF("und", "die_verwandlung2.txt"), 0.001));
        assertDoesNotThrow(() -> assertEquals(
                fIndex.getTFIDF("und", "die_verwandlung2.txt"),
                fIndex.getTFIDF("und", "die_verwandlung3.txt")
        ));
        assertDoesNotThrow(() -> assertEquals(0.91, fIndex.getTFIDF("a", "just_a.txt"), 0.01));
        assertDoesNotThrow(() -> assertEquals(0, fIndex.getTFIDF("als", "just_a.txt")));
        assertDoesNotThrow(() -> assertEquals(0, fIndex.getTFIDF("gregor", "brave_new_world.txt")));
        assertThrows(
                FileIndexException.class,
                () -> fIndex.getTFIDF("יב. קל לראות","2022b-final-test-answers.txt")
        );
    }

    @Test
    public void testSignificantWords() {
        this.index();

        assertDoesNotThrow(() -> assertEquals(
                fIndex.getTopKMostSignificantWords("die_verwandlung2.txt", 100),
                fIndex.getTopKMostSignificantWords("die_verwandlung3.txt", 100)));

        assertDoesNotThrow(() -> assertEquals(
                "a",
                fIndex.getTopKMostSignificantWords("just_a.txt", 1).get(0).getKey()
        ));

        assertDoesNotThrow(() -> assertEquals(
                "the",
                fIndex.getTopKMostSignificantWords("brave_new_world.txt", 1).get(0).getKey()
        ));

        assertDoesNotThrow(() -> assertEquals(
                "of",
                fIndex.getTopKMostSignificantWords("brave_new_world.txt", 2).get(1).getKey()
        ));

        assertDoesNotThrow(() -> assertEquals(
                "abnormalities",
                fIndex.getTopKMostSignificantWords("brave_new_world.txt", 1000).get(532).getKey()
        ));

        assertThrows(
                FileIndexException.class,
                () -> fIndex.getTopKMostSignificantWords("2022b-final-test-answers.txt", 1)
        );
    }

    // Note that this also tests cosineSimilarity
    @Test
    public void testClosestDocs() {
        this.index();

        assertDoesNotThrow(() -> {
            List<Map.Entry<String, Double>> list = fIndex.getTopKClosestDocuments("brave_new_world.txt", 4);
            assertEquals("die_verwandlung1.txt", list.get(3).getKey());
            assertTrue(list.get(1).getKey().equals("die_verwandlung2.txt") || list.get(1).getKey().equals("die_verwandlung3.txt"));
            assertEquals(list.get(1).getValue(), list.get(2).getValue());
            assertTrue(list.get(3).getValue() < 0.001);
        });

        assertDoesNotThrow(() -> {
            List<Map.Entry<String, Double>> list = fIndex.getTopKClosestDocuments("die_verwandlung2.txt", 4);
            assertEquals("die_verwandlung3.txt", list.get(0).getKey());
            assertEquals(1, list.get(0).getValue());
            assertEquals("just_a.txt", list.get(3).getKey());
            assertEquals(0, list.get(3).getValue());
            assertEquals("die_verwandlung1.txt", list.get(1).getKey());
            assertEquals(0.54, list.get(1).getValue(), 0.01);
        });

        assertThrows(
                FileIndexException.class,
                () -> fIndex.getTopKClosestDocuments("2022b-final-test-answers.txt", 2)
        );
    }

}