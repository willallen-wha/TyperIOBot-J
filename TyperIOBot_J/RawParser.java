package TyperIOBot_J;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;

/**
 * Static class used to parse HTML ripped from Typer.io. Has only one public
 * function: parseRaw, which takes the contents of the raw text file and uses
 * regex to extract just the word divs of the HTML, then pass that into a
 * private function to extract the actual word.
 */
public class RawParser {

    // Regex patter to seperate different sections of HTML. Each letter is in the
    // format <span id="letter-X" class="gameboard_base"...>c</span> and all other
    // content is inside <>, so regexing for just letters between the > and <
    // characters finds only letters.
    static Pattern pattern = Pattern.compile(">.<");

    /**
     * Static function to take the raw HTML of the Gameboard div from Typer.io
     * and parse it into just a string of characters to type. Requires the text
     * file to be full and contain only the contents of the Gameboard div.
     * 
     * @param fileName - Name or filepath of the raw text file from which to read.
     * @return A string containing the text as it appears to be typed on Typer.io
     * without any HTML.
     * @throws FileNotFoundException If the text file specified is not found.
     */
    public static String parseRaw(String fileName) throws FileNotFoundException {
        // Create a buffered reader to get the info from the file
        Scanner read = new Scanner(new File(fileName));
        String parseString = "";
        // Read the whole file start to finish
        while(read.hasNext()) {
            parseString += read.nextLine();
        }
        // Split it into individual words by splitting on the "word-#" tag
        String[] words = parseString.split("word-.");
        String resultString = "";
        // Each found word section needs parsed into a series of letters
        for(String word: words) {
            //Get only the letters
            resultString += parseIntoWord(word);
            //Add a space to deliminate words
            resultString += " ";
        }
        // Cut trailing spaces
        resultString.trim();
        read.close();
        // Return the parsed string to type
        return resultString;
    }

    /**
     * Takes just a Gameboard_word div and converts it into the singular word
     * represented inside the div by finding and concatenating every instance
     * of >.< with '.' being any singular character.
     * 
     * @param s - String that starts contains the entire Gameboard_word div
     * HTML content.
     * @return The word constructed by concatenating every character in the
     * Gameboard_word div.
     */
    private static String parseIntoWord(String s) {
        // Match every instance 
        Matcher matcher = pattern.matcher(s);
        // Return string
        String ret = "";
        // Check all occurrences - .find() returns true if it finds another
        // instance of >.< with '.' being any character.
        while (matcher.find()) {
            // matcher.group() is the character set found by matcher.find()
            // and is thus just >.< with the '.' being next letter of the word.
            ret += matcher.group().toCharArray()[1];
        }
        // No more letters found, therefore the word is complete and returned.
        return ret;
    }

}
