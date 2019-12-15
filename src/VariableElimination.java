import java.util.Arrays;

public class VariableElimination {

    //methods on Factors for VARIABLE ELIMINATION algorithm

    public FactorCollection.Factor join_factors(FactorCollection.Factor A, FactorCollection.Factor B, char varToJoin) {
        if((findVarInd(A, varToJoin))==-1 || (findVarInd(B, varToJoin))==-1) //var isn't exist on the factor/s
            throw new RuntimeException("One or more of the factors are not factor of the variable you want to join");
        FactorCollection.Factor joinFactor; //********
        char[][] values = new char[sizeOfRows(A,B,varToJoin)][sizeOfCols(A,B)];

        //......
        return null;
    }

    private int sizeOfCols(FactorCollection.Factor A, FactorCollection.Factor B) {
        int counter = A.getFactorOf().size(); //for starting: the counter will be the size of A columns.
        //now we'll unite it with the variables of B Factor to create the final group of the variables of the new factor.
        for(int i=0; i<B.getFactor_values()[0].length; i++) { //the length of the first row on B's factor_values = B.factorOf.size().
            if(!(Arrays.asList(A.getFactor_values()[0]).contains(B.getFactor_values()[0][i])))
                counter ++;
        }
        return counter;
    }

    private int sizeOfRows(FactorCollection.Factor A, FactorCollection.Factor B, char var) { //check!!!!!
        int a_col = -1, b_col = -1; //the column in every factor that belongs to var
        for(int i=0; i<A.getFactor_values()[0].length; i++) { //to find the column of var in Factor A
            if (A.getFactor_values()[0][i] == var) a_col = i;
            break;
        }
        for(int j=0; j<B.getFactor_values()[0].length; j++) { //to find the column of var in Factor B
            if (A.getFactor_values()[0][j] == var) b_col = j;
            break;
        }
        int counter = 0;
        char[] values; //we'll define this array of var's values
        values = A.getFactorOf().get(findVarInd(A, var)).getShortValuesNames(); //now values point on var's shortValuesNames array.
        for(int val=0; val<values.length; val++) //Loop that goes through all possible values of var
            for(int i = 1; i < A.getFactor_values().length; i++) //Loop that goes through all the a_col column in Factor A
                if(A.getFactor_values()[i][a_col] == val)
                    for(int j = 1; j < B.getFactor_values().length; j++) //Loop that goes through all the b_col column in Factor B
                        if(B.getFactor_values()[j][b_col] == val) counter++;
        return counter;
    }

    private int findVarInd(FactorCollection.Factor f, char varToJoin) {
        for(int i=0; i<f.getFactorOf().size(); i++)
            if (f.getFactorOf().get(i).getName() == varToJoin)
                return i;
        return -1;
    }

    public void eliminate_factors(FactorCollection.Factor A, FactorCollection.Factor B){;}

    private void normalization(FactorCollection.Factor f){;}

    private void optimalOrderToJoin(FactorCollection.Factor[] factor_collection){;}
}
