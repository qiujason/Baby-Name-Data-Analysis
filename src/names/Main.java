package names;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main {
    /**
     * Start of the program.
     */
    private static final int FIRSTYEAR = 1880;
    private static final int LASTYEAR = 2018;

    // test implementation #1
    private static final int YEAR1 = 1900;

    // test implementation #2
    private static final int YEAR2 = 1900;
    private static final String GENDER1 = "F";
    private static final String LETTER = "Q";

    // basic implementation #1
    private static final String NAME1 = "John";
    private static final String GENDER2 = "M";
    
    public static String[] getTopRankedYear(int year) throws FileNotFoundException {
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

    public static int[] getCountByGenderLetterYear(int year, String gender, String letter) throws FileNotFoundException {
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

    public static Map<Integer,Integer> getRankingsNameGender(String name, String gender) throws FileNotFoundException {
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

    public static void main (String[] args) throws FileNotFoundException {
        System.out.println("Test Implementation #1");
        System.out.println(YEAR1);
        for (String s : getTopRankedYear(YEAR1)) System.out.println(s);

        System.out.println("\nTest Implementation #2");
        System.out.println(YEAR2 + " - " + LETTER + " (" + GENDER1 + ")");
        int[] counts = getCountByGenderLetterYear(YEAR2, GENDER1, LETTER);
        System.out.println(counts[0] + " different names");
        System.out.println(counts[1] + " total babies");

        System.out.println("\nBasic Implementation #1");
        System.out.println(NAME1 + " (" + GENDER2 + ")");
        Map<Integer, Integer> rankings = getRankingsNameGender(NAME1, GENDER2);
        for (int year : rankings.keySet()) System.out.println(year + " - " + rankings.get(year));
    }
}
