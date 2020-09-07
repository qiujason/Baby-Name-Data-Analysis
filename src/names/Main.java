package names;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    // CONFIGURATION
    private static final String DIRECTORY = "data/ssa_complete/";
    private static final String MALE = "M";
    private static final String FEMALE = "F";
    private static final int NAMEINDEX = 0;
    private static final int GENDERINDEX = 1;
    private static final int FREQINDEX = 2;
    private static final int TOPRANK = 1;
    //TODO: global variables for namearray indices
    //TODO: make parsefile class. i can create a parsefile object that reads the data
    
    // TODO: add what it takes in, returns, example (javadoc)
    //TODO: clarify in javadocs comment what a line would look like
    public String[] findTopRankedInYear(int year) {
        String[] topRanked = new String[2];
        Map<Integer, String> maleRankTable = getRankTable(year, MALE);
        Map<Integer, String> femaleRankTable = getRankTable(year, FEMALE);
        topRanked[0] = maleRankTable.get(TOPRANK);
        topRanked[1] = femaleRankTable.get(TOPRANK);
        return topRanked;
    }

    public int[] findCountByGenderLetterYear(int year, String gender, String letter) {
        int[] counts = new int[2];
        List<String[]> data = parseFile(year);
        data.stream()
                .filter(nameArray -> nameArray[NAMEINDEX].startsWith(letter) && nameArray[GENDERINDEX].equals(gender))
                .forEach(nameArray -> {
                    counts[0]++;
                    counts[1] += Integer.parseInt(nameArray[FREQINDEX]);
                });
        return counts;
    }

    public Map<Integer,Integer> findRankingsByNameGender(String name, String gender) {
        int startYear = getOldestYear();
        int finalYear = getRecentYear();
        return findRankingsByNameGenderInRange(startYear, finalYear, name, gender);
    }

    public String findSameRankInRecentYear(String name, String gender, int year) {
        Map<Integer, String> rankTable = getRankTable(year, gender);
        int nameRank = -1;
        for (int rank : rankTable.keySet()) {
            if (rankTable.get(rank).equals(name)) {
                nameRank = rank;
                break;
            }
        }
        if (nameRank == -1) {
            return "Name not found";
        }
        int recentYear = getRecentYear();
        if (recentYear != -1) {
            Map<Integer, String> recentYearRankTable = getRankTable(recentYear, gender);
            return recentYearRankTable.get(nameRank);
        } else {
            return "No files found";
        }
    }

    public List<String> findMostPopularInRange(String gender, int startYear, int finalYear) {
        Map<String, Integer> topRankPerYear = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<Integer, String> rankTable = getRankTable(year, gender);
            String topRankedName = rankTable.get(TOPRANK);
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
        Integer[] charCounts = new Integer[100];
        Arrays.fill(charCounts, 0);
        Map<Character, SortedSet<String>> listNamesOfChar = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            fillCountsAndNamesOfFirstLetters(year, FEMALE, charCounts, listNamesOfChar);
        }

        List<Integer> charCountsList = Arrays.asList(charCounts);
        char mostPopularLetter = (char)charCountsList.indexOf(Collections.max(charCountsList));

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

    //TODO: handle null pointer exceptions
    public Map<Integer, Integer> findRankingsByNameGenderInRange(int startYear, int finalYear, String name, String gender) {
        Map<Integer, Integer> rankings = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<String, Integer> rankTableInverse = getRankTableInverse(year, gender);
            try {
                rankings.put(year, rankTableInverse.get(name));
            } catch (NullPointerException e) {
                rankings.put(year, null);
            }
        }
        return rankings;
    }

    public Map<String, Integer> findRankingDifferenceFirstLastYears(int startYear, int finalYear, String name, String gender) {
        Map<Integer, Integer> rankingStartYear = findRankingsByNameGenderInRange(startYear, startYear, name, gender);
        Map<Integer, Integer> rankingFinalYear = findRankingsByNameGenderInRange(finalYear, finalYear, name, gender);
        Map<String, Integer> results = new HashMap<>();
        try {
            int firstRanking = rankingStartYear.get(startYear);
            int lastRanking = rankingFinalYear.get(finalYear);
            results.put(Integer.toString(startYear), firstRanking);
            results.put(Integer.toString(finalYear), lastRanking);
            results.put("Difference (rankings up)", firstRanking - lastRanking);
            return results;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String findNameLargestDifferenceFirstLastYears(int startYear, int finalYear, String gender) {
        Map<Integer, String> rankTable = getRankTable(startYear, gender);
        Map<String, Integer> differences = new HashMap<>();
        rankTable.values().forEach(name -> {
            try {
                Map<String, Integer> rankingDifferencesForName = findRankingDifferenceFirstLastYears(startYear, finalYear, name, gender);
                differences.put(name, Math.abs(rankingDifferencesForName.get("Difference (rankings up)")));
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
        Map<Integer, String> rankTable = getRankTable(startYear, gender);
        Map<String, Integer> averageRanks = new HashMap<>();
        rankTable.values().forEach(name -> averageRanks.put(name, findAverageRankOverRange(startYear, finalYear, name, gender)));
        return averageRanks.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    public int findAverageRankRecentYears(String name, String gender, int numYears) {
        int recentYear = getRecentYear();
        return findAverageRankOverRange(recentYear - numYears + 1, recentYear, name, gender);
    }

    public Map<Integer, String> findNamesAtRank(int startYear, int finalYear, String gender, int rank) {
        Map<Integer, String> namesAtRank = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<Integer, String> rankTable = getRankTable(year, gender);
            namesAtRank.put(year, rankTable.get(rank));
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

    private List<String[]> parseFile(int year) {
        String filepath = DIRECTORY + "yob" + year + ".txt";
        List<String[]> data = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new File(filepath));
            while (scan.hasNextLine()) {
                data.add(scan.nextLine().split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println(filepath + " not found");
        }
        return data;
    }

    private Map<Integer, String> getRankTable(int year, String gender) {
        List<String[]> data = parseFile(year);
        int rank = 1;
        Map<Integer, String> rankTable = new HashMap<>();
        for (String[] nameArray : data) {
            if (nameArray[GENDERINDEX].equals(gender)) {
                rankTable.put(rank, nameArray[NAMEINDEX]);
                rank++;
            }
        }
        return rankTable;
    }

    // hashmap is guaranteed to have unique keys and values
    private Map<String, Integer> getRankTableInverse(int year, String gender) {
        List<String[]> data = parseFile(year);
        int rank = 1;
        Map<String, Integer> rankTableInverse = new HashMap<>();
        for (String[] nameArray : data) {
            if (nameArray[GENDERINDEX].equals(gender)) {
                rankTableInverse.put(nameArray[NAMEINDEX], rank);
                rank++;
            }
        }
        return rankTableInverse;
    }

    private void fillCountsAndNamesOfFirstLetters(int year, String gender, Integer[] charCounts,
                                          Map<Character, SortedSet<String>> listNamesOfChar) {
        List<String[]> data = parseFile(year).stream()
                .filter(nameArray -> nameArray[GENDERINDEX].equals(gender))
                .collect(Collectors.toList());
        data.forEach(nameArray -> {
            char letter = nameArray[NAMEINDEX].charAt(0);
            charCounts[letter]++;
            listNamesOfChar.putIfAbsent(letter, new TreeSet<>());
            listNamesOfChar.get(letter).add(nameArray[NAMEINDEX]);
        });
    }

    private File[] getListOfFiles() {
        File dir = new File(DIRECTORY);
        if (dir == null) {
            return new File[0];
        }
        return dir.listFiles();
    }

    private int getYearFromFileName(File file) {
        String yearString = file.getName().replaceAll("[^0-9]","");
        return Integer.parseInt(yearString);
    }

    private int getRecentYear() {
        File[] directoryListing = getListOfFiles();
        int recentYear = -1;
        for (File file : directoryListing) {
            recentYear = Math.max(recentYear, getYearFromFileName(file));
        }
        return recentYear;
    }

    private int getOldestYear() {
        File[] directoryListing = getListOfFiles();
        int recentYear = 10000; // large number impossible to be a year
        for (File file : directoryListing) {
            recentYear = Math.min(recentYear, getYearFromFileName(file));
        }
        return recentYear;
    }

    public static void main (String[] args) {
        Main main = new Main();

        System.out.println("Test Implementation #1");
        String[] topRanked = main.findTopRankedInYear(1900);
        for (String name : topRanked) {
            System.out.println(name);
        }

        System.out.println("\nTest Implementation #2");
        int[] counts = main.findCountByGenderLetterYear(1900, "M", "Q");
        System.out.println(counts[0] + " different names");
        System.out.println(counts[1] + " total babies");

        System.out.println("\nBasic Implementation #1");
        Map<Integer, Integer> rankings = main.findRankingsByNameGender("Jason", "M");
        rankings.forEach((year, ranking) -> System.out.println(year + " - " + ranking));

        System.out.println("\nBasic Implementation #2");
        System.out.println(main.findSameRankInRecentYear("Jason", "M", 1900));

        System.out.println("\nBasic Implementation #3");
        List<String> mostPopular = main.findMostPopularInRange("M", 2000, 2009);
        mostPopular.forEach(System.out::println);

        System.out.println("\nBasic Implementation #4");
        String[] mostPopularLetterNames = main.findMostPopularLetterGirls(1880, 2018);
        System.out.println("Most Popular Letter for Females: " + mostPopularLetterNames[0]);
        for (String name : mostPopularLetterNames) {
            System.out.print(name + ", ");
        }

        System.out.println("\n\nComplete Implementation #1");
        Map<Integer, Integer> rankingsInRange = main.findRankingsByNameGenderInRange(1900, 2000, "Jason", "M");
        rankingsInRange.forEach((year, ranking) -> System.out.println(year + " - " + ranking));

        System.out.println("\nComplete Implementation #2");
        Map<String, Integer> rankingDifferences = main.findRankingDifferenceFirstLastYears(1900, 2000, "Jason", "M");
        if (rankingDifferences == null) {
            System.out.println("Name not found in one of the years");
        } else {
            rankingDifferences.forEach((k, ranking) -> System.out.println(k + ": " + ranking));
        }

        System.out.println("\nComplete Implementation #3");
//        System.out.println(main.findNameLargestDifferenceFirstLastYears(1900, 2000, MALE));

        System.out.println("\nComplete Implementation #4");
        System.out.println(main.findAverageRankOverRange(2014, 2018, "Jason", MALE));

        System.out.println("\nComplete Implementation #5");
//        System.out.println(main.findNameHighestAverageRank(1971, 1973, FEMALE));
        
        System.out.println("\nComplete Implementation #6");
        System.out.println(main.findAverageRankRecentYears("Jason", MALE, 5));

        System.out.println("\nComplete Implementation #7");
        Map<Integer, String> namesAtRank = main.findNamesAtRank(2000, 2018, MALE, 1);
        namesAtRank.forEach((year, name) -> System.out.println(year + " - " + name));

        System.out.println("\nComplete Implementation #8");
        Map<String, Integer> mostFrequent = main.findNamesAtRankMostOften(2000, 2018, MALE, 1);
        mostFrequent.forEach((name, frequency) -> System.out.println(name + ": " + frequency));

    }
}
