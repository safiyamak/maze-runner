package safiya.summative;

//This code is based on to the youtube video: https://www.youtube.com/watch?v=CNSg5tiWiA4
//I've made some modifications to fit my code better
//I did this because this data structure is more complex and not what we learned in this course, so I needed assistance creating it
public class LinkedGrid {
    private Node first;
    private Node last;
    private int dimension;
    

    public LinkedGrid() {
        last = null;
        dimension = 5;
        int counter = 1; //Starting the identifiers at one
        //Initializing first node
        first = new Node(counter++);
        Node columnMarker = first;
        Node rowMarker = first;

        //Creating rest of row
        for (int i = 0; i < dimension - 1; i++) {
            //Creating each Node in the row with a unique identifier
            Node nextNode = new Node(counter++);
            //These are for when I need to "reset" the maze, and I want to make a new unique maze independent of the previously created one
            //In this case, I need to reset the attributes of the Nodes
            nextNode.setOpen(false);
            nextNode.setVisited(false);
            //Setting the two pointers pointing both right and left.
            columnMarker.setRight(nextNode);
            nextNode.setLeft(columnMarker);
            //Setting the columnMarker to the next column to the right to make sure that I make the next Node in the row
            columnMarker = nextNode;
        }

        for (int i = 0; i < dimension - 1; i++) {
            //Creating first node in a row
            Node temp = new Node(counter++);
            //I need to reset the attributes for every single Node, so every time a new Node is created, I reset it.
            temp.setOpen(false);
            temp.setVisited(false);
            
            //Making a pointer to initalize the next row
            rowMarker.setDown(temp);
            temp.setUp(rowMarker); //Making sure the next row points to the last row
            //Resetting the rowMarker and columnMarker
            rowMarker = temp;
            columnMarker = temp;

            //Creating the rest of the row
            for (int j = 0; j < dimension - 1; j++) {
                temp = new Node(counter++);
                temp.setOpen(false);
                temp.setVisited(false);
                //Going back and forth in the row similar to the first for loop
                columnMarker.setRight(temp);
                temp.setLeft(columnMarker);
                temp.setUp(columnMarker.getUp().getRight());
                //Making suring the top row points to the bottom row
                temp.getUp().setDown(temp);
                //Going to the rest of the row
                columnMarker = temp;
                //Setting last every time since eventually it will go to the last Node, and that will be the last run
                last = temp;
            }

        }
        //The first Node is always open
        first.setOpen(true);

    }

    //Series of getters and setters for each Node and the dimension
    public Node getLast() {
        return last;
    }

    public Node getFirst() {
        return first;
    }

    public void setLast(Node last) {
        this.last = last;
    }
    
    public int getDimension(){
        return dimension;
    }


}
