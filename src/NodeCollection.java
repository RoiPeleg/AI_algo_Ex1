
//import java.util.ArrayList;

public class NodeCollection {
    //to implements Bayesian Network, and easy work with Node constructor and CPT constructor.

    //attributes
    public Node[] nodes;

    //constructor
    public NodeCollection(String[] input) {
        nodes = new Node[input.length - 2];
        for(int i=2; i<input.length; i++) {
            if(input[i].contains("Network"))
                continue;
            if(input[i].contains("Queries"))
                continue;
            Node n = new Node(input[i]); //send every block to Node constructor;
            addNode(n); //in this method we define this.nodes;
        }
    }

    //methods
    public Node convertToItsNode(char var) {
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




}

