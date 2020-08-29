package names;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main {
    /**
     * Start of the program.
     */
    // test implementation #1
    private static final int YEAR1 = 1900;

    // test implementation #2
    private static final int YEAR2 = 1900;
    private static final String GENDER = "F";
    private static final String LETTER = "Q";
    
    private static String[] getTopRankedYear(int year) throws FileNotFoundException {
        String[] topRanked = new String[2];
        Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
        boolean firstFemale = false;
        while(scanner.hasNextLine()) {
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

    private static int[] getCountByGenderLetterYear(int year, String gender, String letter) throws FileNotFoundException {
        int[] counts = new int[2];
        Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
        while(scanner.hasNextLine()) {
            String[] nameArray = scanner.nextLine().split(",");
            if (nameArray[1].equals(gender) && nameArray[0].substring(0,1).equals(letter)) {
                counts[0]++;
                counts[1] += Integer.valueOf(nameArray[2]);
            }
        }
        return counts;
    }
    public static void main (String[] args) throws FileNotFoundException {
        System.out.println("Test Implementation #1");
        for (String s : getTopRankedYear(YEAR1)) System.out.println(s);

        System.out.println("\nTest Implementation #2");
        int[] counts = getCountByGenderLetterYear(YEAR2, GENDER, LETTER);
        System.out.println(counts[0] + " different names");
        System.out.println(counts[1] + " total babies");
    }
}
