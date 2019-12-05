
    import java.util.ArrayList;

    public class NodeCollection {
        //to implements Bayesian Network, and easy work with Node constructor and CPT constructor.

        //attributes
        public Node[] nodes = new Node[0];

        //constructor
        public NodeCollection(String[] input) {
            for(int i=2; i<input.length; i++) {
                if(input[i].contains("Network"))continue;
                if(input[i].contains("Queries"))continue;
                Node n = new Node(input[i]); //send every block to Node constructor;
                addNode(n); //in this method we define this.nodes;
            }
        }

        //methods
        public Node convertToItsNode(char var) {
            int index = -1;
            for(int i=0; i<this.nodes.length; i++) {
                if(nodes[i].getName() == var)
                    return nodes[i];
            }
            return null;
        }

        public void addNode(Node n) {
            int newSize = this.nodes.length+1;
            Node[] newNodes = new Node[newSize];
            for(int i=0; i<this.nodes.length; i++)
                newNodes[i] = this.nodes[i];
            newNodes[newSize] = n;
            this.nodes = newNodes;
        }


        public class Node
        {
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
                String parents = "";
                for(int i=8; i<block_rows[1].length(); i++)  //i=8 because that's the index which the word "values: " ends.
                    values += block_rows[1].charAt(i);
                this.values = values.split(",");
                this.shortValuesNames = new char[this.values.length];
                for(int i=0; i<this.values.length; i++)
                    this.shortValuesNames[i] = this.values[i].charAt(0);

                //init parents ArrayList<Node>
                String[] arrOfParents;
                for(int i=9; i<block_rows[2].length(); i++)  //i=9 is where "parents: " ends.
                    parents += block_rows[2].charAt(i);
                if(parents.equals("none"))
                    this.parents = null;
                else {
                    arrOfParents = parents.split(",");
                    for(int j=0; j<arrOfParents.length; j++)
                        this.parents.add(convertToItsNode(arrOfParents[j].charAt(0)));
                }

                //init CPT
                this.cpt = new CPT(block, this.Name, this.values, this.parents);
            }
        }


    }

