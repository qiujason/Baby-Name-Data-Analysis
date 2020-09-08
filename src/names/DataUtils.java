package names;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DataUtils {
    private static final int NAMEINDEX = 0;
    private static final int GENDERINDEX = 1;

    public static void fillCountsAndNamesOfFirstLetters(List<String[]> data, Integer[] charCounts,
                                                  Map<Character, SortedSet<String>> listNamesOfChar, String gender) {
        data = data.stream()
                .filter(nameArray -> nameArray[GENDERINDEX].equals(gender))
                .collect(Collectors.toList());
        String[] topNameArray = data.get(0);
        char letter = topNameArray[NAMEINDEX].charAt(0);
        charCounts[letter]++;
        data.forEach(nameArray -> {
            char firstLetter = nameArray[NAMEINDEX].charAt(0);
            listNamesOfChar.putIfAbsent(firstLetter, new TreeSet<>());
            listNamesOfChar.get(firstLetter).add(nameArray[NAMEINDEX]);
        });
    }

    public static List<Character> getMostFrequentNamesWithFirstLetter(Integer[] charCounts) {
        List<Integer> charCountsList = Arrays.asList(charCounts);
        int maxFrequency = Collections.max(charCountsList);
        List<Character> mostPopularLetters = new ArrayList<>();
        for (int character = 0; character < charCounts.length; character++) {
            if (charCounts[character] == maxFrequency) {
                mostPopularLetters.add((char)character);
            }
        }
        return mostPopularLetters;
    }

    public static File[] getListOfFiles(String directory) {
        File dir = new File(directory);
        if (dir == null) {
            return new File[0];
        }
        return dir.listFiles();
    }

    public static int getYearFromFileName(File file) {
        String yearString = file.getName().replaceAll("[^0-9]","");
        return Integer.parseInt(yearString);
    }

    public static int getRecentYear(String directory) {
        File[] directoryListing = getListOfFiles(directory);
        int recentYear = -1;
        for (File file : directoryListing) {
            recentYear = Math.max(recentYear, getYearFromFileName(file));
        }
        return recentYear;
    }

    public static int getOldestYear(String directory) {
        File[] directoryListing = getListOfFiles(directory);
        int oldestYear = 10000; // large number impossible to be a year
        for (File file : directoryListing) {
            oldestYear = Math.min(oldestYear, getYearFromFileName(file));
        }
        return oldestYear;
    }
    /*
    ranges of years that are empty, do not fit completely within the years in the given source of data, or are otherwise nonsensical
names that do not match the exact case of those in the various data files
genders that are not either M or F (the only ones given in the data files)
     */

    public static void handleYearErrors(ParseData data, int year) {
        handleYearErrors(data, year, year);
    }

    public static void handleYearErrors(ParseData data, int startYear, int finalYear) {
        if (startYear > finalYear) {
            throw new IllegalArgumentException("First year in range is after last year in range");
        }
        for (int year = startYear; year <= finalYear; year++) {
            if (data.getDataFromYear(year) == null) {
                throw new IllegalArgumentException(year + " is not a valid year or is not in the dataset");
            }
        }
    }
}
