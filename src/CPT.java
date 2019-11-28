import java.util.Arrays;

public class CPT {

    //attributes
    private char [][] CPT_values;
    private double [] CPT_prob;
    private char CPTof;

    //getters
    public char [][] getCPT_values() {
        return this.CPT_values;
    }

    public double[] getCPT_prob() {
        return this.CPT_prob;
    }

    public char getCPTof() {
        return this.CPTof;
    }

    //constructors

    //other methods
    public boolean containsEvidence(char[][]evidence) {
        for(int i=0; i<this.getCPT_values()[0].length; i++) {
            char variable = this.getCPT_values()[0][i];
            for(int j=0; j<evidence[0].length; j++) {
                if(evidence[0][j] == variable)
                    return true;
            }
            /*if(Arrays.asList(evidence[0]).contains(variable)) {
                return true;
            }*/
        }
        return false;
    }

    public double[] copyToFactorProb;

    public char[][] copyToFactorValues;
}
