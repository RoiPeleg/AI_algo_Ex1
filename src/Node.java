import java.util.ArrayList;
public class Node {
    private char Name;
    private String[] values;
    private char[] shortValuesNames; //represents the first character of value's name;
    private ArrayList<Node> parents;//stores parents
    private CPT cpt;

    //getters
    public char getName() {
        return Name;
    }

    //constructor

    /**
     * Constructor that gets Block and defines all the attributes of the Node
     * @param block= a string that represents a variable, with all it's information
     */
    public Node(String block) {
        String[] block_rows = block.split("\n"); //split each row of string
        //init Name
        this.Name = block_rows[0].charAt(1);
        //init values attributes
        String values = "";
        for(int i=8; i<block_rows[1].length(); i++) { //i=8 because that's the index which the word "values: " ends.
            values+= block_rows[1].charAt(i);
            this.values = values.split(","); //maybe we should init the array "values" first!!!!!!!!!!!
        }
        this.shortValuesNames = new char[this.values.length];
        for(int i=0; i<this.values.length; i++)
            this.shortValuesNames[i] = this.values[i].charAt(0);
        //init parents ArrayList<Node>
        /*for(int i=9; i<block_rows[2].length(); i++) { //i=9 is where "parents: " ends.

        }*/
    }

}
