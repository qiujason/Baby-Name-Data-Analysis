package names;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ParseData {
    private static final int NAME_INDEX = 0;
    private static final int GENDER_INDEX = 1;

    private final String directory;
    private final int startYear;
    private final int finalYear;
    private final Map<Integer, List<String[]>> allData;

    /**
     * Creates a ParseData object which parses all the text files within the directory
     * @param directory contains the directory path of all text files within a dataset
     *                  can be a URL or a local path
     */
    public ParseData(String directory) {
        this.directory = directory;
        this.allData = new HashMap<>();
        if (directory.contains("https://")) {
            handleURL();
            this.startYear = Collections.min(allData.keySet());
            this.finalYear = Collections.max(allData.keySet());
        } else {
            this.startYear = DataUtils.getOldestYear(directory);
            this.finalYear = DataUtils.getRecentYear(directory);
        }
        for (int year = startYear; year <= finalYear; year++) {
            try {
                allData.put(year, parseFile(year));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a map table that labels each rank with its corresponding name
     * @param year      an int describing a valid year with a corresponding text file in the dataset
     * @param gender    a String containing either "M" or "F"
     * @return          a HashMap with rank as the key and name as the value
     */
    public Map<Integer, String> getRankTable(int year, String gender) {
        int rank = 1;
        Map<Integer, String> rankTable = new HashMap<>();
        for (String[] nameArray : allData.get(year)) {
            if (nameArray[GENDER_INDEX].equals(gender)) {
                rankTable.put(rank, nameArray[NAME_INDEX]);
                rank++;
            }
        }
        return rankTable;
    }

    /**
     * Returns an inverse map table that labels each name with its corresponding rank
     * @param year      an int describing a valid year with a corresponding text file in the dataset
     * @param gender    a String containing either "M" or "F"
     * @return          a HashMap with name as the key and rank as the value
     */
    public Map<String, Integer> getRankTableInverse(int year, String gender) {
        int rank = 1;
        Map<String, Integer> rankTableInverse = new HashMap<>();
        for (String[] nameArray : allData.get(year)) {
            if (nameArray[GENDER_INDEX].equals(gender)) {
                rankTableInverse.put(nameArray[NAME_INDEX], rank);
                rank++;
            }
        }
        return rankTableInverse;
    }

    /**
     * helper method that parses through the HTML of a URL and grabs the names of text files. The year is pulled from
     * the text file names and is then used to initialize allData
     */
    private void handleURL() {
        try {
            URL dataURL = new URL(directory);
            BufferedReader in = new BufferedReader(new InputStreamReader(dataURL.openStream()));
            in.lines().filter(line -> line.contains("href") && line.contains(".txt"))
                    .map(line -> {
                        int startIndex = line.indexOf("href=\"") + "href\"".length() + 1;
                        return line.substring(startIndex, line.indexOf("\"", startIndex));
                    })
                    .forEach(fileName -> {
                        int year = DataUtils.getYearFromFileName(new File(fileName));
                        allData.put(year, null);
                    });
            in.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad URL: " + directory);
        }
    }

    /**
     * Parses and returns all name data within the text file of the given year in the dataset
     * @param year          an int describing a valid year with a corresponding text file in the dataset
     * @return              an ArrayList of String arrays that contain each name data in the format:
     *                      index 0 contains name, index 1 contains gender, index 2 contains frequency
     * @throws IOException  if file is empty
     */
    private List<String[]> parseFile(int year) throws IOException {
        String filePath = directory + "yob" + year + ".txt";
        Scanner scan;
        try { // if filePath is a URL
            URL url = new URL(filePath);
            scan = new Scanner(url.openStream());
        } catch (MalformedURLException e) { // if filePath is a local path
            scan = new Scanner(new File(filePath));
        }
        List<String[]> dataForYear = new ArrayList<>();
        try {
            if (!scan.hasNextLine()) { // file has no text to begin with
                throw new IOException(filePath + " has no data. Please remove.");
            }
            while (scan.hasNextLine()) {
                dataForYear.add(scan.nextLine().split(","));
            }
        } catch (FileNotFoundException e) { // filePath is invalid
            System.out.println(filePath + " not found");
        }
        return dataForYear;
    }

    /**
     * Returns all name data of a specific year that is stored within allData data structure
     * @param year  an int describing a valid year with a corresponding text file in the dataset
     * @return      an ArrayList of String arrays of a given year that contain each name data in the format:
     *              index 0 contains name, index 1 contains gender, index 2 contains frequency
     */
    public List<String[]> getDataFromYear(int year) {
        return allData.get(year);
    }

    /**
     * getter function for startYear
     * @return startYear
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * getter function for finalYear
     * @return finalYear
     */
    public int getFinalYear() {
        return finalYear;
    }
}
