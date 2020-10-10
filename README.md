data
====

This project uses data about [baby names from the US Social Security Administration](https://www.ssa.gov/oact/babynames/limits.html) to answer specific questions. 


Name: Jason Qiu

### Timeline

Start Date: 8/22/20

Finish Date: 9/7/20

Hours Spent: 25 hours

### Resources Used
Javadocs
Stack Overflow
Baeldung
JUnit Test Infected
Clean Code
Code Smells
GIT Commit Best Practices

### Running the Program

Main class: Main.java

Data files needed:
* For Main.java and Main.Test, you can use the following:
    * ssa_complete/
    * ssa_2000s/
    * https://www2.cs.duke.edu/courses/fall20/compsci307d/assign/01_data/data/ssa_complete/

In order to run the program, you can change the configuration at the top of Main.java.
You can set the directory of your files by commenting and uncommenting or writing in a new directory 
into the DIRECTORY constant.

Key/Mouse inputs: N/A

Cheat keys: N/A

Known Bugs: N/A

Extra credit: N/A


### Notes/Assumptions
Files to test project: MainTest.java; test data found in ssa_test/

Expected Errors:
* Invalid Data Sources:
    * Non-existent filename - handled by FileNotFoundException
    * Non-existent URL - handled by IllegalArgumentException
    * Blank file - handled by IOException
* Invalid Years (handled by DataUtils.handleYearErrors()):
    * Empty range of years - handled by IOException
    * Range is not in dataset - handled by IllegalArgumentException
    * Nonsensical years - handled by IllegalArgumentException
* Names Do Not Match Case:
    * Empty name - handled by IllegalArgumentException
    * Name with bad casing conventions - converts to name with first letter capitalized
* Gender Does Not Match:
    * handled by IllegalArgumentException

Information:
The main method creates a DataAnalysis object, which takes in the appropriate directory. The object then creates and stores a 
ParseData object, which will contain the appropriate data structure. The ParseData object parses through
all the files in the directory and appends the data to a HashMap. The HashMap is organized with year as the key and 
an ArrayList of String arrays as the value. The ArrayList of String arrays represents all the lines of the corresponding
year's text file delimited into a String array of name, gender, and frequency. 

The main method then runs all the methods to answer each question. These methods are contained in 
DataAnalysis.java and are called through the DataAnalysis object. Many helper methods are found in DataUtils.java
and are called frequently by methods within DataAnalysis.java and ParseData.java.

Each method returns a data structure with the corresponding answers and the main method prints the results in a readable format.

Assumptions/Simplifications:
* Assume all URLs will have https:// in URL and that the filenames can be found
in href attributes
* Assume all text files are formatted the same way with each line formatted as Name, Gender, Frequency
* Assume gender is always represented by "M" or "F" for male and female, respectively
* Assume all names are sorted in ranked order
* Assume all files are .txt files
* Assume each file is named as yob + the year of the data set (ex. "yob2018")
* Assume user inputs uppercase Strings for gender

Extra features: N/A

### Impressions
I believe that this was too much work for a project in the timespan. I believe it is unnecessary asking 15 different questions
because it reduces quality of the work, since more time is spent implementing new methods as opposed to refactoring and simplifying
already existing code. It would be better to have half as many questions, so that we can spend more time improving on 
the readability of our code. Often, some questions felt forced and inconsequential with a heavier focus on quantity over quality.
