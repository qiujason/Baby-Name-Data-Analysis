package names;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {
    // CONFIGURATION
    private static final String DIRECTORY = "data/ssa_complete/";
    private static final String FILEPATH = DIRECTORY + "yob";
//    private static final String FILETYPE = ".txt";
    private static final String FEMALE = "F";
    private static final String MALE = "M";
    
    // TODO: add what it takes in, returns, example (javadoc)
    //TODO: clarify in javadocs comment what a line would look like
    public String[] getTopRankedInYear(int year) throws FileNotFoundException {
        String[] topRanked = new String[2];
        List<String[]> data = parseFile(year);
        Map<Integer,String> maleRankTable = getRankTable(data, MALE);
        Map<Integer,String> femaleRankTable = getRankTable(data, FEMALE);
        topRanked[0] = maleRankTable.get(1);
        topRanked[1] = femaleRankTable.get(1);
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
            List<String[]> data = parseFile(year);
            Map<Integer, String> rankTable = getRankTable(data, gender);
            boolean found = false;
            for (int rank : rankTable.keySet()) {
                if (rankTable.get(rank).equals(name)) {
                    rankings.put(year, rank);
                    found = true;
                    break;
                }
            }
            if (!found) {
                rankings.put(year, -1);
            }
        }
        for (int year : rankings.keySet()) {
            System.out.println(year + " - " + rankings.get(year));
        }
        return rankings;
    }

    public String getSameRankInRecentYear(String name, String gender, int year) throws FileNotFoundException {
        List<String[]> data = parseFile(year);
        Map<Integer, String> rankTable = getRankTable(data, gender);
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
        int recentyear = -1;
        for (File file : directoryListing) {
            recentyear = Math.max(recentyear, getYearFromFileName(file));
        }

        List<String[]> recentYearData = parseFile(recentyear);
        Map<Integer, String> recentYearRankTable = getRankTable(recentYearData, gender);
        String matchedName = recentYearRankTable.get(nameRank);
        System.out.println(matchedName);
        return matchedName;
    }

    public List<String> getMostPopularInRange(String gender, int startyear, int finalyear) throws FileNotFoundException {
        Map<String, Integer> years = new HashMap<>();
        for (int year = startyear; year <= finalyear; year++) {
            Scanner scanner = new Scanner(new File("data/" + FILEPATH + "/yob" + year + ".txt"));
            while (scanner.hasNextLine()) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals(gender)) {
                    years.putIfAbsent(nameArray[0], 0);
                    years.put(nameArray[0], years.get(nameArray[0]) + 1);
                    break;
                }
            }
        }
        int max = 0;
        for (String key : years.keySet()) {
            max = Math.max(max, years.get(key));
        }
        List<String> results = new ArrayList<>();
        for (String key : years.keySet()) {
            if (years.get(key) == max) {
                String result = key + " for " + years.get(key) + " years";
                System.out.println(result);
                results.add(result);
            }
        }
        return results;
    }

    public String[] mostPopularLetterGirls(int startyear, int finalyear) throws FileNotFoundException {
        Map<String, Integer> popular = new HashMap<>();
        Map<String, SortedSet<String>> listNames = new HashMap<>();
        for (int year = startyear; year <= finalyear; year++) {
            Scanner scanner = new Scanner(new File("data/" + FILEPATH + "/yob" + year + ".txt"));
            while (scanner.hasNextLine()) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals(FEMALE)) {
                    String letter = nameArray[0].substring(0, 1);
                    popular.putIfAbsent(letter, 0);
                    popular.put(letter, popular.get(letter) + Integer.parseInt(nameArray[2]));
                    listNames.putIfAbsent(letter, new TreeSet<>());
                    listNames.get(letter).add(nameArray[0]);
                }
            }
        }
        String mostPopularLetter = "";
        int max = 0;
        for (char i = 'Z'; i >= 'A'; i--) {
            String letter = Character.toString(i);
            if (popular.get(letter) == null) continue;
            int count = popular.get(letter);
            if (count >= max) {
                mostPopularLetter = letter;
                max = count;
            }
        }
        SortedSet<String> set = listNames.get(mostPopularLetter);
        String[] results = new String[set.size() + 1];
        results[0] = mostPopularLetter;
        System.out.println("Most Popular Letter for Females: " + mostPopularLetter);
        int i = 1;
        for (String name : set) {
            results[i] = name;
            System.out.println(name);
            i++;
        }
        return results;
    }

    private ArrayList<String[]> parseFile(int year) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(DIRECTORY + "yob" + year + ".txt"));
        ArrayList<String[]> data = new ArrayList<>();
        while (scan.hasNextLine()) {
            data.add(scan.nextLine().split(","));
        }
        return data;
    }

    private Map<Integer,String> getRankTable(List<String[]> data, String gender) {
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
        String name = main.getSameRank("Jason", "M", 1900);

        System.out.println("\nBasic Implementation #3");
        List<String> result1 = main.getMostPopularInRange("M", 2000, 2009);

        System.out.println("\nBasic Implementation #4");
        String[] result2 = main.mostPopularLetterGirls(1880, 2018);
    }
}
