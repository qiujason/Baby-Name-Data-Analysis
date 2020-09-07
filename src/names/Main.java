package names;


import java.util.*;


public class Main {
    // CONFIGURATION
    private static final String DIRECTORY = "data/ssa_complete/";

    private static final String MALE = "M";
    private static final String FEMALE = "F";
    //TODO: global variables for namearray indices
    //TODO: make parsefile class. i can create a parsefile object that reads the data
    
    //TODO: add what it takes in, returns, example (javadoc)
    //TODO: clarify in javadocs comment what a line would look like

    public static void main (String[] args) {
        DataAnalysis analysis = new DataAnalysis(DIRECTORY);

        System.out.println("Test Implementation #1");
        String[] topRanked = analysis.findTopRankedInYear(1950);
        for (String name : topRanked) {
            System.out.println(name);
        }

        System.out.println("\nTest Implementation #2");
        int[] counts = analysis.findCountByGenderLetterYear(1950, FEMALE, "B");
        System.out.println(counts[0] + " different names");
        System.out.println(counts[1] + " total babies");

        System.out.println("\nBasic Implementation #1");
        Map<Integer, Integer> rankings = analysis.findRankingsByNameGender("Jack", MALE);
        rankings.forEach((year, ranking) -> System.out.println(year + " - " + ranking));

        System.out.println("\nBasic Implementation #2");
        System.out.println(analysis.findSameRankInRecentYear("Frank", MALE, 1880));

        System.out.println("\nBasic Implementation #3");
        List<String> mostPopular = analysis.findMostPopularInRange(MALE, 1930, 1940);
        mostPopular.forEach(System.out::println);

        System.out.println("\nBasic Implementation #4");
        String[] mostPopularLetterNames = analysis.findMostPopularLetterGirls(1887, 2003);
        System.out.println("Most Popular Letter for Females: " + mostPopularLetterNames[0]);
        for (int i = 1; i < mostPopularLetterNames.length; i++) {
            System.out.print(mostPopularLetterNames[i] + ", ");
        }

        System.out.println("\n\nComplete Implementation #1");
        Map<Integer, Integer> rankingsInRange = analysis.findRankingsByNameGenderInRange(1970, 2000, "Eric", MALE);
        rankingsInRange.forEach((year, ranking) -> System.out.println(year + " - " + ranking));

        System.out.println("\nComplete Implementation #2");
        Map<String, Integer> rankingDifferences = analysis.findRankingDifferenceFirstLastYears(1900, 2007, "Jess", FEMALE);
        if (rankingDifferences == null) {
            System.out.println("Name not found in one of the years");
        } else {
            rankingDifferences.forEach((k, ranking) -> System.out.println(k + ": " + ranking));
        }

        System.out.println("\nComplete Implementation #3");
        System.out.println(analysis.findNameLargestDifferenceFirstLastYears(2003, 2018, MALE));

        System.out.println("\nComplete Implementation #4");
        System.out.println(analysis.findAverageRankOverRange(1900, 1999, "Eddie", MALE));

        System.out.println("\nComplete Implementation #5");
        System.out.println(analysis.findNameHighestAverageRank(1971, 1973, FEMALE));
        
        System.out.println("\nComplete Implementation #6");
        System.out.println(analysis.findAverageRankRecentYears("Ann", FEMALE, 8));

        System.out.println("\nComplete Implementation #7");
        Map<Integer, String> namesAtRank = analysis.findNamesAtRank(1980, 1985, FEMALE, 5);
        namesAtRank.forEach((year, name) -> System.out.println(year + " - " + name));

        System.out.println("\nComplete Implementation #8");
        Map<String, Integer> mostFrequent = analysis.findNamesAtRankMostOften(1987, 2000, FEMALE, 6);
        mostFrequent.forEach((name, frequency) -> System.out.println(name + ": " + frequency));
    }
}
