package safiya.summative;

import java.util.ArrayList;

public class Node {

    private boolean visited;
    private boolean open;
    private int numNeighbours;
    private ArrayList<Node> neighbours;
    private int num;
    private Node up;
    private Node down;
    private Node left;
    private Node right;

    public Node(int num) {
        numNeighbours = 0;
        this.num = num;
        up = down = left = right = null;
        visited = false;
        open = false;
        neighbours = new ArrayList();
    }
    
    /**
     * Getting the direction of the pointer towards a specific neighbour
     * @param neighbour The neighbour in which we are trying to compare
     * @return Direction of the neighbour in String form
     */
    public String getDirection(Node neighbour) {
        String direction = null; //Initializing it as null for return purposes, in case the neighbour is not the same as any of the neighbours inputted.
        if (neighbour == null) {
            return null;
        }
        //IF it is equal to one of the neighbours that the Node is current pointing to, then return that direction
        //I used .equals here instead of comparing their data to make it more readable.
        if (neighbour.equals(left)) {
            direction = "l";
        } else if (neighbour.equals(right)) {
            direction = "r";
        } else if (neighbour.equals(down)) {
            direction = "d";
        } else if (neighbour.equals(up)) {
            direction = "u";
        }
        return direction;
    }
    
    /*
    A series of getters for each Node the current Node is pointing to
    */
    public Node getUp() {
        return up;
    }

    public Node getDown() {
        return down;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
    /**
     * Setting the Node in the up direction and incrementing the number of neighbours the Node currently has. 
     * Also adding the new Node to the neighbours array for later use.
     * @param up The Node that the pointer should change to.
     */
    public void setUp(Node up) {
        numNeighbours++;
        neighbours.add(up);
        this.up = up;
    }
    //It is the same thing for the other 3 directions.
    public void setDown(Node down) {
        numNeighbours++;
        neighbours.add(down);
        this.down = down;
    }

    public void setLeft(Node left) {
        numNeighbours++;
        neighbours.add(left);
        this.left = left;
    }

    public void setRight(Node right) {
        numNeighbours++;
        neighbours.add(right);
        this.right = right;
    }
    /**
     * Getting the number of neighbours the Node is current pointing to
     * @return The number 
     */
    public int getNeighboursNum() {
        return numNeighbours;
    }
    
    /**
     * Getting the ArrayList of the neighbours the Node currently has
     * @return 
     */
    public ArrayList<Node> getNeighbours(){
        return neighbours;
    }
    
    /**
     * Getting the number attached to each Node
     * The "data" is just used as a unique identifier for each Node to more easily compare them without going through the entire .equals() method.
     * @return Unique identifier or number
     */
    public int getData() {
        return num;
    }
    /**
     * Setting whether or not the Node is open 
     * The "open" just means whether or not the cell that the Node represents is a wall or not.
     * @param open Whether it's a wall or not
     */
    public void setOpen(boolean open){
        this.open = open;
    }
    //Getter for the same boolean
    public boolean getOpen(){
        return open;
    }
    /**
     * Checking if the cell that the Node represents has already been visited or not.
     * "Visited" just means whether or not the Maze has already gone to that cell during generation
     * @return boolean of whether it has been visited or not
     */
    public boolean isVisited() {
        return visited;
    }

    //Same boolean, just a setter method.
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    /**
     * Checking if two Nodes are equal using their attributes
     * @param comparison Node to compare
     * @return Whether they are equal or not
     */    
    public boolean equals(Node comparison) {
        boolean equal = false;
        if (comparison == null) {
            return false;
        }
        //Comparing all of the data elements. Since each cell has a unique identifier, we don't need to compare all of the pointers
        if (visited == comparison.isVisited() && open == comparison.getOpen() && num == comparison.getData()){
            equal = true;
        }
        return equal;

    }

}
