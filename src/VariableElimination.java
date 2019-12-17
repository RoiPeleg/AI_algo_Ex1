import java.util.ArrayList;
import java.util.Arrays;

public class VariableElimination {

    //methods on Factors for VARIABLE ELIMINATION algorithm

    public void join_factors(FactorCollection FC, FactorCollection.Factor A, FactorCollection.Factor B, char varToJoin) {
        if((findVarInd(A, varToJoin))==-1 || (findVarInd(B, varToJoin))==-1) //var isn't exist on the factor/s
            throw new RuntimeException("One or more of the factors are not factor of the variable you want to join");
        char[][] values = new char[sizeOfRows(A,B,varToJoin)][sizeOfCols(A,B)];
        double[] prob = new double[values.length - 1];
        int valuesColCount = 0;
        int valuesRowCount = 1;
        for(int i=0; i<A.getFactor_values()[0].length; i++) {
            int indexInB = indexOfVal(B.getFactor_values()[0], A.getFactor_values()[0][i]);
            if(indexInB != -1) { //so we need to multiply the values to set it on the new factor
                values[0][valuesColCount] = A.getFactor_values()[0][i];
                for(int j=1; j<A.getFactor_values().length; j++) //runs on all A's rows
                    for (int k = 1; k < B.getFactor_values().length; k++) //runs on all B's rows
                        if (B.getFactor_values()[k][indexInB] == A.getFactor_values()[j][i]) {
                            values[valuesRowCount][valuesColCount] = B.getFactor_values()[k][indexInB];
                            prob[valuesRowCount - 1] = B.getFactor_prob()[k - 1] * A.getFactor_prob()[j - 1];
                            valuesRowCount++;
                            if (valuesRowCount == values.length) {
                                valuesRowCount = 0;
                                valuesColCount++;
                            }
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

    public static void eliminate_factors(NodeCollection.Node toRemove, FactorCollection.Factor factor){
        char c = toRemove.getName();
        int numberOfVals = toRemove.getValues().length;//T/F etc..
        char[][] vals = factor.getFactor_values();
        double[] given = factor.getFactor_prob();
        int size = (int) Math.pow(numberOfVals,vals.length);//size to be removed
        char[][] chars = new char[vals.length-1][size];
        double [] sum = new double[size];
        int col=0;//col of to remove
        for(int i=0;i<chars.length;i++)//copies first row
        {
            for(int j =0;j<chars[0].length;i++)
                chars[i][j] = vals[i][j];
        }
        for(int i=0;i<vals[0].length;i++)//finds column of toremove
        {
            if(vals[0][i]==c)col=i;
        }
        for(int i =0;i<vals.length/numberOfVals;i++)//sums up needed probs
        {
            sum[i]= given[i] + given[i+size];
        }
        factor.setFactor_values(chars);
        factor.setFactor_prob(sum);
    }

    private static void normalization(FactorCollection.Factor f){;}

    private static void optimalOrderToJoin(FactorCollection.Factor[] factor_collection){;}
}
