import java.io.IOException;
import java.util.ArrayList;

public class Ex1 {
    public static void main(String[] args) {
        NodeCollection nc = Parser.toConstruct();
        String[] q = nc.getQueries();
        ArrayList<String>ls = new ArrayList<String>();
        for (int i=0;i<q.length;i++)
        {
            // System.out.println("q"+i + ") "+q[i]);
            System.out.println(VariableElimination.variableElimination(nc, q[i]));
            ls.add(VariableElimination.variableElimination(nc,q[i]));
        }
        try {
            Parser.toOutput(ls);
        }
        catch (IOException e){e.printStackTrace();}
        }
}
