
import java.util.ArrayList;
import java.util.Arrays;

public class VariableElimination {

    private static int numOfMul = 0; //count the number of multiplies
    private static int numOfAdd = 0; //count the number of plus

    //methods on Factors for VARIABLE ELIMINATION algorithm
    public static void join_factors(FactorCollection FC, FactorCollection.Factor A, FactorCollection.Factor B, char varToJoin) {
        if((findVarInd(A, varToJoin))==-1 || (findVarInd(B, varToJoin))==-1) //var isn't exist on the factor/s
            throw new RuntimeException("One or more of the factors are not factor of the variable you want to join");
        char[][] values = new char[sizeOfRows(A,B,varToJoin)][sizeOfCols(A,B)];
        double[] prob = new double[values.length - 1];
        int valuesColCount = 0;
        int valuesRowCount = 1;
        FactorCollection.Factor longer, shorter;
        if(B.getFactor_values()[0].length > A.getFactor_values()[0].length) {
            longer = B;
            shorter = A;
        } else {
            longer = A;
            shorter = B;
        }
        int indexInShort = findVarInd(shorter, varToJoin); //(index != -1) because we already checked it!
        for(int i=0; i<longer.getFactor_values()[0].length; i++) {
            values[0][valuesColCount] = longer.getFactor_values()[0][i];
            if(longer.getFactor_values()[0][i] == varToJoin) { //so this var exists on B --> we need to multiply the values to set it on the new factor.
                for(int j = 1; j < longer.getFactor_values().length; j++) //runs on all longer's rows
                    for (int k = 1; k < shorter.getFactor_values().length; k++) //runs on all shorter's rows
                        if (shorter.getFactor_values()[k][indexInShort] == longer.getFactor_values()[j][i]) {
                            values[valuesRowCount][valuesColCount] = shorter.getFactor_values()[k][indexInShort];
                            prob[valuesRowCount - 1] = shorter.getFactor_prob()[k - 1] * longer.getFactor_prob()[j - 1];
                            numOfMul++;
                            valuesRowCount++;
                            if (valuesRowCount == values.length) {
                                valuesRowCount = 1;
                                valuesColCount++;
                            }
                        }
            } else {
                while(valuesRowCount < values.length) {
                    int LongRowCount = 1;
                    while (LongRowCount < longer.getFactor_values().length && valuesRowCount < values.length) {
                        values[valuesRowCount][valuesColCount] = longer.getFactor_values()[LongRowCount][i];
                        LongRowCount ++;
                        valuesRowCount ++;
                    }
                }
                valuesRowCount = 1;
                valuesColCount ++;
            }
        }
        while(valuesColCount < values[0].length) { //so there are variables in shorter factor that are not in the longer factor!
            int shortCol = 0;
            char[] var_values;
            int var_values_count = 0;
            char[] comb = new char[valuesColCount];
            char[] combToCompare = new char[comb.length];
            ArrayList<Integer> treatedRows = new ArrayList<Integer>();

            while(shortCol < shorter.getFactor_values()[0].length) {
                if(shortCol != indexInShort) {
                    values[0][valuesColCount] = shorter.getFactor_values()[0][shortCol];
                    var_values = FC.getNC().convertToItsNode(values[0][valuesColCount]).getShortValuesNames();
                    if(valuesRowCount < values.length) {
                        if(treatedRows.isEmpty() || !treatedRows.contains(valuesRowCount)) {
                            for(int i=0; i<valuesColCount; i++) comb[i] = values[valuesRowCount][i];
                            values[valuesRowCount][valuesColCount] = var_values[var_values_count];
                            var_values_count++;
                            for(int k=valuesRowCount+1; k<values.length; k++) { //goes over all values' rows
                                for(int j=0; j<valuesColCount; j++) //goes over all values' columns
                                    combToCompare[j] = values[k][j];
                                if (Arrays.equals(comb, combToCompare) && var_values_count < var_values.length) {
                                    values[k][valuesColCount] = var_values[var_values_count];
                                    var_values_count++;
                                    treatedRows.add(k);
                                }
                            }
                        }
                        valuesRowCount++;
                    }
                }
                shortCol++;
            }
            valuesColCount++;
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

    private static int sizeOfRows(FactorCollection.Factor A, FactorCollection.Factor B, char var) {
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
                    for (int j = 1; j < B.getFactor_values().length; j++) //Loop that goes through all the b_col column in Factor B
                        if (B.getFactor_values()[j][b_col] == values[val]) counter++;

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
        if(factor.getFactorOf().size() == 1) return;
        int rowSize = 1;
        ArrayList<Character> toLeave = new ArrayList<>();
        for(char var : factor.getFactor_values()[0])
            if (var != toRemove.getName()) {
                rowSize *= FC.getNC().convertToItsNode(var).getValues().length;
                toLeave.add(var);
            }
        char[][] values = new char[rowSize + 1][factor.getFactor_values()[0].length - 1];
        for(int i=0; i<toLeave.size(); i++) values[0][i] = toLeave.get(i); //init the first row of values
        double[] prob = new double[rowSize];
        char[] comb = new char[values[0].length]; //array that saves value's combination (of the variables that will be in the new factor - not toRemove).
        char[] combToCompare = new char[values[0].length];
        int comb_counter = 0;
        int valuesRowCount = 1;
        ArrayList<Integer> treatedRows = new ArrayList<Integer>(); //In order not to skip a line whose combination is already within the result factor
        for(int i=1; i<factor.getFactor_values().length; i++) {
            if(treatedRows.isEmpty() || !treatedRows.contains(i)) {
                for (int j = 0; j < factor.getFactor_values()[0].length; j++) {
                    if (factor.getFactor_values()[0][j] != toRemove.getName() && valuesRowCount < values.length) {
                        comb[comb_counter] = factor.getFactor_values()[i][j];
                        values[valuesRowCount][comb_counter] = comb[comb_counter];
                        comb_counter++;
                    }
                }
                if (values.length > valuesRowCount) {
                    prob[valuesRowCount - 1] += factor.getFactor_prob()[i - 1];
                    numOfAdd++;
                }
                comb_counter = 0;

                for (int k = i + 1; k < factor.getFactor_values().length; k++) {
                    for (int c = 0; c < factor.getFactor_values()[0].length; c++) {
                        if (factor.getFactor_values()[0][c] != toRemove.getName()) {
                            combToCompare[comb_counter] = factor.getFactor_values()[k][c];
                            if(comb_counter < comb.length-1) comb_counter++;
                            else comb_counter = 0;
                        }
                    }
                    if (Arrays.equals(comb, combToCompare)) { //we this is a "twin" row
                        prob[valuesRowCount - 1] += factor.getFactor_prob()[k - 1];
                        numOfAdd++;
                        treatedRows.add(k);
                    }
                }
                valuesRowCount++;
                comb_counter = 0;
            }
        }
        factor.setFactor_values(values);
        factor.setFactor_prob(prob);
        factor.setFactorOf(values[0]);
    }

    public static void normalization(FactorCollection FC) {
        FactorCollection.Factor factor = FC.getFactor_collection().get(0);
        double[] c = factor.getFactor_prob();
        double sum = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 0) continue;
            sum += c[i];
            numOfAdd++;
        }
        for (int i = 0; i < c.length; i++) {
            c[i] = c[i] / sum;
        }
        factor.setFactor_prob(c);
    }

