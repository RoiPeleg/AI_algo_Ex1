import java.util.ArrayList;

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
    public CPT(String block, char name, String[] values, ArrayList<NodeCollection.Node> parents) {
        this.CPT_values = new char[sizeOfRows(values, parents)][sizeOfCols(parents)];
        this.CPTof = name;

        //init the values in the first row (variables names)
        CPT_values[0][0] = name;
        for(int j=1; j<CPT_values[0].length; j++)
            CPT_values[0][j] = parents.get(j).getName();
        
    }

    //private methods to help us in the constructor
    private int sizeOfCols(ArrayList<NodeCollection.Node> parents) {
        return parents.size() + 1;
    }

    private int sizeOfRows(String[] values, ArrayList<NodeCollection.Node> parents) {
        int size = values.length;
        for(int i=0; i<parents.size(); i++) {
            size *= parents.get(i).getValues().length;
        }
        return size;
    }

    //other methods
    public boolean containsEvidence(char[][]evidence) {
        for(int i=0; i<this.getCPT_values()[0].length; i++) {
            char variable = this.getCPT_values()[0][i];
            for(int j=0; j<evidence[0].length; j++) {
                if(evidence[0][j] == variable)
                    return true;
            }
        }
        return false;
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

}
