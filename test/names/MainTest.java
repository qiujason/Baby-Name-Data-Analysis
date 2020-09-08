package names;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    /*
    CONFIGURATION
     */
    private static final String DIRECTORY = "data/ssa_test/";

    private DataAnalysis testInstance;

    @BeforeEach
    void instantiate() {
        testInstance = new DataAnalysis(DIRECTORY);
    }

    @Test
    void testFindTopRankedYearNormal() {
        assertArrayEquals(new String[]{"Liam", "Emma"}, testInstance.findTopRankedInYear(2020));
    }
    @Test
    void testFindTopRankedYearAllSame() {
        assertArrayEquals(new String[]{"John", "Emma"}, testInstance.findTopRankedInYear(2023));
    }
    @Test
    void testFindTopRankedYearTied() {
        assertArrayEquals(new String[]{"John", "Emma"}, testInstance.findTopRankedInYear(2022));
    }

    @Test
    void testFindCountByGenderLetterYearFemale() {
        assertArrayEquals(new int[]{4, 240000}, testInstance.findCountByGenderLetterYear(2024, "F", "O"));
    }
    @Test
    void testFindCountByGenderLetterYearNone() {
        assertArrayEquals(new int[]{0, 0}, testInstance.findCountByGenderLetterYear(2024, "M", "Z"));
    }
    @Test
    void testFindCountByGenderLetterYearMale() {
        assertArrayEquals(new int[]{4, 320000}, testInstance.findCountByGenderLetterYear(2024, "M", "W"));
    }

    @Test
    void testFindRankingsByNameGenderMale() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 3);
        expected.put(2021, 0);
        expected.put(2022, 2);
        expected.put(2023, 3);
        expected.put(2024, 4);
        assertEquals(expected, testInstance.findRankingsByNameGender("William", "M"));
    }
    @Test
    void testFindRankingsByNameGenderFemale() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 5);
        expected.put(2021, 0);
        expected.put(2022, 2);
        expected.put(2023, 2);
        expected.put(2024, 5);
        assertEquals(expected, testInstance.findRankingsByNameGender("Olivia", "F"));
    }
    @Test
    void testFindRankingsByNameGenderNone() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 0);
        expected.put(2021, 0);
        expected.put(2022, 0);
        expected.put(2023, 0);
        expected.put(2024, 0);
        assertEquals(expected, testInstance.findRankingsByNameGender("Jason", "M"));
    }

    @Test
    void testFindSameRankInRecentYearFemale() {
        assertEquals("Jennifer", testInstance.findSameRankInRecentYear("Sophia", "F", 2020));
    }
    @Test
    void testFindSameRankInRecentYearNameNotFound() {
        assertEquals("Name not found", testInstance.findSameRankInRecentYear("Gerry", "M", 2020));
    }
    @Test
    void testFindSameRankInRecentYearMale() {
        assertEquals("William", testInstance.findSameRankInRecentYear("James", "M", 2023));
    }

    @Test
    void testFindMostPopularInRangeAllYears() {
        assertArrayEquals(new String[]{"Emma for 4 years"},
                testInstance.findMostPopularInRange("F", 2020, 2024).toArray());
    }
    @Test
    void testFindMostPopularInRangeTie() {
        assertArrayEquals(new String[]{"Clara for 1 years", "Emma for 1 years"},
                testInstance.findMostPopularInRange("F", 2020, 2021).toArray());
    }
    @Test
    void testFindMostPopularInRangeSameYear() {
        assertArrayEquals(new String[]{"John for 1 years"},
                testInstance.findMostPopularInRange("M", 2022, 2022).toArray());
    }

    @Test
    void testMostPopularLetterGirlsSameYear() {
        assertArrayEquals(new String[]{"E", "Emma"},
                testInstance.findMostPopularLetterGirls(2022, 2022));
    }
    @Test
    void testMostPopularLetterGirlsRange() {
        assertArrayEquals(new String[]{"E", "Elizabeth", "Emma"},
                testInstance.findMostPopularLetterGirls(2020, 2023));
    }
    @Test
    void testMostPopularLetterGirlsTie() {
        assertArrayEquals(new String[]{"C", "E"},
                testInstance.findMostPopularLetterGirls(2021, 2022));
    }

    @Test
    void testFindRankingsByNameGenderInRangeSameYear() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 3);
        assertEquals(expected,
                testInstance.findRankingsByNameGenderInRange(2020, 2020, "William", "M"));
    }
    @Test
    void testFindRankingsByNameGenderInRange() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 3);
        expected.put(2021, 0);
        expected.put(2022, 3);
        expected.put(2023, 4);
        assertEquals(expected,
                testInstance.findRankingsByNameGenderInRange(2020, 2023, "Ava", "F"));
    }
    @Test
    void testFindRankingsByNameGenderInRangeNameNotFound() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2020, 0);
        expected.put(2021, 0);
        expected.put(2022, 0);
        assertEquals(expected,
                testInstance.findRankingsByNameGenderInRange(2020, 2022, "Jason", "M"));
    }

    @Test
    void testFindRankingDifferenceFirstLastYearsSameYear() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("2021", 1);
        expected.put("Difference", 0);
        assertEquals(expected,
                testInstance.findRankingDifferenceFirstLastYears(2021, 2021, "Clara", "F"));
    }
    @Test
    void testFindRankingDifferenceFirstLastYearsRange() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("2020", 2);
        expected.put("2022", 4);
        expected.put("Difference", -2);
        assertEquals(expected,
                testInstance.findRankingDifferenceFirstLastYears(2020, 2022, "Noah", "M"));
    }
    @Test
    void testFindRankingDifferenceFirstLastYearsNameNotFound() {
        assertEquals(null,
                testInstance.findRankingDifferenceFirstLastYears(2020, 2024, "Oliver", "M"));
    }

    @Test
    void testFindNameLargestDifferenceFirstLastYearsFemale() {
        assertEquals("Isabella",
                testInstance.findNameLargestDifferenceFirstLastYears(2020, 2021, "F"));
    }
    @Test
    void testFindNameLargestDifferenceFirstLastYearsMale() {
        assertEquals("James",
                testInstance.findNameLargestDifferenceFirstLastYears(2020, 2024, "M"));
    }
    @Test
    void testFindNameLargestDifferenceFirstLastWithMissingNames() {
        assertEquals("Olivia",
                testInstance.findNameLargestDifferenceFirstLastYears(2023, 2024, "F"));
    }

    @Test
    void testFindAverageRankOverRangeSameYear() {
        assertEquals(3,
                testInstance.findAverageRankOverRange(2020, 2020, "William", "M"));
    }
    @Test
    void testFindAverageRankOverRange() {
        assertEquals(2,
                testInstance.findAverageRankOverRange(2020, 2023, "Ava", "F"));
    }
    @Test
    void testFindAverageRankOverRangeNameNotFound() {
        assertEquals(0,
                testInstance.findAverageRankOverRange(2020, 2022, "Jason", "M"));
    }

    @Test
    void testFindNameHighestAverageRankFemale() {
        assertEquals("Emma",
                testInstance.findNameHighestAverageRank(2020, 2021, "F"));
    }
    @Test
    void testFindNameHighestAverageRankMale() {
        assertEquals("Liam",
                testInstance.findNameHighestAverageRank(2020, 2024, "M"));
    }
    @Test
    void testFindNameHighestAverageRankMissingNames() {
        assertEquals("Emma",
                testInstance.findNameHighestAverageRank(2023, 2024, "F"));
    }

    @Test
    void testFindAverageRankRecentYearsFemale() {
        assertEquals(3,
                testInstance.findAverageRankRecentYears("Isabella", "F", 3));
    }
    @Test
    void testFindAverageRankRecentYearsMale() {
        assertEquals(3,
                testInstance.findAverageRankRecentYears("William", "M", 2));
    }
    @Test
    void testFindAverageRankRecentYearsOneYear() {
        assertEquals(3,
                testInstance.findAverageRankRecentYears("Ava", "F", 1));
    }

    @Test
    void testFindNamesAtRankSameYear() {
        Map<Integer, String> expected = new HashMap<>();
        expected.put(2020, "Sophia");
        assertEquals(expected,
                testInstance.findNamesAtRank(2020, 2020, "F", 2));
    }
    @Test
    void testFindNamesAtRankRange() {
        Map<Integer, String> expected = new HashMap<>();
        expected.put(2020, "James");
        expected.put(2021, "Invalid rank");
        expected.put(2022, "Noah");
        expected.put(2023, "James");
        assertEquals(expected,
                testInstance.findNamesAtRank(2020, 2023, "M", 4));
    }
    @Test
    void testFindNamesAtRankNonExistent() {
        Map<Integer, String> expected = new HashMap<>();
        expected.put(2020, "Invalid rank");
        expected.put(2021, "Invalid rank");
        assertEquals(expected,
                testInstance.findNamesAtRank(2020, 2021, "F", 10));
    }

    @Test
    void testFindNamesAtRankMostOftenSameYear() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("Sophia", 1);
        assertEquals(expected,
                testInstance.findNamesAtRankMostOften(2020, 2020, "F", 2));
    }
    @Test
    void testFindNamesAtRankMostOftenRange() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("James", 2);
        assertEquals(expected,
                testInstance.findNamesAtRankMostOften(2020, 2023, "M", 4));
    }
    @Test
    void testFindNamesAtRankMostOftenNonExistent() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("Invalid rank", 2);
        assertEquals(expected,
                testInstance.findNamesAtRankMostOften(2020, 2021, "F", 10));
    }

    @Test
    void testEmptyYear() {
        Exception exception = assertThrows(Exception.class, () -> testInstance.findTopRankedInYear(2025));
        assertEquals("2025 is not a valid year or is not in the dataset", exception.getMessage());
    }

    @Test
    void testNonexistentYear() {
        Exception exception = assertThrows(Exception.class, () -> testInstance.findTopRankedInYear(20300));
        assertEquals("20300 is not a valid year or is not in the dataset", exception.getMessage());
    }

    @Test
    void testInvalidRangeOfYears() {
        Exception exception = assertThrows(Exception.class, () -> testInstance.findMostPopularLetterGirls(2040, 2030));
        assertEquals("First year in range is after last year in range", exception.getMessage());
    }

    @Test
    void testRangeOfYearsOutsideDataset() {
        Exception exception = assertThrows(Exception.class, () -> testInstance.findMostPopularLetterGirls(2019, 2021));
        assertEquals("2019 is not a valid year or is not in the dataset", exception.getMessage());
    }

    @Test
    void testInvalidRangeOfYearsEmptyYear() {
        Exception exception = assertThrows(Exception.class, () -> testInstance.findMostPopularLetterGirls(2023, 2025));
        assertEquals("2025 is not a valid year or is not in the dataset", exception.getMessage());
    }

    @Test
    void testBadGenderInput() {
        Exception exception = assertThrows(Exception.class,
                () -> testInstance.findSameRankInRecentYear("Sophia", "G", 2020));
        assertEquals("Invalid Gender", exception.getMessage());
    }
}