    public static int[] optimalOrderToJoin(ArrayList<FactorCollection.Factor> H_factors, char hidden){
        int[] optimalIndex = new int[2];
        //the array we return is always 2 rows length:
        // 0 = the index of the first factor to join in H_factors arrayList
        // 1 = the index of second factor

        if(H_factors.size() == 2) {
            optimalIndex[0] = 0;
            optimalIndex[1] = 1;
            return optimalIndex;
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < H_factors.size(); i++) {
            for (int j = i + 1; j < H_factors.size(); j++) {
                int x = sizeOfRows(H_factors.get(i), H_factors.get(j), hidden);
                if (x < min) {
                    min = x;
                    optimalIndex[0] = i;
                    optimalIndex[1] = j;
                }
                else if(x == min) {
                    int ASCII_curr_min = 0, ASCII_new = 0;
                    //calculate the ascii sum of the current factors that selected
                    for(int f = 0; f < H_factors.get(optimalIndex[0]).getFactor_values()[0].length; f++)
                        ASCII_curr_min += (int) H_factors.get(optimalIndex[0]).getFactor_values()[0][f];
                    for(int s = 0; s < H_factors.get(optimalIndex[1]).getFactor_values()[0].length; s++)
                        if (indexOfVal(H_factors.get(optimalIndex[0]).getFactor_values()[0], H_factors.get(optimalIndex[1]).getFactor_values()[0][s]) == -1)
                            ASCII_curr_min += (int) H_factors.get(optimalIndex[1]).getFactor_values()[0][s];
                    //calculate the ascii sum of the new factors that we may select
                    for(int n = 0; n < H_factors.get(i).getFactor_values()[0].length; n++)
                        ASCII_new += (int) H_factors.get(i).getFactor_values()[0][n];
                    for(int k = 0; k < H_factors.get(j).getFactor_values()[0].length; k++)
                        if (indexOfVal(H_factors.get(i).getFactor_values()[0], H_factors.get(j).getFactor_values()[0][k]) == -1)
                            ASCII_new += (int) H_factors.get(j).getFactor_values()[0][k];
                    //choose the minimum ascii sum
                    if(ASCII_new < ASCII_curr_min) {
                        min = x;
                        optimalIndex[0] = i;
                        optimalIndex[1] = j;
                    }
                }
                else continue;
            }
        }
        return optimalIndex;
    }

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
            evidence[1][i] = evidenceStr[i].charAt(2);
        }

        //if the answer to the Query is exist on the CPT (of the query variable), so we return it without calculate all the things in the algorithm.
        // String ansCPT = NC.convertToItsNode(Q.charAt(0)).getCpt().isAnswerQuery(evidence,Q);
        //if(ansCPT != "") return ansCPT;

        String[] givenOrder = query.split("\\)")[1].replaceFirst("\\,", "").split("-");
        char[] gOrder = new char[givenOrder.length];
        for(int i=0;i<givenOrder.length;i++)gOrder[i]=givenOrder[i].charAt(0);

        //init factors
        FactorCollection FC = new FactorCollection(NC,evidence);

        for (FactorCollection.Factor F : FC.getFactor_collection()) {
            // F.visualPrint();
        }
        for (int i = 0; i < NC.getNodes().length; i++) {
            //NC.getNodes()[i].getCpt().visualPrint();
        }
        for(char hidden : gOrder) {
            //find all the factors that are factors of the hidden variable
            ArrayList<FactorCollection.Factor> H_factors = new ArrayList<FactorCollection.Factor>();
            for(FactorCollection.Factor factor : FC.getFactor_collection())
                if (factor.getFactorOf().contains(NC.convertToItsNode(hidden)))
                    H_factors.add(factor);

            //choose which 2 factors to join every time (until there is only one factor in the list)
            while(H_factors.size() > 1) {
                int[] index_in_H_factors = optimalOrderToJoin(H_factors, hidden);
                int index_of_first_factor = FC.getFactor_collection().indexOf(H_factors.get(index_in_H_factors[0]));
                int index_of_second_factor = FC.getFactor_collection().indexOf(H_factors.get(index_in_H_factors[1]));
                //FC.getFactor_collection().get(index_of_first_factor).visualPrint();
                //FC.getFactor_collection().get(index_of_second_factor).visualPrint();
                join_factors(FC, FC.getFactor_collection().get(index_of_first_factor), FC.getFactor_collection().get(index_of_second_factor), hidden);
                //update H_factors
                FactorCollection.Factor elementToRemove = H_factors.get(index_in_H_factors[1]);
                H_factors.remove(elementToRemove);
            }

            //eliminate hidden
            if (H_factors.size() != 0) {
                int index_in_FC = FC.getFactor_collection().indexOf(H_factors.get(0)); //there is only one factor there
                eliminate_factors(FC, NC.convertToItsNode(hidden), FC.getFactor_collection().get(index_in_FC));
            }
        }

        //join all remains factors (if exist)
        while (FC.getSize() > 1) { //so there are some factors of the query variable
            int[] index_in_FC = optimalOrderToJoin(FC.getFactor_collection(), Q.charAt(0));
            join_factors(FC, FC.getFactor_collection().get(index_in_FC[0]), FC.getFactor_collection().get(index_in_FC[1]), Q.charAt(0));
        }
        //and normalize
        normalization(FC); //there is only one last factor in the FC, for sure...

        String res = "";

        char Q_val = Q.charAt(2); //the first char of the value of the Query variable (what we need to find in the factor that lefts).
        int row = 0; //we'll find the row in the factor_values that has the answer.
        //certainty there is only ONE COLUMN - of the QUERY variable !!
        for(int i=1; i<FC.getFactor_collection().get(0).getFactor_values().length; i++)
            if(FC.getFactor_collection().get(0).getFactor_values()[i][0] == Q_val) {
                row = i;
                break;
            }
        double prob_ans = FC.getFactor_collection().get(0).getFactor_prob()[row -1];
        res += String.format("%.5g", prob_ans);
        res+= "," + Integer.toString(numOfAdd) + "," + Integer.toString(numOfMul);
        return res;
    }
}
