package names;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

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
        assertArrayEquals(new String[]{"Emma", "Liam"}, testInstance.getTopRankedYear(2021));
    }
    @Test
    void testGetTopRankedYear2022() throws FileNotFoundException {
        assertArrayEquals(new String[]{"Emma", "Liam"}, testInstance.getTopRankedYear(2022));
    }

    @Test
    void testGetCountByGenderLetterYear2020() throws FileNotFoundException {
        assertArrayEquals(new int[]{2, 110000}, testInstance.getCountByGenderLetterYear(2020, "F", "A"));
    }

    @Test
    void testGetRankingsNameGender() {
    }

    @Test
    void testGetSameRank2020() throws FileNotFoundException {
        assertEquals("Sara", testInstance.getSameRank("Sophia", "F", 2020));
    }

    @Test
    void testGetSameRankInvalidName() throws FileNotFoundException {
        assertEquals("Name not found", testInstance.getSameRank("Gerry", "M", 2020));
    }

    @Test
    void testGetSameRank2021() throws FileNotFoundException {
        assertEquals("Isabella", testInstance.getSameRank("Isabella", "F", 2021));
    }

    @Test
    void getMostPopularInRange() throws FileNotFoundException {
    }

    @org.junit.jupiter.api.Test
    void mostPopularLetterGirls() {
    }
}