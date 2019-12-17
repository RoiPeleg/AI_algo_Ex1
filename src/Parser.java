//import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

import java.io.*;
//import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    public static NodeCollection toConstruct() { //should gets a sb input
        try {
            StringBuilder sb = getInput("src/input.txt");
            String [] s = sb.toString().split("Var");
            if(!s[0].contains("Network")) throw new RuntimeException("The input is not representing a Network!");
            NodeCollection NC1 = new NodeCollection(s);
            return NC1; //The NodeCollection actually represents a bayesian network
            /*
            char[][]evidence = {{'J','M'},
                    {'t','t'}};
            FactorCollection FC1 = new FactorCollection(NC1, evidence);
            //for(int i=0; i< FC1.getSize(); i++) FC1.getFactor_collection().get(i).visualPrint();
            VariableElimination ve = new VariableElimination();
            ve.join_factors(FC1.getFactor_collection().get(0),FC1.getFactor_collection().get(2),'B');
            //NodeCollection NC2 = new NodeCollection(s2);
             */
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static void toOutput (ArrayList<String>ls)throws IOException {
        File file = new File("output.txt");
        if (!file.canWrite()) throw new IOException("can not be write to");
        if (file.exists()) file.delete();
        FileWriter fileWriter = new FileWriter(file, true);
        for (int i=0;i<ls.size();i++)
            fileWriter.write(ls.get(i) + "\n");
        fileWriter.close();
    }
}
