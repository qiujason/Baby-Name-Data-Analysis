package names;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    Main main = new Main();

    @Test
    void testGetTopRankedYear() throws FileNotFoundException {
        assertArrayEquals(new String[]{"Mary", "John"}, main.getTopRankedYear(1900));
    }

    @org.junit.jupiter.api.Test
    void getCountByGenderLetterYear() {
    }

    @org.junit.jupiter.api.Test
    void getRankingsNameGender() {
    }

    @org.junit.jupiter.api.Test
    void getSameRank() {
    }

    @org.junit.jupiter.api.Test
    void getMostPopularInRange() {
    }

    @org.junit.jupiter.api.Test
    void mostPopularLetterGirls() {
    }
}