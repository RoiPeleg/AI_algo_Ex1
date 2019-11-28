import java.util.Arrays;

public class Factor {

    //attributes
    private char [][] factor_values;
    private double [] factor_prob;
    private char factorOf;

    //constructors
    public Factor(CPT T, char[][]evidence) {
        if(!T.containsEvidence(evidence)) { //so we need to copy the CPT as is
           this.factor_values = T.copyToFactorValues;
           this.factor_prob = T.copyToFactorProb;
        }
        else {
            //to determine the size of the factor table
            //we need to check how many rows we need to remove...
        }

    }
}
