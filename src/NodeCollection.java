
import java.util.ArrayList;

public class NodeCollection {
    //to implements Bayesian Network, and easy work with Node constructor and CPT constructor.

    //attributes
    public Node[] nodes;
    private int counter = -1;
    private String[] Queries;

    //getters
    public String[] getQueries(){return Queries;}

    public Node[] getNodes() {
        return nodes;
    }

    //constructor
    public NodeCollection(String[] input) {
        nodes = new Node[input.length - 2];
        for(int i=2; i<input.length; i++) {
            if(input[i].contains("Queries")) {
                String[] inp = input[i].split("Queries");
                Node n = new Node(inp[0]);
                counter++;
                nodes[counter] = n;
                Queries = inp[1].replaceFirst("\\n","").split("\\n");
            }
            else {
                Node n = new Node(input[i]); //send every block to Node constructor;
                counter ++;
                nodes[counter] = n; //in this method we define this.nodes;
            }
        }
    }

    //methods
    public Node convertToItsNode(char var) {
        for(int i=0; i<this.nodes.length; i++) {
            if(nodes[i] != null && nodes[i].getName() == var)
                return nodes[i];
        }
        return null;
    }

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

        public ArrayList<Node> getParents() {
            return parents;
        }

        public String[] getValues() {
            return values;
        }

        public char[] getShortValuesNames() {
            return shortValuesNames;
        }

        public CPT getCpt() {
            return cpt;
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
            String values_str = "";
            String parents_str = "";
            for(int i=8; i<block_rows[1].length(); i++)  //i=8 because that's the index which the word "values: " ends.
                values_str += block_rows[1].charAt(i);
            this.values = values_str.split(",");
            this.shortValuesNames = new char[this.values.length];
            for(int i=0; i<this.values.length; i++)
                this.shortValuesNames[i] = this.values[i].charAt(0);

            //init parents ArrayList<Node>
            String[] arrOfParents;
            for(int i=9; i<block_rows[2].length(); i++)  //i=9 is where "parents: " ends.
                parents_str += block_rows[2].charAt(i);
            this.parents = new ArrayList<Node>();
            if(!parents_str.equals("none")) {
                arrOfParents = parents_str.split(",");
                for(int j=0; j<arrOfParents.length; j++)
                    this.parents.add(convertToItsNode(arrOfParents[j].charAt(0)));
            }

            //init CPT
            this.cpt = new CPT(block, this.Name, this.shortValuesNames, this.parents);
        }
    }

}

