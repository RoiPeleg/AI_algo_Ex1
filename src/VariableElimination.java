import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class VariableElimination {

    private int numOfMul = 0; //count the number of multiplies
    private int numOfAdd = 0; //count the number of plus

    //methods on Factors for VARIABLE ELIMINATION algorithm
    public static void join_factors(FactorCollection FC, FactorCollection.Factor A, FactorCollection.Factor B, char varToJoin) {
        if((findVarInd(A, varToJoin))==-1 || (findVarInd(B, varToJoin))==-1) //var isn't exist on the factor/s
            throw new RuntimeException("One or more of the factors are not factor of the variable you want to join");
        char[][] values = new char[sizeOfRows(A,B,varToJoin)][sizeOfCols(A,B)];
        double[] prob = new double[values.length - 1];
        int valuesColCount = 0;
        int valuesRowCount = 1;
        for(int i=0; i<A.getFactor_values()[0].length; i++) {
            //int indexInB = indexOfVal(B.getFactor_values()[0], A.getFactor_values()[0][i]);
            int indexInB = findVarInd(B, varToJoin); //(indexInB != -1) because we already checked it!
            values[0][valuesColCount] = A.getFactor_values()[0][i];
            if(A.getFactor_values()[0][i] == varToJoin) { //so this var exists on B --> we need to multiply the values to set it on the new factor.
                for(int j=1; j<A.getFactor_values().length; j++) //runs on all A's rows
                    for (int k = 1; k < B.getFactor_values().length; k++) //runs on all B's rows
                        if (B.getFactor_values()[k][indexInB] == A.getFactor_values()[j][i]) {
                            values[valuesRowCount][valuesColCount] = B.getFactor_values()[k][indexInB];
                            prob[valuesRowCount - 1] = B.getFactor_prob()[k - 1] * A.getFactor_prob()[j - 1];
                            valuesRowCount++;
                            if (valuesRowCount == values.length) {
                                valuesRowCount = 1; //0;
                                valuesColCount++;
                            }
                        }
            } else {
                while(valuesRowCount < values.length) {
                    int ARowCount = 1;
                    while(ARowCount < A.getFactor_values().length) {
                        values[valuesRowCount][valuesColCount] = A.getFactor_values()[ARowCount][i];
                        ARowCount ++;
                        valuesRowCount ++;
                    }
                }
                valuesRowCount = 1;
                valuesColCount ++;
            }
        }
        if(valuesColCount != values[0].length) { //so there are variables on B that are not exist on A
            for (int i = 0; i < B.getFactor_values()[0].length; i++) {
                if (indexOfVal(A.getFactor_values()[0], B.getFactor_values()[0][i]) == -1) { //not exist in A
                    while(valuesRowCount < values.length) {
                        int BRowCount = 0;
                        while(BRowCount < B.getFactor_values().length) {
                            values[valuesRowCount][valuesColCount] = B.getFactor_values()[BRowCount][i];
                            BRowCount ++;
                            valuesRowCount ++;
                        }
                    }
                    valuesRowCount = 1;
                    valuesColCount ++;
                }
            }
        }
        A.setFactor_values(values);
        A.setFactor_prob(prob);
        A.setFactorOf(values[0]);
        FC.removeFactor(B);
    }

    private static int sizeOfCols(FactorCollection.Factor A, FactorCollection.Factor B) {
        int counter = A.getFactorOf().size(); //for starting: the counter will be the size of A columns.
        //now we'll unite it with the variables of B Factor to create the final group of the variables of the new factor.
        for(int i=0; i<B.getFactor_values()[0].length; i++) { //the length of the first row on B's factor_values = B.factorOf.size().
            if(indexOfVal(A.getFactor_values()[0], B.getFactor_values()[0][i]) == -1)
                counter ++;
        }
        return counter;
    }

    private static int sizeOfRows(FactorCollection.Factor A, FactorCollection.Factor B, char var) { //check!!!!!
        int a_col = -1, b_col = -1; //the column in each factor that belongs to var
        for(int i=0; i<A.getFactor_values()[0].length; i++) { //to find the column of var in Factor A
            if (A.getFactor_values()[0][i] == var) {
                a_col = i;
                break;
            }
        }
        for(int j=0; j<B.getFactor_values()[0].length; j++) { //to find the column of var in Factor B
            if (B.getFactor_values()[0][j] == var) {
                b_col = j;
                break;
            }
        }
        int counter = 0; //counter for size of rows
        char[] values; //we'll define this array of var's values
        values = A.getFactorOf().get(findVarInd(A, var)).getShortValuesNames(); //now values point on var's shortValuesNames array.
        for(int val=0; val<values.length; val++) //Loop that goes through all possible values of var
            for(int i = 1; i < A.getFactor_values().length; i++) //Loop that goes through all the a_col column in Factor A
                if(A.getFactor_values()[i][a_col] == values[val])
                    for(int j = 1; j < B.getFactor_values().length; j++) //Loop that goes through all the b_col column in Factor B
                        if(B.getFactor_values()[j][b_col] == values[val]) counter++;
        return counter + 1; //+1 because there is also the first row (index 0) of variables' names.
    }

    private static int findVarInd(FactorCollection.Factor f, char varToJoin) {
        for(int i=0; i<f.getFactorOf().size(); i++)
            if (f.getFactorOf().get(i).getName() == varToJoin)
                return i;
        return -1;
    }

    private static int indexOfVal(char[] arr, char c) {
        for(int i=0; i<arr.length; i++)
            if(arr[i] == c) return i;
        return -1;
    }

    public static void eliminate_factors(FactorCollection FC, NodeCollection.Node toRemove, FactorCollection.Factor factor) {
        char c = toRemove.getName();
        int numberOfVals = toRemove.getValues().length;//T/F etc..
        char[][] vals = factor.getFactor_values();
        double[] given = factor.getFactor_prob();
        int size = (int) Math.pow(vals[0].length-1,numberOfVals)+1;//size to be removed
        char[][] chars = new char[size][vals[0].length-1];
        double [] sum = new double[size-1];
        int col = 0;//col of toremove
        for (int i = 0; i < vals[0].length; i++)//finds column of toremove
        {
            if (vals[0][i] == c) col = i;
        }
        int k = 0, m = 0;
        for(int i=0;i<chars[0].length;i++)
        {
            if (col == i)
                continue;
            chars[k++][m] = vals[0][i];
            for (int j = 1; j < vals.length; j = j + numberOfVals) {
                chars[k++][m] = vals[j][i];
            }
            k = 0;
            m++;
        }
        for (int i = 1; i < sum.length; i++)//sums up needed probs
        {
            double s = 0;
            for (int j = 0; j < numberOfVals; j++) {
                s += given[i + j];
            }
            sum[i] = s;
        }
        factor.setFactor_values(chars);
        factor.setFactor_prob(sum);
        factor.setFactorOf(chars[0]);
    }

    public static void normalization(FactorCollection.Factor f){;}

    public static int[] optimalOrderToJoin(ArrayList<FactorCollection.Factor> H_factors){
        int[] t = new int[2];
        int max = H_factors.get(0).getFactor_prob().length * H_factors.get(1).getFactor_prob().length;
        for (int i = 0; i < H_factors.size(); i++) {
            for (int j = 0; j < H_factors.size(); j++) {
                if (j == i) continue;
                int x = H_factors.get(i).getFactor_prob().length * H_factors.get(j).getFactor_prob().length;
                if (max < x) {
                    max = x;
                    t[0] = i;
                    t[1] = j;
                }
            }
        }
        return t;
       //always 2 rows:
        // 0 = the index of the first factor to join
        // 1 = the index of second factor
    };

    //main function: Variable Elimination
    public static String variableElimination(NodeCollection NC, String query) {

        //set query
        if (!query.contains("(")) return "yes";
        String[] s = query.split("\\)")[0].replace("P", "").replace("(", "").split("\\|");
        String Q = s[0];
        String[] evidenceStr = s[1].split("\\,");
        char[][] evidence = new char[2][evidenceStr.length];
        for(int i=0; i<evidenceStr.length; i++) {
            evidence[0][i] = evidenceStr[i].charAt(0);
            evidence[1][i] = evidenceStr[i].toUpperCase().charAt(2);
        }
        String[] givenOrder = query.split("\\)")[1].replaceFirst("\\,", "").split("-");
        char[] gOrder = new char[givenOrder.length];
        for(int i=0;i<givenOrder.length;i++)gOrder[i]=givenOrder[i].charAt(0);

        //init factors
        FactorCollection FC = new FactorCollection(NC,evidence);
        for(char hidden : gOrder) {

            //find all the factors that are factors of the hidden variable
            ArrayList<FactorCollection.Factor> H_factors = new ArrayList<FactorCollection.Factor>();
            for(FactorCollection.Factor factor : FC.getFactor_collection())
                if (factor.getFactorOf().contains(NC.convertToItsNode(hidden)))
                    H_factors.add(factor);

            //choose which 2 factors to join every time (until there is only one factor in the list)
            while(H_factors.size() > 1) {
                int[] index_in_H_factors = optimalOrderToJoin(H_factors);
                int index_of_first_factor = FC.getFactor_collection().indexOf(H_factors.get(index_in_H_factors[0]));
                int index_of_second_factor = FC.getFactor_collection().indexOf(H_factors.get(index_in_H_factors[1]));
                join_factors(FC, FC.getFactor_collection().get(index_of_first_factor), FC.getFactor_collection().get(index_of_second_factor), hidden);

                //remove the factors from H_factors
                H_factors.remove(index_in_H_factors[0]);
                H_factors.remove(index_in_H_factors[1]);
            }

            //eliminate hidden
            int index_in_FC = FC.getFactor_collection().indexOf(H_factors.get(0)); //there is only one factor there
            eliminate_factors(FC, NC.convertToItsNode(hidden), FC.getFactor_collection().get(index_in_FC));
        }

        //join all remains factors and normalize
        return "";
    }
}
