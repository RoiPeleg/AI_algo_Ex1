import java.util.ArrayList;

public class Ex1 {
    public static void main(String[] args) {
        NodeCollection nc = Parser.toConstruct();
        String[] q = nc.getQueries();
        ArrayList<String>ls = new ArrayList<String>();
        for (int i=0;i<q.length;i++)
        {
            System.out.println("q"+i + ") "+q[i]);
        }
        //Queries handling
        Parser.toOutput(ls);
        }
}
