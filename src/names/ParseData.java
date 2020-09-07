package names;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ParseData {
    private static final int NAME_INDEX = 0;
    private static final int GENDER_INDEX = 1;

    private final String directory;
    private final int startYear;
    private final int finalYear;
    private final Map<Integer, List<String[]>> data;

    //TODO: NOTE THAT THE URL HAS TO BEGIN WITH HTTPS://
    public ParseData(String directory) {
        this.directory = directory;
        this.data = new HashMap<>();
        if (directory.contains("https://")) {
            handleURL();
            this.startYear = Collections.min(data.keySet());
            this.finalYear = Collections.max(data.keySet());
        } else {
            this.startYear = DataUtils.getOldestYear(directory);
            this.finalYear = DataUtils.getRecentYear(directory);
        }
        for (int year = startYear; year <= finalYear; year++) {
            try {
                data.put(year, parseFile(year));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<Integer, String> getRankTable(int year, String gender) {
        int rank = 1;
        Map<Integer, String> rankTable = new HashMap<>();
        for (String[] nameArray : data.get(year)) {
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
        for (String[] nameArray : data.get(year)) {
            if (nameArray[GENDER_INDEX].equals(gender)) {
                rankTableInverse.put(nameArray[NAME_INDEX], rank);
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
                        data.put(year, null);
                    });
            in.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad URL: " + directory);
        }
    }

    private List<String[]> parseFile(int year) throws IOException {
        String filePath = directory + "yob" + year + ".txt";
        URL url = new URL(filePath);
        if (!directory.contains("https://")) {
            url = new File(filePath).toURI().toURL();
        }
        List<String[]> dataForYear = new ArrayList<>();
        try {
            Scanner scan = new Scanner(url.openStream());
            if (!scan.hasNextLine()) {
                throw new IllegalArgumentException(filePath + " has no data. Please remove.");
            }
            while (scan.hasNextLine()) {
                dataForYear.add(scan.nextLine().split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " not found");
        }
        return dataForYear;
    }
}
