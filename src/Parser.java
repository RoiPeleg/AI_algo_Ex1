import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Parses input txt to create it's matching graph
 */
public class Parser {
    /**
     * gets the input file and give constructors the matching values
     * @param input input file path
     * @throws Exception
     */
    public static void getInput(String input) throws Exception {
        FileReader InputFile = new FileReader(input);
        BufferedReader reader = new BufferedReader(InputFile);
        String line;
        ArrayList<String> content = new ArrayList<String>();//all lines without blanks

        while ((line = reader.readLine()) != null) {
            //if the line we're on contains the text we don't want to add, skip it
            if (line.length() ==0) {
                continue;
            }
            //if we get here, we assume that we want the text, so add it
            content.add(line);
        }

    }

    public static void main(String[] args) {
        try{
            getInput("C:\\Users\\user\\Documents\\GitHub\\AI_algo\\src\\input.txt");
        }catch (Exception e){System.out.println(e.getMessage());}
    }
}
