public class Ex1 {
    public static void main(String[] args) {
        NodeCollection nc = Parser.toConstruct();
        String[] q = nc.getQueries();
        for (int i=0;i<q.length;i++)
        {
            System.out.println("q"+i + ") "+q[i]);
        }
        //Queries handling
        //write to output
    }
}
