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

    public String[] findTopRankedInYear(int year) {
        DataUtils.handleYearErrors(data, year);
        String[] topRanked = new String[2];
        Map<Integer, String> maleRankTable = data.getRankTable(year, MALE);
        Map<Integer, String> femaleRankTable = data.getRankTable(year, FEMALE);
        topRanked[0] = maleRankTable.get(TOP_RANK);
        topRanked[1] = femaleRankTable.get(TOP_RANK);
        return topRanked;
    }

    public int[] findCountByGenderLetterYear(int year, String gender, String letter) {
        DataUtils.handleYearErrors(data, year);
        int[] counts = new int[2];
        data.getDataFromYear(year).stream()
                .filter(nameArray -> nameArray[NAME_INDEX].startsWith(letter) && nameArray[GENDER_INDEX].equals(gender))
                .forEach(nameArray -> {
                    counts[0]++;
                    counts[1] += Integer.parseInt(nameArray[FREQ_INDEX]);
                });
        return counts;
    }

    public Map<Integer,Integer> findRankingsByNameGender(String name, String gender) {
        return findRankingsByNameGenderInRange(data.getStartYear(), data.getFinalYear(), name, gender);
    }

    public String findSameRankInRecentYear(String name, String gender, int year) {
        DataUtils.handleYearErrors(data, year);
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

    public List<String> findMostPopularInRange(String gender, int startYear, int finalYear) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        Map<String, Integer> topRankPerYear = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<Integer, String> rankTable = data.getRankTable(year, gender);
            String topRankedName = rankTable.get(TOP_RANK);
            topRankPerYear.putIfAbsent(topRankedName, 0);
            topRankPerYear.put(topRankedName, topRankPerYear.get(topRankedName) + 1);
        }
        int mostFrequent = Collections.max(topRankPerYear.values());
        return topRankPerYear.entrySet().stream()
                .filter(entry -> entry.getValue() == mostFrequent)
                .map(entry -> entry.getKey() + " for " + entry.getValue() + " years")
                .collect(Collectors.toList());
    }

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

    public Map<Integer, Integer> findRankingsByNameGenderInRange(int startYear, int finalYear, String name, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
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

    public Map<String, Integer> findRankingDifferenceFirstLastYears(int startYear, int finalYear, String name, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
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

    public String findNameLargestDifferenceFirstLastYears(int startYear, int finalYear, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
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
        return differences.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    public int findAverageRankOverRange(int startYear, int finalYear, String name, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        Map<Integer, Integer> rankings = findRankingsByNameGenderInRange(startYear, finalYear, name, gender);
        int sum = rankings.values().stream().reduce(0, (total, rank) -> {
            if (rank == null) {
                return total;
            } else {
                return total + rank;
            }
        });
        return sum/rankings.values().size();
    }

    public String findNameHighestAverageRank(int startYear, int finalYear, String gender) {
        DataUtils.handleYearErrors(data, startYear, finalYear);
        Map<Integer, String> rankTable = data.getRankTable(startYear, gender);
        Map<String, Integer> averageRanks = new HashMap<>();
        rankTable.values().forEach(name -> averageRanks.put(name, findAverageRankOverRange(startYear, finalYear, name, gender)));
        return averageRanks.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    public int findAverageRankRecentYears(String name, String gender, int numYears) {
        int recentYear = data.getFinalYear();
        return findAverageRankOverRange(recentYear - numYears + 1, recentYear, name, gender);
    }

    public Map<Integer, String> findNamesAtRank(int startYear, int finalYear, String gender, int rank) {
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

    public Map<String, Integer> findNamesAtRankMostOften(int startYear, int finalYear, String gender, int rank) {
        Map<String, Integer> allFrequencies = new HashMap<>();
        Map<Integer, String> namesAtRank = findNamesAtRank(startYear, finalYear, gender, rank);
        namesAtRank.values().forEach(name -> allFrequencies.put(name, allFrequencies.getOrDefault(name, 0) + 1));
        Map<String, Integer> mostFrequent = new HashMap<>();
        int maxFrequency = Collections.max(allFrequencies.values());
        allFrequencies.entrySet().stream()
                .filter(entry -> entry.getValue() == maxFrequency)
                .forEach(entry -> mostFrequent.put(entry.getKey(), entry.getValue()));
        return mostFrequent;
    }
}
