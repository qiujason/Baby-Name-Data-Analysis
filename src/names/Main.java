package names;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.BinaryOperator;


public class Main {
    // CONFIGURATION
    private static final String DIRECTORY = "data/ssa_complete/yob";
    private static final String FILETYPE = ".txt";
    private static final String FEMALE = "F";
    private static final String MALE = "M";
    
    // TODO: add what it takes in, returns, example (javadoc)
    //TODO: clarify in javadocs comment what a line would look like
    public String[] getTopRankedInYear(int year) throws FileNotFoundException {
        String[] topRanked = new String[2];
        List<String[]> data = parseFile(year);
        boolean firstFemale = false;
        for (String[] nameArray : data) {
            if (!firstFemale && nameArray[1].equals(FEMALE)) { //TODO: convert to helper method
                firstFemale = true;
                topRanked[0] = nameArray[0];
            }
            else if (nameArray[1].equals(MALE)) {
                topRanked[1] = nameArray[0];
                break;
            }
        }
        for (String s : topRanked) {
            System.out.println(s);
        }
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

    public Map<Integer,Integer> getRankingsNameGender(String name, String gender) throws FileNotFoundException {
        Map<Integer, Integer> rankings = new HashMap<>();
        String filepath = "data/" + DIRECTORY;
        File dir = new File(filepath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                // get year from text file name by only extracting the numbers
                String yearString = file.getName().replaceAll("[^0-9]","");
                // convert yearString to int
                int year = Integer.parseInt(yearString);
                Scanner scanner = new Scanner(file);
                boolean found = false;
                int rank = 1;
                while (scanner.hasNextLine()) {
                    String[] nameArray = scanner.nextLine().split(",");
                    if (nameArray[1].equals(gender)) {
                        if (nameArray[0].equals(name)) {
                            rankings.put(year, rank);
                            found = true;
                            break;
                        }
                        rank++;
                    }
                }
                // name not found
                if (!found) {
                    rankings.put(year, -1);
                }
            }
        }
        for (int year : rankings.keySet()) {
            System.out.println(year + " - " + rankings.get(year));
        }
        return rankings;
    }

    public String getSameRank(String name, String gender, int year) throws FileNotFoundException {
        int rank = 0;
        boolean found = false;
        ArrayList<String[]> data = new ArrayList<>();
        for (String[] nameArray : data) {
            if (nameArray[1].equals(gender)) {
                rank++;
                if (nameArray[0].equals(name)) {
                    found = true;
                    break;
                }
            }
        }
//        while (scanner.hasNextLine()) {
//            String[] nameArray = scanner.nextLine().split(",");
//            if (nameArray[1].equals(gender)) {
//                rank++;
//                if (nameArray[0].equals(name)) {
//                    found = true;
//                    break;
//                }
//            }
//        }
        // find most recent year
        // get all text files within directory
        File dir = new File("data/" + DIRECTORY);
        File[] directoryListing = dir.listFiles();
        int recentyear = -1;
        if (directoryListing != null) {
            for (File file : directoryListing) {
                // get year from text file name by only extracting the numbers
                String yearString = file.getName().replaceAll("[^0-9]", "");
                // convert yearString to int
                recentyear = Math.max(recentyear, Integer.parseInt(yearString));
            }
        }
        if (found) {
            // scan file of most recent year
            Scanner scanner = new Scanner(new File(dir + "/yob" + recentyear + ".txt")); // most recent year
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

    public List<String> getMostPopularInRange(String gender, int startyear, int finalyear) throws FileNotFoundException {
        Map<String, Integer> years = new HashMap<>();
        for (int year = startyear; year <= finalyear; year++) {
            Scanner scanner = new Scanner(new File("data/" + DIRECTORY + "/yob" + year + ".txt"));
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
            Scanner scanner = new Scanner(new File("data/" + DIRECTORY + "/yob" + year + ".txt"));
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
        Scanner scan = new Scanner(new File(DIRECTORY + year + FILETYPE));
        ArrayList<String[]> data = new ArrayList<>();
        while (scan.hasNextLine()) {
            data.add(scan.nextLine().split(","));
        }
        return data;
    }

    public static void main (String[] args) throws FileNotFoundException {
        Main main = new Main();

        System.out.println("Test Implementation #1");
        String[] topRanked = main.getTopRankedInYear(1900);

        System.out.println("\nTest Implementation #2");
        int[] counts = main.getCountByGenderLetterYear(1900, "M", "Q");

        System.out.println("\nBasic Implementation #1");
        Map<Integer, Integer> rankings = main.getRankingsNameGender("Jason", "M");

        System.out.println("\nBasic Implementation #2");
        String name = main.getSameRank("Jason", "M", 1900);

        System.out.println("\nBasic Implementation #3");
        List<String> result1 = main.getMostPopularInRange("M", 2000, 2009);

        System.out.println("\nBasic Implementation #4");
        String[] result2 = main.mostPopularLetterGirls(1880, 2018);
    }
}
