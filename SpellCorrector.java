package spell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {

    public SpellCorrector() throws FileNotFoundException {
    } //Implements ISpellCorrector Interface

    public static void main(String[] args) throws IOException {

        String dictionaryFileName = args[0];
        String inputWord = args[1];

        SpellCorrector corrector = new SpellCorrector();

        corrector.useDictionary(dictionaryFileName);

        String suggestion = corrector.suggestSimilarWord(inputWord);
        if (suggestion == null) {
            suggestion = "No similar word found";
        }

        System.out.println("Suggestion is: " + suggestion);
    }

    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFileName File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(String dictionaryFileName) throws FileNotFoundException {
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String str = scanner.next();
            dictionary.add(str);
        }
        scanner.close();
    }

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     * @param inputWord
     * @return The suggestion or null if there is no similar word in the dictionary
     */
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();

        //Return the input string if it exists in the trie
        if (dictionary.find(inputWord) != null){
            return inputWord;
        }

        //If the input word is not found in the dictionary, build and add ed1 strings to ed1Set
        SortedSet<String> ed1Set = new TreeSet<String>();
        SortedSet<String> ed1FoundSet = new TreeSet<String>();
        //Build ed1Set
        //Deletions
        for (int i = 0; i < inputWord.length(); i++){
            StringBuilder sb = new StringBuilder(inputWord);
            sb.deleteCharAt(i);
            String resultString = sb.toString();
            ed1Set.add(resultString);
        }
        //Transpositions
        for (int i = 0; i < (inputWord.length()-1); i++){
            StringBuilder sb = new StringBuilder(inputWord);
            char firstChar = sb.charAt(i);
            char secondChar = sb.charAt(i+1);
            sb.setCharAt(i,secondChar);
            sb.setCharAt(i+1,firstChar);
            String resultString = sb.toString();
            ed1Set.add(resultString);
        }
        //Alterations
        for (int i = 0; i < inputWord.length(); i++){
            for (int j = 0; j < 26; j++){
                StringBuilder sb = new StringBuilder(inputWord);
                sb.setCharAt(i, (char)('a'+j) );
                String resultString = sb.toString();
                ed1Set.add(resultString);
            }
        }
        //Insertions
        for (int i = 0; i < (inputWord.length()+1); i++){
            for (int j = 0; j < 26; j++){
                StringBuilder sb = new StringBuilder(inputWord);
                sb.insert(i, (char)('a'+j) );
                String resultString = sb.toString();
                ed1Set.add(resultString);
            }
        }

        //Add ed1 found strings to ed1FoundSet
        //Find highest count among ed1 found strings
        int highestCount1 = 0;
        for (String str: ed1Set) {
            if (dictionary.find(str) != null) {
                ed1FoundSet.add(str);
                int stringCount1 = dictionary.find(str).getValue();
                if (stringCount1 > highestCount1) {
                    highestCount1 = stringCount1;
                }
            }
        }

        //If ed1FoundSet is not empty, return ed1 string with the highest count and is first alpha
        if (!ed1FoundSet.isEmpty()){
            SortedSet<String> topCounts1 = new TreeSet<String>();
            for (String str : ed1FoundSet){
                if (dictionary.find(str).getValue() == highestCount1){
                    topCounts1.add(str);
                }
            }
            return topCounts1.first();
        }

        //If no ed1 found strings, build ed2Set by editing all strings in ed1FoundSet
        SortedSet<String> ed2Set = new TreeSet<String>();
        SortedSet<String> ed2FoundSet = new TreeSet<String>();
        //Build ed2Set
        for (String str : ed1Set) {
            //Deletions
            for (int i = 0; i < str.length(); i++) {
                StringBuilder sb = new StringBuilder(str);
                sb.deleteCharAt(i);
                String resultString = sb.toString();
                ed2Set.add(resultString);
            }
            //Transpositions
            for (int i = 0; i < (str.length()-1); i++){
                StringBuilder sb = new StringBuilder(str);
                char firstChar = sb.charAt(i);
                char secondChar = sb.charAt(i+1);
                sb.setCharAt(i,secondChar);
                sb.setCharAt(i+1,firstChar);
                String resultString = sb.toString();
                ed2Set.add(resultString);
            }
            //Alterations
            for (int i = 0; i < str.length(); i++){
                for (int j = 0; j < 26; j++){
                    StringBuilder sb = new StringBuilder(str);
                    sb.setCharAt(i, (char)('a'+j) );
                    String resultString = sb.toString();
                    ed2Set.add(resultString);
                }
            }
            //Insertions
            for (int i = 0; i < (str.length()+1); i++){
                for (int j = 0; j < 26; j++){
                    StringBuilder sb = new StringBuilder(str);
                    sb.insert(i, (char)('a'+j) );
                    String resultString = sb.toString();
                    ed2Set.add(resultString);
                }
            }
        }

        //Add ed2 found strings to ed2FoundSet
        //Find highest count among ed2 found strings
        int highestCount2 = 0;
        for (String str: ed2Set){
            if (dictionary.find(str) != null){
                ed2FoundSet.add(str);
                int stringCount = dictionary.find(str).getValue();
                if (stringCount > highestCount2){
                    highestCount2 = stringCount;
                }
            }
        }

        //Take strings with highest count and add them to a top counts set
        //Return the first alpha string if there are multiple top count strings
        if (!ed2FoundSet.isEmpty()){
            SortedSet<String> topCounts2 = new TreeSet<String>();
            for (String str : ed2FoundSet){
                if (dictionary.find(str).getValue() == highestCount2){
                    topCounts2.add(str);
                }
            }
            return topCounts2.first();
        }
        //Return null if no ed1 or ed2 strings exist, meaning no similar words can be suggested
        return null;
    }

    //Data Members
    private Trie dictionary = new Trie();
}