# Design Document

## Names of People
Jason Qiu - sole developer

## Design Goals
The main goals of the project is to provide an ability to parse through the data files
and provide an overall data structure that methods can extract any name data from. Next,
I want to provide the ability to answer all the questions through individual methods. I want each
method to return data as a variable or a data structure. Finally, I want to provide many helper 
methods to avoid redundancy within my code.

## High-Level Design
The high-level design of the program is having 3 classes for file processing, data analysis, and data utilities
(containing helper methods). The DataAnalysis object is created given a directory path. The DataAnalysis then creates
a ParseData object that takes the directory path and parses through all the data of the files within the directory.
The ParseData object allows for other classes to grab chunks of data of specific years in various formats. The DataAnalysis
contains all the methods for each individual question. The DataUtils class is called by all classes and contains any helper
methods for any of the classes.

## Assumptions/Decisions Made
Assumptions and decisions made to simplify my design were to assume that all the text files were listed within the directory
and they were all of the format "yobXXXX.txt" where XXXX is the year of the data. This simplified my code because
I can grab the year of the file through its filename based on the assumption that all filenames were named similarly.
Additionally, I assumed all files contained data in the format of Name, Gender, and Frequency per line. This simplified my
code because I can assume the same formatting for all data within my dataset. I assumed all URLs contained "https://", which
simplified my code in that I can just check if DIRECTORY contains "https://" and parse it as a URL. Finally, I assumed that all
data within the text files were within rank order by frequency. This simplified my code as I could easily iterate through 
the files knowing that the rankings were in the order of the names listed in the files.

## Adding New Features to Project
To add new features to the project, you can add it to DataAnalysis class at the end of the file. It should be a public
method and have appropriate parameters. It can make use of the ParseData object, which can be called to grab specific
data of specific years. Many helper methods are provided within DataUtils, which can help with things like handling input
errors. Finally, the new method should be called within the main method.