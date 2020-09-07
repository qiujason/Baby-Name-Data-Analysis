package names;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ParseData {
    private static final int NAMEINDEX = 0;
    private static final int GENDERINDEX = 1;

    private String directory;
    private int startYear;
    private int finalYear;
    private Map<Integer, List<String[]>> data;

    public ParseData(String directory) {
        this.directory = directory;
        this.startYear = DataUtils.getOldestYear(directory);
        this.finalYear = DataUtils.getRecentYear(directory);
        this.data = new HashMap<>();
        for (int year = startYear; year <= finalYear; year++) {
            data.put(year, parseFile(year));
        }
    }

    public Map<Integer, String> getRankTable(int year, String gender) {
        int rank = 1;
        Map<Integer, String> rankTable = new HashMap<>();
        for (String[] nameArray : data.get(year)) {
            if (nameArray[GENDERINDEX].equals(gender)) {
                rankTable.put(rank, nameArray[NAMEINDEX]);
                rank++;
            }
        }
        return rankTable;
    }

    public Map<String, Integer> getRankTableInverse(int year, String gender) {
        int rank = 1;
        Map<String, Integer> rankTableInverse = new HashMap<>();
        for (String[] nameArray : data.get(year)) {
            if (nameArray[GENDERINDEX].equals(gender)) {
                rankTableInverse.put(nameArray[NAMEINDEX], rank);
                rank++;
            }
        }
        return rankTableInverse;
    }

    public List<String[]> getDataFromYear(int year) {
        return data.get(year);
    }

    public int getStartYear() {
        return startYear;
    }

    public int getFinalYear() {
        return finalYear;
    }

    private List<String[]> parseFile(int year) {
        String filepath = directory + "yob" + year + ".txt";
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
}
