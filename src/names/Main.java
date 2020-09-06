package names;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    // CONFIGURATION
    private static final String DIRECTORY = "data/ssa_complete/";
    private static final String FILEPATH = DIRECTORY + "yob";
//    private static final String FILETYPE = ".txt";
    private static final String FEMALE = "F";
    private static final String MALE = "M";
    private static final int TOPRANK = 1;
    
    // TODO: add what it takes in, returns, example (javadoc)
    //TODO: clarify in javadocs comment what a line would look like
    public String[] getTopRankedInYear(int year) throws FileNotFoundException {
        String[] topRanked = new String[2];
        Map<Integer,String> maleRankTable = getRankTable(year, MALE);
        Map<Integer,String> femaleRankTable = getRankTable(year, FEMALE);
        topRanked[0] = maleRankTable.get(TOPRANK);
        topRanked[1] = femaleRankTable.get(TOPRANK);
        System.out.println(topRanked[0]);
        System.out.println(topRanked[1]);
        return topRanked;
    }

    public int[] getCountByGenderLetterYear(int year, String gender, String letter) throws FileNotFoundException {
        int[] counts = new int[2];
        List<String[]> data = parseFile(year);
        data.stream()
                .filter(nameArray -> nameArray[0].startsWith(letter) && nameArray[1].equals(gender))
                .forEach(nameArray -> {
                    counts[0]++;
                    counts[1] += Integer.parseInt(nameArray[2]);
                });
        System.out.println(counts[0] + " different names");
        System.out.println(counts[1] + " total babies");
        return counts;
    }

    public Map<Integer,Integer> getRankingsByNameGender(String name, String gender) throws FileNotFoundException {
        Map<Integer, Integer> rankings = new HashMap<>();
        for (File file : getListOfFiles()) {
            int year = getYearFromFileName(file);
            Map<Integer, String> rankTable = getRankTable(year, gender);
            rankTable.keySet().forEach(entry -> {
                if (rankTable.get(entry).equals(name)) {
                    rankings.put(year, entry);
                }
            });
            rankings.putIfAbsent(year, -1);
        }
        for (int year : rankings.keySet()) {
            System.out.println(year + " - " + rankings.get(year));
        }
        return rankings;
    }

    public String getSameRankInRecentYear(String name, String gender, int year) throws FileNotFoundException {
        Map<Integer, String> rankTable = getRankTable(year, gender);
        int nameRank = -1;
        for (int rank : rankTable.keySet()) {
            if (rankTable.get(rank).equals(name)) {
                nameRank = rank;
                break;
            }
        }
        if (nameRank == -1) {
            System.out.println("Name not found");
            return "Name not found";
        }

        // get most recent year from dataset
        File[] directoryListing = getListOfFiles();
        int recentYear = -1;
        for (File file : directoryListing) {
            recentYear = Math.max(recentYear, getYearFromFileName(file));
        }

        Map<Integer, String> recentYearRankTable = getRankTable(recentYear, gender);
        String matchedName = recentYearRankTable.get(nameRank);
        System.out.println(matchedName);
        return matchedName;
    }

    public List<String> getMostPopularInRange(String gender, int startYear, int finalYear) throws FileNotFoundException {
        Map<String, Integer> topRankPerYear = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            Map<Integer, String> rankTable = getRankTable(year, gender);
            String topRankedName = rankTable.get(TOPRANK);
            topRankPerYear.putIfAbsent(topRankedName, 0);
            topRankPerYear.put(topRankedName, topRankPerYear.get(topRankedName) + 1);
        }
        int mostFrequent = Collections.max(topRankPerYear.values());
        List<String> results = topRankPerYear.entrySet().stream()
                .filter(entry -> entry.getValue() == mostFrequent)
                .map(entry -> entry.getKey() + " for " + entry.getValue() + " years")
                .collect(Collectors.toList());
        results.forEach(System.out::println);
        return results;
    }

    public String[] mostPopularLetterGirls(int startYear, int finalYear) throws FileNotFoundException {
        Integer[] charCounts = new Integer[100];
        Arrays.fill(charCounts, 0);
        Map<Character, SortedSet<String>> listNamesOfChar = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            List<String[]> data = parseFile(year).stream()
                    .filter(nameArray -> nameArray[1].equals(FEMALE))
                    .collect(Collectors.toList());
            data.forEach(nameArray -> {
                char letter = nameArray[0].charAt(0);
                charCounts[letter]++;
                listNamesOfChar.putIfAbsent(letter, new TreeSet<>());
                listNamesOfChar.get(letter).add(nameArray[0]);
            });
        }

        List<Integer> charCountsList = Arrays.asList(charCounts);
        char mostPopularLetter = (char)charCountsList.indexOf(Collections.max(charCountsList));

        SortedSet<String> listOfNames = listNamesOfChar.get(mostPopularLetter);
        String[] results = new String[listOfNames.size() + 1];
        results[0] = Character.toString(mostPopularLetter);
        System.out.println("Most Popular Letter for Females: " + mostPopularLetter);
        int i = 1;
        for (String name : listOfNames) {
            results[i] = name;
            System.out.print(name + ", ");
            i++;
        }
        return results;
    }

    private List<String[]> parseFile(int year) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(DIRECTORY + "yob" + year + ".txt"));
        List<String[]> data = new ArrayList<>();
        while (scan.hasNextLine()) {
            data.add(scan.nextLine().split(","));
        }
        return data;
    }

    private Map<Integer,String> getRankTable(int year, String gender) throws FileNotFoundException {
        List<String[]> data = parseFile(year);
        int rank = 1;
        Map<Integer, String> rankTable = new HashMap<>();
        for (String[] nameArray : data) {
            if (nameArray[1].equals(gender)) {
                rankTable.put(rank, nameArray[0]);
                rank++;
            }
        }
        return rankTable;
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

    public static void main (String[] args) throws FileNotFoundException {
        Main main = new Main();

        System.out.println("Test Implementation #1");
        String[] topRanked = main.getTopRankedInYear(1900);

        System.out.println("\nTest Implementation #2");
        int[] counts = main.getCountByGenderLetterYear(1900, "M", "Q");

        System.out.println("\nBasic Implementation #1");
        Map<Integer, Integer> rankings = main.getRankingsByNameGender("Jason", "M");

        System.out.println("\nBasic Implementation #2");
        String name = main.getSameRankInRecentYear("Jason", "M", 1900);

        System.out.println("\nBasic Implementation #3");
        List<String> result1 = main.getMostPopularInRange("M", 2000, 2009);

        System.out.println("\nBasic Implementation #4");
        String[] result2 = main.mostPopularLetterGirls(1880, 2018);
    }
}
