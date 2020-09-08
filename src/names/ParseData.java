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

    //TODO: NOTE THAT THE URL HAS TO BEGIN WITH HTTPS://
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

    private List<String[]> parseFile(int year) throws IOException {
        String filePath = directory + "yob" + year + ".txt";
        Scanner scan;
        try {
            URL url = new URL(filePath);
            scan = new Scanner(url.openStream());
        } catch (MalformedURLException e) {
            scan = new Scanner(new File(filePath));
        }
        List<String[]> dataForYear = new ArrayList<>();
        try {
            if (!scan.hasNextLine()) {
                throw new IOException(filePath + " has no data. Please remove.");
            }
            while (scan.hasNextLine()) {
                dataForYear.add(scan.nextLine().split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " not found");
        }
        return dataForYear;
    }

    public List<String[]> getDataFromYear(int year) {
        return allData.get(year);
    }

    public int getStartYear() {
        return startYear;
    }

    public int getFinalYear() {
        return finalYear;
    }
}
