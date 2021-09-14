# SpellingCorrector
A program that takes in a misspelled word and suggests the most likely correct spelling of the word. Word suggestion is determined using Damerau-Levenshtein distance.
-The program first reads in a text file of correctly spelled words, builds a Trie dictionary containing them, and suggests a correct spelling of the input word by manipulating it and searching for the new words in the current dictionary. 

From the command line, run this program with the following commands: java SpellCorrector notsobig.txt 
