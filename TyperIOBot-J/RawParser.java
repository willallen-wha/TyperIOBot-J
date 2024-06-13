import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;

public class RawParser {

    static Pattern pattern = Pattern.compile(">.<");

    public static String parseRaw(String fileName) throws FileNotFoundException {
        //Create a buffered reader to get the info from the file
        Scanner read = new Scanner(new File(fileName));
        String parseString = "";
        //Get the input
        while(read.hasNext()) {
            parseString += read.nextLine();
        }
        //Split it into individual words
        String[] words = parseString.split("word-.");
        String resultString = "";
        for(String word: words) {
            //Get only the letters
            resultString += parseIntoWord(word);
            //Add a space to deliminate words
            resultString += " ";
        }
        resultString.trim();
        read.close();
        return resultString;
    }

    private static String parseIntoWord(String s) {
        Matcher matcher = pattern.matcher(s);
        String ret = "";
        // Check all occurrences
        while (matcher.find()) {
            ret += matcher.group().toCharArray()[1];
        }
        return ret;
    }

}
