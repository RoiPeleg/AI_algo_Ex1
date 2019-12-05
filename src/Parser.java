import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
/**
 * Parses input txt to create it's matching graph
 */
public class Parser {
    /**
     * gets the input file and sorts it
     * @param input input file path
     * @throws Exception
     */
    public static StringBuilder getInput(String input) throws Exception {
        FileReader InputFile = new FileReader(input);
        BufferedReader reader = new BufferedReader(InputFile);
        StringBuilder contents = new StringBuilder();
        String line;
        while(reader.ready()) {
            line = reader.readLine();
            if (line.length() ==0) {
                continue;
            }
            contents.append(line+"\n");
        }
        reader.close();
        return contents;
    }

    public void toConstruct() {
        try {
            StringBuilder sb = getInput("C:\\Users\\user\\IdeaProjects\\AI_algorithms\\src\\input.txt"); //roi : ("C:\\Users\\user\\Documents\\GitHub\\AI_algo\\src\\input.txt")
            //StringBuilder sb_2 = getInput("C:\\Users\\user\\Documents\\GitHub\\AI_algo\\src\\input2.txt");
            String [] s = sb.toString().split("Var");
            //String [] s2 = sb.toString().split("Var");
            NodeCollection NC1 = new NodeCollection(s);
            //NodeCollection NC2 = new NodeCollection(s2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*public static void main(String[] args) {
        toConstruct();
    }*/
}
