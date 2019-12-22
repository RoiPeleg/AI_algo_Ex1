import java.util.ArrayList;
import java.util.Arrays;

public class myTest {

    public static void main(String[] args) {

        /*char[][] values = {{'E', 'B', 'A'},
                {'T', 'T', 'T'},
                {'T', 'F', 'T'},
                {'F', 'T', 'T'},
                {'F', 'F', 'T'},
                {'T', 'T', 'F'},
                {'T', 'F', 'F'},
                {'F', 'T', 'F'},
                {'F', 'F', 'F'}};
        double[] prob = {0.5985, 0.1827, 0.5922, 0.00063, 0.000025, 0.000355, 0.00003, 0.0004995};*/

        /*char[][]evidence = {{'J','M'},
                {'t','t'}};
        NodeCollection NC = Parser.toConstruct();
        FactorCollection FC = new FactorCollection(NC,evidence);
        VariableElimination.join_factors(FC,FC.getFactor_collection().get(3),FC.getFactor_collection().get(4),'A');
        VariableElimination.join_factors(FC,FC.getFactor_collection().get(2),FC.getFactor_collection().get(3),'A');
        VariableElimination.eliminate_factors(FC, NC.nodes[2], FC.getFactor_collection().get(2));
        for(int i=0; i< FC.getSize(); i++) FC.getFactor_collection().get(i).visualPrint();*/

        NodeCollection NC = Parser.toConstruct();
        String[] q = NC.getQueries();
        ArrayList<String>ls = new ArrayList<String>();
        /*for (int i=0;i<q.length;i++)
        {
            ls.add(VariableElimination.variableElimination(NC,q[i]));
        }*/

        ls.add(VariableElimination.variableElimination(NC,q[4]));

        for(int i=0; i<ls.size(); i++){
            System.out.println(ls.get(i));
        }


    }
}
