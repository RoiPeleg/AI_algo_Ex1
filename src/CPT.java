
import java.util.ArrayList;
import java.util.Arrays;

public class CPT {

    //attributes
    private char [][] CPT_values;
    private double [] CPT_prob;
    private char CPTof;

    //getters
    public char[][] getCPT_values() {
        return this.CPT_values;
    }

    public double[] getCPT_prob() {
        return this.CPT_prob;
    }

    public char getCPTof() {
        return this.CPTof;
    }

    //constructors
    public CPT(String block, char name, char[] values, ArrayList<NodeCollection.Node> parents) {
        this.CPT_values = new char[sizeOfRows(values, parents)][sizeOfCols(parents)];
        this.CPT_prob = new double[CPT_values.length-1];
        this.CPTof = name;

        //set the values in the first row (variables names)
        for(int j=0; j<parents.size(); j++)
            CPT_values[0][j] = parents.get(j).getName();
        CPT_values[0][CPT_values[0].length-1] = name;

        //set the rest of the CPT (and prob)
        String[] block_rows = block.split("\n");
        int row_count_block = 4; //4 = the line where the content of the CPT begins;
        int row_count_CPT = 1;
        int col_count_CPT = 0;
        initCPT(values, block_rows, row_count_block, row_count_CPT, col_count_CPT);
    }

    //private methods to help us in the constructor
    private int sizeOfCols(ArrayList<NodeCollection.Node> parents) {
        return parents.size() + 1;
    }

    private int sizeOfRows(char[] values, ArrayList<NodeCollection.Node> parents) {
        int size = values.length;
        for(int i=0; i<parents.size(); i++) {
            size *= parents.get(i).getValues().length;
        }
        return size + 1; //+1 because it's the row with variables's names.
    }

    public void initCPT(char[] values, String[] block_rows, int row_count_block, int row_count_CPT, int col_count_CPT) {
        while (row_count_block < block_rows.length && row_count_CPT < CPT_values.length) {
            String[] s = block_rows[row_count_block].split(",");
            int equ_count = 0; //counter for the number of '=' we already had on the string.
            for (int i = 0; i < s.length; i++) {
                if (s[i].charAt(0) != '=' && s[i].charAt(0) != '0') {
                    this.CPT_values[row_count_CPT][col_count_CPT] = s[i].charAt(0);
                    col_count_CPT++;
                }
                else if (s[i].charAt(0) == '=') {
                    if(equ_count != 0 && CPT_values[0].length > 1) {
                        while(col_count_CPT < values.length-1) {
                            this.CPT_values[row_count_CPT][col_count_CPT] = CPT_values[row_count_CPT-1][col_count_CPT];
                            col_count_CPT++;
                        }
                    }
                    this.CPT_values[row_count_CPT][col_count_CPT] = s[i].charAt(1);
                    equ_count++;
                }
                else {//begins in '0'
                    this.CPT_prob[row_count_CPT-1] = Double.parseDouble(s[i]);
                    row_count_CPT++;
                    col_count_CPT = 0;
                }
            }
            //init the last value in a new row, before we continue to the next block row;
            for(int j=0; j<CPT_values[0].length-1; j++) {
                CPT_values[row_count_CPT][col_count_CPT] = CPT_values[row_count_CPT-1][col_count_CPT];
                col_count_CPT++;
            }
            CPT_values[row_count_CPT][CPT_values[0].length-1] = values[values.length-1];
            double sumToSub = 0;
            for(int i=row_count_CPT-1; i>row_count_CPT-values.length; i--)
                sumToSub+= CPT_prob[i-1];
            CPT_prob[row_count_CPT-1] = 1.0-sumToSub;
            row_count_CPT++;
            col_count_CPT = 0;
            row_count_block++;

            equ_count = 0;
        }
    }

    //other methods
    /**
     * method that count how many evidence variables this CPT contains
     * @param evidence = 2D array that represent the evidence variables names & it's value
     * @return the number of evidence variables in this CPT:
     * (0: if does not exist variables at all)
     */
    public int countEvidence(char[][]evidence) {
        int counter = 0;
        for(int i=0; i<evidence[0].length; i++) {
            for(int j=0; j<this.CPT_values[0].length; j++) {
                if(this.CPT_values[0][j] == evidence[0][i])
                    counter ++;
            }
        }
        return counter;
    }

    public double[] copyToFactorProb() {
        int size = this.getCPT_prob().length;
        double[] arrCopy = new double[size];
        for(int i=0; i<size; i++) {
            arrCopy[i] = this.getCPT_prob()[i];
        }
        return arrCopy;
    }

    public char[][] copyToFactorValues() {
        int rowSize = this.getCPT_values().length;
        int colSize = this.getCPT_values()[0].length;
        char[][] tableCopy = new char[rowSize][colSize];
        for(int i=0; i<rowSize; i++) {
            for(int j=0; j<colSize; j++) {
                tableCopy[i][j] = this.getCPT_values()[i][j];
            }
        }
        return tableCopy;
    }

    //printing method (to help us test this class visually)
    public void visualPrint() {
        System.out.println("CPT of: " + this.getCPTof());
        System.out.println();
        for(int i=0; i<getCPT_values().length; i++) {
            for(int j=0; j<getCPT_values()[0].length; j++) {
                System.out.print(getCPT_values()[i][j] + " ");
            }
            if(i == 0) {
                System.out.println("| P");
                System.out.println();
            }
            else
                System.out.println(" "+ getCPT_prob()[i-1]);
        }
    }

    /**
     * Method that checks if the CPT contains the answer to the Query
     * @param evidence = 2D array of evidence variables:
     *                 row - 0: the variables names.
     *                 row - 1: the variables values (that given as evidence)
     * @param Q = a string that represents the query variable (name) and it's value [in form of: "Q=value"]
     * @return the query's answer if it's exist in the CPT
     *         an empty string "" if we can't answer the query.
     */
    public String isAnswerQuery(char[][] evidence, String Q) {
        char[] comb = new char[evidence[0].length + 1]; //the size is the number of evidence variables + the query.
        int comb_count = 0;
        char[] combToCompare = new char[comb.length];
        if(countEvidence(evidence) != 0) {
            for (int j = 0; j < this.CPT_values[0].length; j++) {
                char val = CPT_values[0][j];
                if (val == Q.charAt(0)) {
                    comb[comb_count] = Q.charAt(2);
                    comb_count++;
                }
                if (indexVal(evidence[0], val) != -1) {
                    comb[comb_count] = evidence[1][indexVal(evidence[0], val)];
                    comb_count++;
                }
            }
            comb_count = 0;
            for(int i = 1; i < this.CPT_values.length; i++) {
                for(int j = 0; j < this.CPT_values[0].length; j++) {
                    if (CPT_values[0][j] == Q.charAt(0) || indexVal(evidence[0], CPT_values[0][j]) != -1) {
                        if (comb_count < combToCompare.length) combToCompare[comb_count] = CPT_values[i][j];
                        comb_count++;
                    }
                }
                comb_count = 0;
                if(Arrays.equals(comb,combToCompare)) {
                    String p = String.format("%.5g", CPT_prob[i - 1]);
                    return p+",0,0";
                }
            }
        }
        return "";
    }

    private int indexVal(char[] arr, char c) {
        for(int i=0; i<arr.length; i++)
            if(arr[i] == c) return i;
        return -1;
    }

}

