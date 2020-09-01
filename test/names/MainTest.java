package names;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private Main testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new Main();
    }

    @Test
    void testGetTopRankedYear2020() throws FileNotFoundException {
        assertArrayEquals(new String[]{"Emma", "Liam"}, testInstance.getTopRankedYear(2020));
    }
    @Test
    void testGetTopRankedYear2021() throws FileNotFoundException {
        assertArrayEquals(new String[]{"Clara", "John"}, testInstance.getTopRankedYear(2021));
    }
    @Test
    void testGetTopRankedYear2022() throws FileNotFoundException {
        assertArrayEquals(new String[]{"Emma", "John"}, testInstance.getTopRankedYear(2022));
    }

    @Test
    void testGetCountByGenderLetterYearFemale() throws FileNotFoundException {
        assertArrayEquals(new int[]{2, 110000}, testInstance.getCountByGenderLetterYear(2020, "F", "A"));
    }

    @Test
    void testGetCountByGenderLetterYearNone() throws FileNotFoundException {
        assertArrayEquals(new int[]{0, 0}, testInstance.getCountByGenderLetterYear(2021, "M", "Z"));
    }

    @Test
    void testGetCountByGenderLetterYearMale() throws FileNotFoundException {
        assertArrayEquals(new int[]{1, 20000}, testInstance.getCountByGenderLetterYear(2022, "M", "M"));
    }

    @Test
    void testGetRankingsNameGenderMale() throws FileNotFoundException {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 3);
        expected.put(2021, 10);
        expected.put(2022, 3);
        assertEquals(expected, testInstance.getRankingsNameGender("William", "M"));
    }

    @Test
    void testGetRankingsNameGenderFemale() throws FileNotFoundException {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 5);
        expected.put(2021, 4);
        expected.put(2022, 2);
        assertEquals(expected, testInstance.getRankingsNameGender("Olivia", "F"));
    }

    @Test
    void testGetRankingsNameGenderNone() throws FileNotFoundException {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, -1);
        expected.put(2021, -1);
        expected.put(2022, -1);
        assertEquals(expected, testInstance.getRankingsNameGender("Jason", "M"));
    }

    @Test
    void testGetSameRank2020() throws FileNotFoundException {
        assertEquals("Olivia", testInstance.getSameRank("Sophia", "F", 2020));
    }

    @Test
    void testGetSameRankInvalidName() throws FileNotFoundException {
        assertEquals("Name not found", testInstance.getSameRank("Gerry", "M", 2020));
    }

    @Test
    void testGetSameRank2021() throws FileNotFoundException {
        assertEquals("Olivia", testInstance.getSameRank("Isabella", "F", 2021));
    }

    @Test
    void testGetMostPopularInRange20_22() throws FileNotFoundException {
        assertArrayEquals(new String[]{"Emma for 2 years"},
                testInstance.getMostPopularInRange("F", 2020, 2022).toArray());
    }

    @Test
    void testGetMostPopularInRange20_21() throws FileNotFoundException {
        assertArrayEquals(new String[]{"Clara for 1 years", "Emma for 1 years"},
                testInstance.getMostPopularInRange("F", 2020, 2021).toArray());
    }

    @Test
    void testGetMostPopularInRange22_22() throws FileNotFoundException {
        assertArrayEquals(new String[]{"John for 1 years"},
                testInstance.getMostPopularInRange("M", 2022, 2022).toArray());
    }

    @Test
    void testMostPopularLetterGirls22() throws FileNotFoundException {
        assertArrayEquals(new String[]{"A", "Amelia", "Ava"}, testInstance.mostPopularLetterGirls(2022, 2022));
    }

    @Test
    void testMostPopularLetterGirls20_22() throws FileNotFoundException {
        assertArrayEquals(new String[]{"E", "Elizabeth", "Emma", "Evelyn"},
                testInstance.mostPopularLetterGirls(2020, 2022));
    }

    @Test
    void testMostPopularLetterGirls21_22() throws FileNotFoundException {
        assertArrayEquals(new String[]{"C", "Charlotte", "Clara"},
                testInstance.mostPopularLetterGirls(2021, 2022));
    }
}