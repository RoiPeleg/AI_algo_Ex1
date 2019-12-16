import java.util.Arrays;

public class VariableElimination {

    //methods on Factors for VARIABLE ELIMINATION algorithm

    public FactorCollection.Factor join_factors(FactorCollection.Factor A, FactorCollection.Factor B, char varToJoin) {
        if((findVarInd(A, varToJoin))==-1 || (findVarInd(B, varToJoin))==-1) //var isn't exist on the factor/s
            throw new RuntimeException("One or more of the factors are not factor of the variable you want to join");
        FactorCollection.Factor joinFactor; //= new FactorCollection.Factor();
        char[][] values = new char[sizeOfRows(A,B,varToJoin)][sizeOfCols(A,B)];
        double[] prob = new double[values.length - 1];
        for(int i=0; i<A.getFactor_values()[0].length; i++) {
            if(indexOfVal(B.getFactor_values()[0], A.getFactor_values()[0][i]) != -1) { //so we need to multiply the values to set it on the new factor
                for(int j=0; j<B.getFactor_values().length; j++) {

                }
            }
        }
        //joinFactor.setFactor_values(values);
        //joinFactor.setFactor_prob(prob);
        //for(int i=0; i<values[0].length; i++) //init factorOf!!!!

        return null;
    }

    private int sizeOfCols(FactorCollection.Factor A, FactorCollection.Factor B) {
        int counter = A.getFactorOf().size(); //for starting: the counter will be the size of A columns.
        //now we'll unite it with the variables of B Factor to create the final group of the variables of the new factor.
        for(int i=0; i<B.getFactor_values()[0].length; i++) { //the length of the first row on B's factor_values = B.factorOf.size().
            if(indexOfVal(A.getFactor_values()[0], B.getFactor_values()[0][i]) == -1)
                counter ++;
        }
        return counter;
    }

    private int sizeOfRows(FactorCollection.Factor A, FactorCollection.Factor B, char var) { //check!!!!!
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

    private int findVarInd(FactorCollection.Factor f, char varToJoin) {
        for(int i=0; i<f.getFactorOf().size(); i++)
            if (f.getFactorOf().get(i).getName() == varToJoin)
                return i;
        return -1;
    }

    private int indexOfVal(char[] arr, char c) {
        for(int i=0; i<arr.length; i++)
            if(arr[i] == c) return i;
        return -1;
    }

    public void eliminate_factors(FactorCollection.Factor A, FactorCollection.Factor B){;}

    private void normalization(FactorCollection.Factor f){;}

    private void optimalOrderToJoin(FactorCollection.Factor[] factor_collection){;}
}
