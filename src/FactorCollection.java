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

        //setters
        public void setFactor_values(char[][] factor_values) {
            this.factor_values = factor_values;
        }
        public void setFactor_prob(double[] factor_prob) {
            this.factor_prob = factor_prob;
        }
        public void setFactorOf(ArrayList<NodeCollection.Node> factorOf) {
            this.factorOf = factorOf;
        }

        //constructor
        public Factor(CPT T, char[][]evidence) {
            this.factor_values = T.copyToFactorValues();
            this.factor_prob = T.copyToFactorProb();
            //if there are evidence variables in CPT, so we need to remove some rows from it
            int count_evidence = T.countEvidence(evidence);
            if(count_evidence != 0)
                for (int i = 0; i < evidence[0].length; i++)
                    for (int j = 0; j < T.getCPT_values()[0].length; j++)
                        if (evidence[0][i] == T.getCPT_values()[0][j]) {
                            int factor_count = 1;
                            for (int k = 1; k < T.getCPT_values().length; k++) {
                                if (T.getCPT_values()[k][j] != evidence[1][i]) { //so we need to remove this row from the factor
                                    this.factor_values = remove_row(this.factor_values, factor_count);
                                    this.factor_prob = remove_value(this.factor_prob, factor_count-1);
                                    factor_count --;
                                }
                                factor_count ++;
                            }
                        }
            if(count_evidence != 0) { //the factor contains evidence
                this.factor_values = remove_evidence_col(this.factor_values, evidence, count_evidence);
            }
            int variables_count = this.factor_values[0].length;
            this.factorOf = new ArrayList<NodeCollection.Node>(variables_count);
            for(int i=0; i<variables_count; i++) {
                this.factorOf.add(NC.convertToItsNode(factor_values[0][i]));
            }
        }

        public Factor() { //default constructor
            this.factor_values = null;
            this.factor_prob = null;
            this.factorOf = null;
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

        private char[][] remove_evidence_col(char[][] table, char[][] evidence, int count_evidence) {
            char[][] newTable = new char[table.length][table[0].length - count_evidence];
            int newTable_count = 0; //counter for number of columns we already define in the new table.
            for(int i=0; i<evidence[0].length; i++) {
                if(containsVal(table[0], evidence[0][i])) {
                    for(int j=0; j<table[0].length; j++) {
                        if(table[0][j] != evidence[0][i]) //so we need to copy this column to the result (left it)
                            for(int k=0; k<newTable.length; k++)
                                newTable[k][newTable_count] = table[k][j];
                        newTable_count ++;
                    }
                }
            }
            return newTable;
        }

        private boolean containsVal(char[] arr, char c) {
            for(int i=0; i<arr.length; i++)
                if(arr[i] == c) return true;
            return false;
        }

        public boolean isOneValued() {
            if(this.factor_prob.length == 1)
                return true;
            return false;
        }

        //printing method (to help us test this class visually)
        public void visualPrint() {
            System.out.println("Factor of: " + Arrays.toString(factor_values[0]));
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
            System.out.println();
        }


    }



}
