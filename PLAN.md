# Data Plan
## Jason Qiu

This is the link to the [assignment](http://www.cs.duke.edu/courses/compsci307/current/assign/01_data/):


### What is the answer to the two questions below for the data file yob1900.txt (pick a letter that makes it easy too answer)? 
1. 1900: Female - Mary; Male - John
2. Female in 1900 with Q: 3 names and 111 total babies.
### Describe the algorithm you would use to answer each one.
1. Parse through the file and find the first name with a corresponding F and the first 
name with a corresponding M. The text file is already sorted, so the first name would be the top ranked
for each gender.
2. For each gender denoted by M or F, parse through the names and check if the name starts
with the corresponding letter. Keep two variables that keep track of number of names and number of 
babies. When the letter matches, add to the two variables accordingly.
### Likely you may not even need a data structure to answer the questions below, but what about some of the questions in Part 2?
For the questions in part 2, I would utilize class objects. I would make a Person class that stores class variables: 
name, gender, and a HashMap. The HashMap would have the year as the key and the number of babies with that name as the 
value. I can parse through the files and add new Person objects or add the counts corresponding to the year to the HashMap
for each name. Therefore, for these questions, I can easily sort and filter through names and genders and get the 
corresponding count for each year of the name.
### What are some ways you could test your code to make sure it is giving the correct answer (think beyond just choosing "lucky" parameter values)?
I would create a JUnit test that can easily test different years, names, and genders. For instance, I would check to see
if the right year, name, and gender is outputted for each respective test case.
### What kinds of things make the second question harder to test?
It is harder to test because we would have to go through more common letters by hand and check it with our output. It 
would be really difficult having to count them by hand.
### What kind of errors might you expect to encounter based on the questions or in the dataset?
I would expect errors such as sorting incorrectly, getting the wrong name, parsing errors, or getting the wrong count of babies.
### How would you detect those errors and what would a reasonable "answer" be in such cases?
In these lines, I would create JUnit tests to test if the answers are reasonable. For instance, I would create tests
that would check to see if the number of babies of a certain name is the right count or if the right name is outputted
for the appropriate year/ranking. Reasonable answers would be a number of babies that isn't ridiculously high and a name
that would make sense for its popularity.
### How would your algorithm and testing need to change (if at all) to handle multiple files (i.e., a range of years)?
The algorithm would have to be able to incorporate names and counts changing among multiple years. 
I would create JUnit tests for checking names and counts of a range of years so that I can see if 
the algorithm can handle the changes among multiple years. 
