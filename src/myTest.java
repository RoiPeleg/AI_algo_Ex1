import java.util.Arrays;

public class myTest {

    public static void main(String[] args) {

        /*Parser p = new Parser();
        NodeCollection NC = p.toConstruct();
        char[][]evidence = {{'J','M'},
                {'t','t'}};
        FactorCollection FC = new FactorCollection(NC, evidence);
        //for(int i=0; i< FC.getSize(); i++) FC.getFactor_collection().get(i).visualPrint();
        VariableElimination ve = new VariableElimination();
        ve.join_factors(FC, FC.getFactor_collection().get(3), FC.getFactor_collection().get(4), 'A');
        ve.join_factors(FC, FC.getFactor_collection().get(2), FC.getFactor_collection().get(3), 'A');
        for(int i=0; i< FC.getSize(); i++) FC.getFactor_collection().get(i).visualPrint();*/

        char[][] values = {{'E', 'B', 'A'},
                {'T', 'T', 'T'},
                {'T', 'F', 'T'},
                {'F', 'T', 'T'},
                {'F', 'F', 'T'},
                {'T', 'T', 'F'},
                {'T', 'F', 'F'},
                {'F', 'T', 'F'},
                {'F', 'F', 'F'}};
        double[] prob = {0.5985, 0.1827, 0.5922, 0.00063, 0.000025, 0.000355, 0.00003, 0.0004995};

        char[][]evidence = {{'J','M'},
                {'t','t'}};
        NodeCollection NC = Parser.toConstruct();

        FactorCollection FC = new FactorCollection(NC,evidence);
        FactorCollection.Factor f = FC.new Factor(values,prob);

        for(int i=0; i< FC.getSize(); i++) FC.getFactor_collection().get(i).visualPrint();

        VariableElimination.eliminate_factors(FC,NC.nodes[2],f);
        System.out.println("after eli ............................");
        for(int i=0; i< FC.getSize(); i++) FC.getFactor_collection().get(i).visualPrint();

        /*Parser p = new Parser();
        try{
            StringBuilder sb = p.getInput("C:\\Users\\user\\IdeaProjects\\AI_algorithms\\src\\input.txt");
            String [] s =sb.toString().split("Var");
            method(s[3]);
            *//*for(int i=2; i<s.length;i++)
            {
                if(s[i].contains("Network"))continue;
                if(s[i].contains("Queries"))continue;
                //System.out.println(s[i] + "************");
            }*//*
         *//*int indx = sb.indexOf("Parents:")+9;
            System.out.println(indx);
            String [] as = sb.substring(indx).split("CPT");
            for(int i=0; i<as.length; i++) {
                System.out.println(as[i]);
            }*//*
            //System.out.println(sb.indexOf("CPT"));
        }catch (Exception e){System.out.println(e.getMessage());}*/

        /*String s = "Parents: A,B,C";
        String[] parents = s.substring(8).split(",");
        for(int i=0; i<parents.length; i++) {
            System.out.println(parents[i]);
        }*/

    }

    /*private static void method(String block) {
        String[] block_rows = block.split("\n"); //split each row of string
        System.out.println("block_rows values: "+block_rows[1]);
        String values = "";
        for (int i = 8; i < block_rows[1].length(); i++) {
            values += block_rows[1].charAt(i);
        }
        String[] arr = values.split(",");
            for (int j = 0; j < arr.length; j++) {
                System.out.println(arr[j]);
            }
    }*/

}
