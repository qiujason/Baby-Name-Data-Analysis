package names;

import java.util.*;
import java.util.stream.Collectors;

public class DataAnalysis {
    private static final String MALE = "M";
    private static final String FEMALE = "F";
    private static final int NAME_INDEX = 0;
    private static final int GENDER_INDEX = 1;
    private static final int FREQ_INDEX = 2;
    private static final int TOP_RANK = 1;

    private final ParseData data;

    public DataAnalysis(String directory) {
        data = new ParseData(directory);
    }

    /**
     * Returns the top ranked male and female names of a specified year
     * @param year  an int describing a valid year with a corresponding text file in the dataset
     * @return      a string array of length 2; index 0 has the top ranked male name; index 1 has the top ranked female name
     */
    public String[] findTopRankedInYear(int year) {
        DataUtils.handleYearErrors(data, year);
        String[] topRanked = new String[2];
        Map<Integer, String> maleRankTable = data.getRankTable(year, MALE);
        Map<Integer, String> femaleRankTable = data.getRankTable(year, FEMALE);
        topRanked[0] = maleRankTable.get(TOP_RANK);
        topRanked[1] = femaleRankTable.get(TOP_RANK);
        return topRanked;
    }

    /**
     * Returns number of names starting with a specified letter and the number
     * of total babies with names starting with the specified letter within a specified year
     * @param year      an int describing a valid year with a corresponding text file in the dataset
     * @param gender    a String containing either "M" or "F"
     * @param letter    a String containing one letter
     * @return          an int array of length 2; index 0 has the number of names starting with the specified letter;
     *                  index 1 has the total number of babies with a name starting with the specified letter
     */
    public int[] findCountByGenderLetterYear(int year, String gender, String letter) {
        DataUtils.handleYearErrors(data, year);
        DataUtils.handleGenderErrors(gender);
        int[] counts = new int[2];
        data.getDataFromYear(year).stream()
                .filter(nameArray -> nameArray[NAME_INDEX].startsWith(letter) && nameArray[GENDER_INDEX].equals(gender))
                .forEach(nameArray -> {
                    counts[0]++;
                    counts[1] += Integer.parseInt(nameArray[FREQ_INDEX]);
                });
        return counts;
    }

    /**
     * Returns years and the corresponding rank for a given name and gender
     * @param name      a String containing a name
     * @param gender    a String containing either "M" or "F"
     * @return          a HashMap with each year as the keys and the ranking for that year as the value
     */
    public Map<Integer,Integer> findRankingsByNameGender(String name, String gender) {
        DataUtils.handleGenderErrors(gender);
        name = DataUtils.handleNameConvention(name);
        return findRankingsByNameGenderInRange(data.getStartYear(), data.getFinalYear(), name, gender);
    }

    /**
     * Returns name in the most recent year that is the same rank as the name of the given year and gender
     * @param name      a String containing a name
     * @param gender    a String containing either "M" or "F"
     * @param year      an int describing a valid year with a corresponding text file in the dataset
     * @return          a String with a name in the recent year that matches the same rank
     *                  Name not found if name is not found in recent year
     */
    public String findSameRankInRecentYear(String name, String gender, int year) {
        DataUtils.handleYearErrors(data, year);
        DataUtils.handleGenderErrors(gender);
        name = DataUtils.handleNameConvention(name);
        Map<String, Integer> rankTableInverse = data.getRankTableInverse(year, gender);
        Integer nameRank = rankTableInverse.get(name);
        if (nameRank == null) {
            return "Name not found";
        }
        int recentYear = data.getFinalYear();
        if (recentYear != -1) {
            Map<Integer, String> recentYearRankTable = data.getRankTable(recentYear, gender);
            return recentYearRankTable.get(nameRank);
        } else {
            return "No files found";
        }
    }

