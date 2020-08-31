package names;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main {
    /**
     * Start of the program.
     */
    // CONFIGURATION
    // for all methods
    private static final int RECENTYEAR = 2018;
    private static final String DIRECTORY = "ssa_complete";

    // change arguments for each method
    // test implementation #1
    private final int YEAR1 = 1900;

    // test implementation #2
    private final int YEAR2 = 1900;
    private final String GENDER1 = "F"; // M or F
    private final String LETTER = "Q"; // First letter of names

    // basic implementation #1
    private final String NAME1 = "John";
    private final String GENDER2 = "M"; // M or F

    // basic implementation #2
    private final int YEAR3 = 2000;
    private final String NAME2 = "Jason";
    private final String GENDER3 = "M"; // M or F

    // basic implementation #3
    private final int TOPRANKS = 10;
    private final int START_YEAR1 = 2000;
    private final int FINAL_YEAR1 = 2009;
    private final String GENDER4 = "M"; // M or F

    // basic implementation #4
    private final int START_YEAR2 = 2000;
    private final int FINAL_YEAR2 = 2009;
    
    public String[] getTopRankedYear(int year) throws FileNotFoundException {
        String[] topRanked = new String[2];
        Scanner scanner = new Scanner(new File("data/" + DIRECTORY + "/yob" + year + ".txt"));
        boolean firstFemale = false;
        while (scanner.hasNextLine()) {
            String[] nameArray = scanner.nextLine().split(",");
            if (!firstFemale && nameArray[1].equals("F")) {
                firstFemale = true;
                topRanked[0] = nameArray[0];
            }
            else if (nameArray[1].equals("M")) {
                topRanked[1] = nameArray[0];
                break;
            }
        }
        for (String s : topRanked) System.out.println(s);
        return topRanked;
    }

    public int[] getCountByGenderLetterYear(int year, String gender, String letter) throws FileNotFoundException {
        int[] counts = new int[2];
        Scanner scanner = new Scanner(new File("data/" + DIRECTORY + "/yob" + year + ".txt"));
        while (scanner.hasNextLine()) {
            String[] nameArray = scanner.nextLine().split(",");
            if (nameArray[1].equals(gender) && nameArray[0].substring(0,1).equals(letter)) {
                counts[0]++;
                counts[1] += Integer.parseInt(nameArray[2]);
            }
        }
        System.out.println(counts[0] + " different names");
        System.out.println(counts[1] + " total babies");
        return counts;
    }

    public Map<Integer,Integer> getRankingsNameGender(String name, String gender) throws FileNotFoundException {
        Map<Integer, Integer> rankings = new HashMap<>();
        for (int year = 1880; year <= 2018; year++) {
            Scanner scanner = new Scanner(new File("data/" + DIRECTORY + "/yob" + year + ".txt"));
            boolean found = false;
            while (scanner.hasNextLine()) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals(gender) && nameArray[0].equals(name)) {
                    rankings.put(year, Integer.parseInt(nameArray[2]));
                    found = true;
                    break;
                }
            }
            // name not found
            if (!found) rankings.put(year, -1);
        }
        for (int year : rankings.keySet()) System.out.println(year + " - " + rankings.get(year));
        return rankings;
    }

    public String getSameRank(String name, String gender, int year) throws FileNotFoundException {
        int rank = 0;
        Scanner scanner = new Scanner(new File("data/" + DIRECTORY + "/yob" + year + ".txt"));
        boolean found = false;
        while (scanner.hasNextLine()) {
            String[] nameArray = scanner.nextLine().split(",");
            if (nameArray[1].equals(gender)) {
                rank++;
                if (nameArray[0].equals(name)) {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            scanner = new Scanner(new File("data/" + DIRECTORY + "/yob" + RECENTYEAR + ".txt")); // most recent year
            int num = 0;
            while (scanner.hasNextLine() && num < rank) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals(gender)) {
                    num++;
                    if (num == rank) {
                        System.out.println(nameArray[0]);
                        return nameArray[0];
                    }
                }
            }
        }
        System.out.println("Name not found");
        return "Name not found";
    }

    public String[] getMostPopularInRange(String gender, int startyear, int finalyear) throws FileNotFoundException {
        Map<String, Integer> ranking = new HashMap<>();
        Map<String, Integer> years = new HashMap<>();
        for (int year = startyear; year <= finalyear; year++) {
            Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
            int rank = 0;
            while (scanner.hasNextLine() && rank < TOPRANKS) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals(gender)) {
                    ranking.putIfAbsent(nameArray[0], 0);
                    ranking.put(nameArray[0], ranking.get(nameArray[0]) + Integer.parseInt(nameArray[2]));
                    years.putIfAbsent(nameArray[0], 0);
                    years.put(nameArray[0], years.get(nameArray[0]) + 1);
                    rank++;
                }
            }
        }
        SortedSet<String> keys = new TreeSet<>((a,b)-> ranking.get(b)-ranking.get(a));
        keys.addAll(years.keySet());
        String[] results = new String[keys.size()];
        int i = 1;
        for (String key : keys) {
            String result = i + ". " + key + " for " + years.get(key) + " years";
            System.out.println(i + ". " + key + " for " + years.get(key) + " years");
            results[i-1] = result;
            i++;
        }
        return results;
    }

    public String[] mostPopularLetterGirls(int startyear, int finalyear) throws FileNotFoundException {
        Map<String, Integer> popular = new HashMap<>();
        Map<String, SortedSet<String>> listNames = new HashMap<>();
        for (int year = startyear; year <= finalyear; year++) {
            Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
            while (scanner.hasNextLine()) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals("F")) {
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

    public static void main (String[] args) throws FileNotFoundException {
        Main main = new Main();

        System.out.println("Test Implementation #1");
        System.out.println(main.YEAR1);
        String[] topRanked = main.getTopRankedYear(main.YEAR1);

        System.out.println("\nTest Implementation #2");
        System.out.println(main.YEAR2 + " - " + main.LETTER + " (" + main.GENDER1 + ")");
        int[] counts = main.getCountByGenderLetterYear(main.YEAR2, main.GENDER1, main.LETTER);

        System.out.println("\nBasic Implementation #1");
        System.out.println(main.NAME1 + " (" + main.GENDER2 + ")");
        Map<Integer, Integer> rankings = main.getRankingsNameGender(main.NAME1, main.GENDER2);

        System.out.println("\nBasic Implementation #2");
        String name = main.getSameRank(main.NAME2, main.GENDER3, main.YEAR3);

        System.out.println("\nBasic Implementation #3");
        System.out.println(main.START_YEAR1 + " - " + main.FINAL_YEAR1);
        String[] result1 = main.getMostPopularInRange(main.GENDER4, main.START_YEAR1, main.FINAL_YEAR1);

        System.out.println("\nBasic Implementation #4");
        System.out.println(main.START_YEAR2 + " - " + main.FINAL_YEAR2);
        String[] result2 = main.mostPopularLetterGirls(main.START_YEAR2, main.FINAL_YEAR2);
    }
}
