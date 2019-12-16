//import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
//import java.util.regex.Matcher;
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
            //StringBuilder sb_2 = getInput("C:\\Users\\user\\IdeaProjects\\AI_algorithms\\src\\input2.txt");
            String [] s = sb.toString().split("Var");
            //String [] s2 = sb_2.toString().split("Var");
            if(!s[0].contains("Network")) throw new RuntimeException("The input is not representing a Network!");
            NodeCollection NC1 = new NodeCollection(s);
            char[][]evidence = {{'J','M'},
                    {'t','t'}};
            FactorCollection FC1 = new FactorCollection(NC1, evidence);
            //for(int i=0; i< FC1.getSize(); i++) FC1.getFactor_collection().get(i).visualPrint();
            VariableElimination ve = new VariableElimination();
            ve.join_factors(FC1.getFactor_collection().get(0),FC1.getFactor_collection().get(2),'B');
            //NodeCollection NC2 = new NodeCollection(s2);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
    }

    /*public static void main(String[] args) {
        toConstruct();
    }*/
}
