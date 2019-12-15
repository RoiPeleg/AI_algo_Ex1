import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class FactorCollection {

    //attributes
    private ArrayList<Factor> factor_collection;
    private int size;
    private NodeCollection NC; //the Node Collection this Factor Collection belongs to.

    //getters
    public ArrayList<Factor> getFactor_collection() {
        return factor_collection;
    }
    public int getSize() {
        return size;
    }
    public NodeCollection getNC() {
        return NC;
    }

    //constructor
    public FactorCollection(NodeCollection NC, char[][]evidence) {
        this.NC = NC;
        factor_collection = new ArrayList<Factor>();
        for(int i=0; i<NC.nodes.length; i++) {
            Factor f = new Factor(NC.nodes[i].getCpt(), evidence);
            addFactor(f);
        }
        removeOneValued();
    }

    //methods
    public void addFactor(Factor f) {
        factor_collection.add(f);
        size++;
    }

    private void removeOneValued() {
        int index = 0;
        while(index < this.size) {
            if(this.factor_collection.get(index).isOneValued()) {
                factor_collection.remove(index);
                index--; //because all the other elements following it move down one index.
                size --;
            }
            index++;
        }
    }

    public class Factor {

        //attributes
        private char[][] factor_values;
        private double[] factor_prob;
        //private char[] factorOf;
        private ArrayList<NodeCollection.Node> factorOf;

        //getters
        public char[][] getFactor_values() {
            return factor_values;
        }

        public double[] getFactor_prob() {
            return factor_prob;
        }

        public ArrayList<NodeCollection.Node> getFactorOf() {
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
                                if (T.getCPT_values()[k][j] != evidence[1][i]) { //so we need to remove this row from the factor
                                    this.factor_values = remove_row(this.factor_values, k);
                                    this.factor_prob = remove_value(this.factor_prob, k-1);
                                }
            int variables_count = this.factor_values[0].length;
            this.factorOf = new ArrayList<NodeCollection.Node>(variables_count);
            for(int i=0; i<variables_count; i++) {
                this.factorOf.add(NC.convertToItsNode(factor_values[0][i]));
            }
        }

        public Factor(int sizeRow, int sizeCol) {
            this.factor_values = new char[sizeRow+1][sizeCol];
            this.factor_prob = new double[sizeRow];
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

        public boolean isOneValued() {
            if(this.factor_prob.length == 1)
                return true;
            return false;
        }

        //printing method (to help us test this class visually)
        public void visualPrint() {
            System.out.println("Factor of: " + factorOf.toString());
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

        public void join_factors(Factor A, Factor B, char varToJoin) {
            if((findVarInd(A, varToJoin))==-1 || (findVarInd(B, varToJoin))==-1) //var isn't exist on the factor/s
                throw new RuntimeException("One or more of the factors are not factor of the variable you want to join");
            Factor joinFactor = new Factor(sizeOfRows(A,B,varToJoin), sizeOfCols(A,B));

            //......
        }

        private int sizeOfCols(Factor A, Factor B) {
            int counter = A.factorOf.size(); //for starting: the counter will be the size of A columns.
            //now we'll unite it with the variables of B Factor to create the final group of the variables of the new factor.
            for(int i=0; i<B.factor_values[0].length; i++) { //the length of the first row on B's factor_values = B.factorOf.size().
                if(!(Arrays.asList(A.factor_values[0]).contains(B.factor_values[0][i])))
                    counter ++;
            }
            return counter;
        }

        private int sizeOfRows(Factor A, Factor B, char var) {
            int a_col = -1, b_col = -1; //the column in every factor that belongs to var
            for(int i=0; i<A.factor_values[0].length; i++) { //to find the column of var in Factor A
                if (A.factor_values[0][i] == var) a_col = i;
                break;
            }
            for(int j=0; j<B.factor_values[0].length; j++) { //to find the column of var in Factor B
                if (A.factor_values[0][j] == var) b_col = j;
                break;
            }
            int counter = 0;
            char[] values; //we'll define this array of var's values
            values = factorOf.get(findVarInd(A, var)).getShortValuesNames(); //now values point on var's shortValuesNames array.
            for(int val=0; val<values.length; val++) //Loop that goes through all possible values of var
                for(int i = 1; i < A.factor_values.length; i++) //Loop that goes through all the a_col column in Factor A
                    if(A.factor_values[i][a_col] == val)
                        for(int j = 1; j < B.factor_values.length; j++) //Loop that goes through all the b_col column in Factor B
                             if(B.factor_values[j][b_col] == val) counter++;
            return counter;
        }

        private int findVarInd(Factor f, char varToJoin) {
            for(int i=0; i<f.factorOf.size(); i++)
                if (f.factorOf.get(i).getName() == varToJoin)
                    return i;
            return -1;
        }

        public void eliminate_factors(Factor A, Factor B){;}

        private void normalization(Factor f){;}

        private void optimalOrderToJoin(Factor[] factor_collection){;}
    }



}
