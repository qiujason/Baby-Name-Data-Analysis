## Lab Discussion
### Jason Qiu, Alex Lu


### Issues in Current Code

 * What pieces of code help versus obscure your understanding?
 Some pieces of code that obscure my understanding is my excessive use of many loops in one method.
 Some pieces of code that help understanding were my variable names and comments.

 * What names in the code are helpful and what makes other names less useful?
 Some names in my code that were helpful were startyear, finalyear, mostPopularLetter.
 Some names that were unhelpful were popular and set.

 * What additional methods might be helpful to make the code more readable or usable?
 I could make use of a lot more methods as a lot of my current methods are pretty long. For instance,
 one method that I could incorporate is a file that takes a filename and outputs a scanner that reads that file.
 I use this in all my methods, so having a helper method that I can call universally would be really beneficial.

 * What assumptions does this code have?
 Some assumptions my code has are that the file names all have "yob" + yearname + ".txt". Also, I assume that user
 inputs would be accurate. For example, some people may write "f" instead of "F" which would not be equivalent.

 * What comments might be helpful within the code?
 Some comments that would be helpful are for certain lines with long pieces of code. Although I try to make my code
 as readable as possible, often I have long lines that call many methods within 1 line. These confusingly long lines can
 be explained through comments.
 Additionally, I can use comments to explain my data structures.

 * WhatÂ Code SmellsÂ did you find?
 Duplicated code
 Long methods



### Refactoring Plan

 * What are the code's biggest issues?
 The code's biggest issue is long methods. I need to make better use of helper methods because my methods often
 do 5-6 different things when they should just be doing 1 thing.

 * Which issues are easy to fix and which are hard?
 Some issues that are easy to fix is refactoring my code into using more helper methods and using better variable names.
 Some hard aspects are the way I parsed through my files. I believe I can just parse through all the files once and
 use a universal data structure that I can reference in all my methods.
  
 * What are good ways to implement the changes "in place"?
Some good ways to implement the changes in place are to use built in move, rename, and refactor functions. Also,
to make sure my code still works, I should be sure to test it frequently.

### Refactoring Work

 * Issue chosen: Fix and Alternatives
Long methods: this is an important issue that is in all my methods. After discussing with my partner, we agreed
that I should split my method into smaller helper methods that encapsulate single functions. Alternatively, I could simplify
some of my code, especially in regards to complex data structures.


 * Issue chosen: Fix and Alternatives
Duplicated code associated with Data Resource Folder: instead of putting data in my file path each time, 
my partner recommended that I mark my data folder as a data resource folder. This way, I do not have to write "data/" 
each time. Another alternative to this is to set a variable to "data/" so I do not duplicate it each time.