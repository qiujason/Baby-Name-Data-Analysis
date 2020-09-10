package names;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DataUtils {
    private static final int NAMEINDEX = 0;
    private static final int GENDERINDEX = 1;

    /**
     * Fills charCounts with number of names of given gender starting with each letter and listNamesOfChar with
     * each name of given gender that start with each corresponding letter
     * @param data              ParseData object that holds all the name data
     * @param charCounts        an Integer array that contains all the counts of each character which is represented by an index
     *                          denoted by the character's corresponding ASCII value (ex. 'A' = 65; 'Z' = 90)
     * @param listNamesOfChar   a HashMap containing a key Character that represents the first letter and value SortedSet of
     *                          strings that will hold all names that start with the first letter
     * @param gender            a String containing either "M" or "F"
     */
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

    /**
     * Returns the letters that are the most popular for names to begin with
     * @param charCounts    an Integer array that contains all the counts of each character which is represented by an index
     *                      denoted by the character's corresponding ASCII value (ex. 'A' = 65; 'Z' = 90)
     * @return              a List of Characters that are the most popular letters
     */
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

    /**
     * Returns all the files of a given directory
     * @param directory contains the directory path of all text files within a dataset
     * @return          returns a File array containing all the Files within the given directory
     */
    public static File[] getListOfFiles(String directory) {
        File dir = new File(directory);
        if (dir == null) {
            return new File[0];
        }
        return dir.listFiles();
    }

    /**
     * Reads the year from the name of the file object
     * @param file  file object
     * @return      an int that describes the year of the file's data
     */
    public static int getYearFromFileName(File file) {
        String yearString = file.getName().replaceAll("[^0-9]","");
        return Integer.parseInt(yearString);
    }

    /**
     * Returns the most recent year in the dataset
     * @param directory contains the directory path of all text files within a dataset
     * @return          an int describing the most recent year in the dataset
     */
    public static int getRecentYear(String directory) {
        File[] directoryListing = getListOfFiles(directory);
        int recentYear = -1;
        for (File file : directoryListing) {
            recentYear = Math.max(recentYear, getYearFromFileName(file));
        }
        return recentYear;
    }

    /**
     * Returns the oldest year in the dataset
     * @param directory contains the directory path of all text files within a dataset
     * @return          an int describing the oldest year in the dataset
     */
    public static int getOldestYear(String directory) {
        File[] directoryListing = getListOfFiles(directory);
        int oldestYear = 10000; // large number impossible to be a year
        for (File file : directoryListing) {
            oldestYear = Math.min(oldestYear, getYearFromFileName(file));
        }
        return oldestYear;
    }

    /**
     * Throws exceptions if year is empty, does not fit completely within the years in the given source of data,
     * or is otherwise nonsensical
     * @param data  ParseData object that holds all the name data
     * @param year  an int describing a year with a corresponding text file in the dataset
     */
    public static void handleYearErrors(ParseData data, int year) {
        handleYearErrors(data, year, year);
    }

    /**
     * Throws exceptions if ranges of years are empty, do not fit completely within the years in the given source of data,
     * or are otherwise nonsensical
     * @param data      ParseData object that holds all the name data
     * @param startYear an int describing a year indicating the first year in the range
     * @param finalYear an int describing a year indicating the last year in the range
     */
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

    /**
     * Throws exception if name is empty and otherwise corrects String to follow proper case conventions
     * (first letter is capitalized and all other letters are lowercase)
     * @param name  a String containing a name
     * @return      a corrected String with correct case conventions
     */
    public static String handleNameConvention(String name) {
        if (name.equals("")) {
            throw new IllegalArgumentException("Invalid Name");
        }
        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }

    /**
     * Throws exception if gender is not "M" or "F"
     * @param gender a String
     */
    public static void handleGenderErrors(String gender) {
        if (!gender.equals("M") && !gender.equals("F")) {
            throw new IllegalArgumentException("Invalid Gender");
        }
    }
}
