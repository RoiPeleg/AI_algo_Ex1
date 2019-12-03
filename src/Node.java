import java.util.ArrayList;
public class Node {
    private String Name;
    private char shortName;
    private ArrayList<Node> parents;//stores parents
    private CPT cpt;

    //getters
    public String getName() {
        return Name;
    }
    public char getshortName() {
        return shortName;
    }

    //constructor

    /**
     * Constructor that gets Block and defines all the attributes of the Node
     * @param block= a string that represents a variable, with all it's information
     */
    public Node(String block) {
        String[] s = block.split("\n"); //split each row of string
        for(int i=0;i<s.length;i++) {

        }
    }

}
