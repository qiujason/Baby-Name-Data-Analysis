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
    // first and last years of the dataset
    private final int FIRSTYEAR = 1880;
    private final int LASTYEAR = 2018;

    // test implementation #1
    private final int YEAR1 = 1900;

    // test implementation #2
    private final int YEAR2 = 1900;
    private final String GENDER1 = "F";
    private final String LETTER = "Q";

    // basic implementation #1
    private final String NAME1 = "John";
    private final String GENDER2 = "M";

    // basic implementation #2
    private final int YEAR3 = 2000;
    private final String NAME2 = "Jason";
    private final String GENDER3 = "M";

    // basic implementation #3
    private final int TOPRANKS = 10;
    private final int START_YEAR = 2000;
    private final int FINAL_YEAR = 2009;
    private final String GENDER4 = "M";
    
    public String[] getTopRankedYear(int year) throws FileNotFoundException {
        String[] topRanked = new String[2];
        Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
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
        return topRanked;
    }

    public int[] getCountByGenderLetterYear(int year, String gender, String letter) throws FileNotFoundException {
        int[] counts = new int[2];
        Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
        while (scanner.hasNextLine()) {
            String[] nameArray = scanner.nextLine().split(",");
            if (nameArray[1].equals(gender) && nameArray[0].substring(0,1).equals(letter)) {
                counts[0]++;
                counts[1] += Integer.parseInt(nameArray[2]);
            }
        }
        return counts;
    }

    public Map<Integer,Integer> getRankingsNameGender(String name, String gender) throws FileNotFoundException {
        Map<Integer, Integer> rankings = new HashMap<>();
        for (int year = FIRSTYEAR; year <= LASTYEAR; year++) {
            Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
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
        return rankings;
    }

    public String getSameRank(String name, String gender, int year) throws FileNotFoundException {
        int rank = 0;
        Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
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
            scanner = new Scanner(new File("data/ssa_complete/yob2018.txt")); // most recent year
            int num = 0;
            while (scanner.hasNextLine() && num < rank) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals(gender)) {
                    num++;
                    if (num == rank) return nameArray[0];
                }
            }
        }
        return "Name not found";
    }

    public void getMostPopularInRange(Map<String, Integer> ranking, Map<String, Integer> years, String gender, int startyear, int finalyear) throws FileNotFoundException {
        for (int year = startyear; year < finalyear; year++) {
            Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
            int rank = 0;
            while (scanner.hasNextLine() && rank < TOPRANKS) {
                String[] nameArray = scanner.nextLine().split(",");
                if (nameArray[1].equals(gender)) {
                    ranking.putIfAbsent(nameArray[0], 0);
                    ranking.put(nameArray[0], ranking.get(nameArray[0]) + rank);
                    years.putIfAbsent(nameArray[0], 0);
                    years.put(nameArray[0], years.get(nameArray[0]) + 1);
                    rank++;
                }
            }
        }
    }

    public static void main (String[] args) throws FileNotFoundException {
        Main main = new Main();

        System.out.println("Test Implementation #1");
        System.out.println(main.YEAR1);
        for (String s : main.getTopRankedYear(main.YEAR1)) System.out.println(s);

        System.out.println("\nTest Implementation #2");
        System.out.println(main.YEAR2 + " - " + main.LETTER + " (" + main.GENDER1 + ")");
        int[] counts = main.getCountByGenderLetterYear(main.YEAR2, main.GENDER1, main.LETTER);
        System.out.println(counts[0] + " different names");
        System.out.println(counts[1] + " total babies");

        System.out.println("\nBasic Implementation #1");
        System.out.println(main.NAME1 + " (" + main.GENDER2 + ")");
        Map<Integer, Integer> rankings = main.getRankingsNameGender(main.NAME1, main.GENDER2);
        for (int year : rankings.keySet()) System.out.println(year + " - " + rankings.get(year));

        System.out.println("\nBasic Implementation #2");
        System.out.println(main.getSameRank(main.NAME2, main.GENDER3, main.YEAR3));

        System.out.println("\nBasic Implementation #3");
        Map<String, Integer> ranking = new HashMap<>();
        Map<String, Integer> years = new HashMap<>();
        main.getMostPopularInRange(ranking, years, main.GENDER4, main.START_YEAR, main.FINAL_YEAR);
        SortedSet<String> keys = new TreeSet<>((a,b)->{
            int val = years.get(b) - years.get(a);
            return val != 0 ? val : ranking.get(a)-ranking.get(b);
        });
        keys.addAll(years.keySet());
        int i = 1;
        for (String key : keys) {
            System.out.println(i + ". " + key + " for " + years.get(key) + " years");
            i++;
        }
    }
}
