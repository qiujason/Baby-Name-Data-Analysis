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
    public static void main (String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("1."); // question 1
        System.out.print("Year: ");
        String year = scan.next();
        Scanner scanner = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
        boolean firstFemale = false;
        while(scanner.hasNextLine()) {
            String[] nameArray = scanner.nextLine().split(",");
            if (!firstFemale && nameArray[1].equals("F")) {
                firstFemale = true;
                System.out.println(nameArray[0]);
            }
            else if (nameArray[1].equals("M")) {
                System.out.println(nameArray[0]);
                break;
            }
        }

        System.out.println("2."); // question 2
        System.out.print("Year: ");
        year = scan.next();
        System.out.print("Gender (M/F): ");
        String gender = scan.next();
        System.out.print("First Letter: ");
        String letter = scan.next();
        Scanner sc = new Scanner(new File("data/ssa_complete/yob" + year + ".txt"));
        int count = 0;
        int totalBabies = 0;
        while(sc.hasNextLine()) {
            String[] nameArray = sc.nextLine().split(",");
            if (nameArray[1].equals(gender) && nameArray[0].substring(0,1).equals(letter)) {
                count++;
                totalBabies += Integer.valueOf(nameArray[2]);
            }
        }
        System.out.println(count);
        System.out.println(totalBabies);
    }
}