    /**
     * Returns names of a given gender that are the most frequent within the range of years
     * @param gender    a String containing either "M" or "F"
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @return          an ArrayList of strings containing names that are the most frequent
     */
    public List<String> findMostPopularInRange(String gender, int startYear, int finalYear) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        DataUtils.handleGenderErrors(gender);
        Map<String, Integer> topRankPerYear = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<Integer, String> rankTable = data.getRankTable(year, gender);
            String topRankedName = rankTable.get(TOP_RANK);
            topRankPerYear.putIfAbsent(topRankedName, 0);
            topRankPerYear.put(topRankedName, topRankPerYear.get(topRankedName) + 1);
        }
        int mostFrequent = Collections.max(topRankPerYear.values());
        // filter through to get only entries that are most frequent and then add it to a new arraylist
        return topRankPerYear.entrySet().stream()
                .filter(entry -> entry.getValue() == mostFrequent)
                .map(entry -> entry.getKey() + " for " + entry.getValue() + " years")
                .collect(Collectors.toList());
    }

    /**
     * Returns the most popular letters of girl names in a range and an array of the names if there is no tie
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @return          an array of Strings of two formats:
     *                  1. if one letter is the most popular, it returns an array with index 0 containing
     *                  the most popular letter and all the names starting with that letter in the following
     *                  indices
     *                  2. if multiple letters are tied for being the most popular, it returns an array of those letters
     */
    public String[] findMostPopularLetterGirls(int startYear, int finalYear) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        Integer[] charCounts = new Integer[100];
        Arrays.fill(charCounts, 0);
        Map<Character, SortedSet<String>> listNamesOfChar = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            DataUtils.fillCountsAndNamesOfFirstLetters(data.getDataFromYear(year), charCounts, listNamesOfChar, FEMALE);
        }
        List<Character> mostPopularLetters = DataUtils.getMostFrequentNamesWithFirstLetter(charCounts);
        if (mostPopularLetters.size() <= 1) { // if only one name is most frequent
            char mostPopularLetter = mostPopularLetters.get(0);
            SortedSet<String> listOfNames = listNamesOfChar.get(mostPopularLetter);
            String[] results = new String[listOfNames.size() + 1];
            results[0] = Character.toString(mostPopularLetter);
            int i = 1;
            for (String name : listOfNames) {
                results[i] = name;
                i++;
            }
            return results;
        }
        String[] results = new String[mostPopularLetters.size()]; // if tied
        for (int i = 0; i < mostPopularLetters.size(); i++) {
            results[i] = Character.toString(mostPopularLetters.get(i));
        }
        return results;
    }

    /**
     * Returns years in a specified range and the corresponding rank for a given name and gender
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @param name      a String containing a name
     * @param gender    a String containing either "M" or "F"
     * @return          a HashMap with each year in the range as the keys and the ranking for that year as the value
     */
    public Map<Integer, Integer> findRankingsByNameGenderInRange(int startYear, int finalYear, String name, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        DataUtils.handleGenderErrors(gender);
        name = DataUtils.handleNameConvention(name);
        Map<Integer, Integer> rankings = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<String, Integer> rankTableInverse = data.getRankTableInverse(year, gender);
            Integer ranking = rankTableInverse.get(name);
            if (ranking != null) {
                rankings.put(year, rankTableInverse.get(name));
            } else {
                rankings.put(year, 0);
            }
        }
        return rankings;
    }

    /**
     * Returns rankings of the name in the first year of the range, last year
     * of the range, and the difference in rankings between the years
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @param name      a String containing a name
     * @param gender    a String containing either "M" or "F"
     * @return          a HashMap with 3 key-value pairs:
     *                  1. Key: Start year as a string; Value: Ranking in the start year
     *                  2. Key: Final year as a string; Value: Ranking in the final year
     *                  3. Key: "Difference"; Value: Ranking difference
     */
    public Map<String, Integer> findRankingDifferenceFirstLastYears(int startYear, int finalYear, String name, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        DataUtils.handleGenderErrors(gender);
        name = DataUtils.handleNameConvention(name);
        Map<Integer, Integer> rankingStartYear = findRankingsByNameGenderInRange(startYear, startYear, name, gender);
        Map<Integer, Integer> rankingFinalYear = findRankingsByNameGenderInRange(finalYear, finalYear, name, gender);
        Map<String, Integer> results = new HashMap<>();
        int firstRanking = rankingStartYear.get(startYear);
        int lastRanking = rankingFinalYear.get(finalYear);
        if (firstRanking == 0 || lastRanking == 0) return null; // name not found
        results.put(Integer.toString(startYear), firstRanking);
        results.put(Integer.toString(finalYear), lastRanking);
        results.put("Difference", firstRanking - lastRanking);
        return results;
    }

    /**
     * Returns a name of a given gender with the largest difference in ranks between a given first and last years of a range
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @param gender    a String containing either "M" or "F"
     * @return          a String containing the name with the largest difference in rank between the first and last
     *                  years of the range
     */
    public String findNameLargestDifferenceFirstLastYears(int startYear, int finalYear, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        DataUtils.handleGenderErrors(gender);
        Map<Integer, String> rankTable = data.getRankTable(startYear, gender);
        Map<String, Integer> differences = new HashMap<>();
        rankTable.values().forEach(name -> {
            try {
                Map<String, Integer> rankingDifferencesForName = findRankingDifferenceFirstLastYears(startYear, finalYear, name, gender);
                differences.put(name, Math.abs(rankingDifferencesForName.get("Difference")));
            } catch (NullPointerException ignored) {
                // ignore; skip names that are not found in the dataset
            }
        });
        // find the max entry by comparing differences (value of the entry) and then get the key (name) of the max entry
        return differences.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    /**
     * Returns the average rank of a name of a given gender over a given range of years
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @param name      a String containing a name
     * @param gender    a String containing either "M" or "F"
     * @return          an int describing the average rank of a name over a range of years specified by startYear and finalYear
     */
    public int findAverageRankOverRange(int startYear, int finalYear, String name, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        DataUtils.handleGenderErrors(gender);
        name = DataUtils.handleNameConvention(name);
        Map<Integer, Integer> rankings = findRankingsByNameGenderInRange(startYear, finalYear, name, gender);
        // get sum by looping through ranks and adding the next value to sum (if not null);
        int sum = rankings.values().stream().reduce(0, (total, rank) -> {
            if (rank == null) {
                return total;
            } else {
                return total + rank;
            }
        });
        return sum/rankings.values().size();
    }

    /**
     * Returns the name of a given gender with the highest average rank over a given range of years specified by startYear and finalYear
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @param gender    a String containing either "M" or "F"
     * @return          a String containing the name of a gender with the highest average rank over a range of years
     */
    public String findNameHighestAverageRank(int startYear, int finalYear, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        DataUtils.handleGenderErrors(gender);
        Map<Integer, String> rankTable = data.getRankTable(startYear, gender);
        Map<String, Integer> averageRanks = new HashMap<>();
        rankTable.values().forEach(name -> averageRanks.put(name, findAverageRankOverRange(startYear, finalYear, name, gender)));
        // find the min (highest rank) entry by comparing differences (value of the entry) and then get the key (name) of the min entry
        return averageRanks.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    /**
     * Returns the average rank of a name of a given gender in the most recent specified number of years
     * @param name      a String containing a name
     * @param gender    a String containing either "M" or "F"
     * @param numYears  an int describing the number of recent years
     * @return          an int describing the average rank of a name of a gender in numYears recent years
     */
    public int findAverageRankRecentYears(String name, String gender, int numYears) {
        DataUtils.handleGenderErrors(gender);
        name = DataUtils.handleNameConvention(name);
        int recentYear = data.getFinalYear();
        return findAverageRankOverRange(recentYear - numYears + 1, recentYear, name, gender);
    }

    /**
     * Returns all names of a given gender at a given rank from a given range specified by startYear and finalYear
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @param gender    a String containing either "M" or "F"
     * @param rank      an int describing a rank
     * @return          a HashMap with year as the key and name at the rank as the value
     */
    public Map<Integer, String> findNamesAtRank(int startYear, int finalYear, String gender, int rank) {
        DataUtils.handleGenderErrors(gender);
        Map<Integer, String> namesAtRank = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<Integer, String> rankTable = data.getRankTable(year, gender);
            String name = rankTable.get(rank);
            if (name == null) {
                name = "Invalid rank";
            }
            namesAtRank.put(year, name);
        }
        return namesAtRank;
    }

    /**
     * Returns the names of a given gender and frequencies of each name at a given rank over a given range specified
     * by startYear and finalYear
     * @param startYear an int describing a valid year indicating the first year in the range
     * @param finalYear an int describing a valid year indicating the last year in the range
     * @param gender    a String containing either "M" or "F"
     * @param rank      an int describing a rank
     * @return          a HashMap with names that are more frequently at the rank as the key and the number of years
     *                  the name has been at that rank over the range as the value
     */
    public Map<String, Integer> findNamesAtRankMostOften(int startYear, int finalYear, String gender, int rank) {
        DataUtils.handleGenderErrors(gender);
        Map<String, Integer> allFrequencies = new HashMap<>();
        Map<Integer, String> namesAtRank = findNamesAtRank(startYear, finalYear, gender, rank);
        namesAtRank.values().forEach(name -> allFrequencies.put(name, allFrequencies.getOrDefault(name, 0) + 1));
        Map<String, Integer> mostFrequent = new HashMap<>();
        int maxFrequency = Collections.max(allFrequencies.values());
        // filter through allFrequencies entries and filter only the entries that have maxFrequency and add to mostFrequent hashmap
        allFrequencies.entrySet().stream()
                .filter(entry -> entry.getValue() == maxFrequency)
                .forEach(entry -> mostFrequent.put(entry.getKey(), entry.getValue()));
        return mostFrequent;
    }
}
