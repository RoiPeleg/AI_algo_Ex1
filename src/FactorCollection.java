import java.util.ArrayList;
import java.util.Arrays;

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
            if(!(containsVar(A, varToJoin)) || !(containsVar(B, varToJoin)))
                throw new RuntimeException("One or more of the factors are not factor of the variable you want to join");
            Factor f = new Factor(sizeOfRows(A,B,varToJoin), sizeOfCols(A,B,varToJoin));

            //......
        }

        private int sizeOfCols(Factor A, Factor B, char var) {
            return 0;
        }

        private int sizeOfRows(Factor A, Factor B, char var) {
            int a_col = -1, b_col = -1; //the column in every factor that belongs to var
            for(int i=0; i<A.factor_values[0].length; i++) {
                if (A.factor_values[0][i] == var) a_col = i;
                break;
            }
            for(int j=0; j<B.factor_values[0].length; j++) {
                if (A.factor_values[0][j] == var) b_col = j;
                break;
            }
            ArrayList<Character> values = new ArrayList<Character>();
            for(int i=0; i<A.factorOf.size(); i++) {
                char value = A.factorOf.get(i).getShortValuesNames()[i];
                values.add(value);
            }
            //...........
            return 0;
        }

        private boolean containsVar(Factor f, char varToJoin) {
            for(int i=0; i<f.factorOf.size(); i++)
                if (f.factorOf.get(i).getName() == varToJoin)
                    return true;
            return false;
        }

        public void eliminate_factors(Factor A, Factor B){;}

        private void normalization(Factor f){;}

        private void optimalOrderToJoin(Factor[] factor_collection){;}
    }



}
