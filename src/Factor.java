import java.util.Arrays;

public class Factor {

    //attributes
    private char[][] factor_values;
    private double[] factor_prob;
    private char[] factorOf;

    //getters
    public char[][] getFactor_values() {
        return factor_values;
    }

    public double[] getFactor_prob() {
        return factor_prob;
    }

    public char[] getFactorOf() {
        return factorOf;
    }

    //constructor
    public Factor(CPT T, char[][]evidence) {
        this.factor_values = T.copyToFactorValues();
        this.factor_prob = T.copyToFactorProb();
        //if there are evidence variables in CPT, so we need to remove some rows from it
        if(T.containsEvidence(evidence))
            for (int i = 0; i < evidence[0].length; i++)
            for (int j = 0; j < T.getCPT_values()[0].length; j++)
                if (evidence[0][i] == T.getCPT_values()[0][j])
                    for (int k = 1; k < T.getCPT_values().length; k++)
                    if (T.getCPT_values()[k][j] != evidence[1][i]) { //so we need to remove this row to factor
                        this.factor_values = remove_row(this.factor_values, k);
                        this.factor_prob = remove_value(this.factor_prob, k-1);
                    }
        int variables_count = this.factor_values[0].length;
        this.factorOf = new char[variables_count];
        for(int i=0; i<variables_count; i++) {
            this.factorOf[i] = this.factor_values[0][i];
        }
    }

    //Manually constructor (for testing)
    public Factor(char[][] values, double[] prob) {
        this.factor_values = values;
        this.factor_prob = prob;
        this.factorOf = new char[values[0].length];
        for(int i=0; i<values[0].length; i++) {
            this.factorOf[i] = values[0][i];
        }
    }

    // Auxiliary functions for the constructor
    private double[] remove_value(double[] arr, int value_indx) {
        int newSize = arr.length-1;
        double[] newArr = new double[newSize];
        int i = 0;
        while (i != value_indx) {
            newArr[i] = arr[i];
            i++;
        }
        if(i+1 < arr.length) {
            int j = i+1;
            while(i < newSize) {
                newArr[i] = arr[j];
                i++;
                j++;
            }
        }
        return newArr;
    }

    private char[][] remove_row(char[][] table, int row_indx) {
        int newSizeRow = table.length-1;
        int colSize = table[0].length;
        char[][] newTable = new char[newSizeRow][colSize];
        int i;
        for(i=0; i<newSizeRow; i++) {
            if(i == row_indx) break;
            for(int j=0; j<colSize; j++) {
                newTable[i][j] = table[i][j];
            }
        }
        if(i+1 < table.length) {
            int r = i+1;
            while(i < newSizeRow) {
                for(int j=0; j<colSize; j++)
                    newTable[i][j] = table[r][j];
                i++;
                r++;
            }
        }
        return newTable;
    }

   /* private static char[] copyTheRow(char [][] table, int row_num) {
        char [] arr = new char[table[row_num].length];
        for(int i=0; i<arr.length; i++) {
            arr[i] = table[row_num][i];
        }
    }*/

   /* private static char [][] append(char[][]a, char[]b) { //or use "Object" instead of "char"
        //if a/b == null
        char [][] result = new char[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }*/

   //printing method (to help us test this class visually)
   public void visualPrint() {
       System.out.println("Factor of: " + Arrays.toString(this.getFactorOf()));
       System.out.println();
        for(int i=0; i<getFactor_values().length; i++) {
            for(int j=0; j<getFactor_values()[0].length; j++) {
                System.out.print(getFactor_values()[i][j] + " ");
            }
            if(i == 0) {
                System.out.println("| P");
                System.out.println();
            }
            else
                System.out.println(" "+ getFactor_prob()[i-1]);
        }
   }

   //methods for VARIABLE ELIMINATION algorithm

    public void join_factors(Factor A, Factor B) {
        int numOfCols = 0, numOfRows = 0;
        //......
    }

    public void eliminate_factors(Factor A, Factor B){;}

    private void normalization(Factor f){;}

    private void optimalOrderToJoin(Factor[] factor_collection){;}
}
