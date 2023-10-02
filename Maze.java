package safiya.summative;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Maze {

    private LinkedGrid maze;
    private Node lastNode;
    private int playerFileLength;
    private FileWriter out;

    public Maze() {
        maze = new LinkedGrid();
        lastNode = maze.getLast();
        playerFileLength = 0;
    }

    /**
     * Recursively generating the Maze using the current Node
     *
     * @param current The current node it is at, starts with first Node when it
     * is first called
     */
    public void generateMaze(Node current) {
        //BASE CASE: If the current Node is the same as the last Node (based on the number identifier)
        if (current.getData() == maze.getLast().getData()) { //Cant use .equals method because the current Node other attributes have not been altered yet, so it is simply based on if they are the same number
            isValidMaze(); //Checking if it is a validMaze before ending
        } else {
            //Getting the neighbours of the current Node
            ArrayList<Node> neighbours = current.getNeighbours();
            //Checking each Node in the neighbours array and removing them if they have already been visited to avoid repetition.
            for (int i = 0; i < neighbours.size(); i++) {
                if (neighbours.get(i).isVisited()) {
                    neighbours.remove(i);
                }
            }

            Random rand = new Random();
            //IF there are neighbours to analyze
            if (!neighbours.isEmpty()) {
                //Pick one of the neighbours randomly
                int randNeighbour = rand.nextInt(neighbours.size());
                Node temp = neighbours.get(randNeighbour);
                current.setVisited(true); //Set the current as visited
                temp.setOpen(true); //Set the new Node as open
                //I set the new Node and not the current Node because if it is the first Node, the first Node will always be open
                current = temp;
                //RECURSIVE CALL: Keep generating the maze based on the current Node.
                generateMaze(current);
                //If the neighbours are empty, that means that the Maze has trapped itself in a spot, and we need to set the last as that Node because the Maze cannot go anywhere else
            } else {
                maze.setLast(current);
                isValidMaze();
            }

        }
    }

    /**
     * Checking if the maze is empty after the Maze has finished generating If
     * it is empty, it will reset the maze and regenerate the Maze until it is
     * not empty
     *
     * BIG O ANALYSIS HERE
     */
    public void isValidMaze() {
        int counter = 0;
        //area represents the total area of the maze.
        int area = maze.getDimension() * maze.getDimension();
        Node rowMarker = maze.getFirst(); //Traversing each Node in the maze
        Node temp = maze.getFirst();
        while (rowMarker != null) {
            while (temp != null) {
                //IF the cell is open or a ".", then add to the counter
                if (temp.getOpen() == true) {
                    counter++;
                }
                temp = temp.getRight();
            }
            temp = rowMarker.getDown();
            rowMarker = temp;
        }
        //IF the counter 
        if (counter == area) {
            resetMaze();
            generateMaze(maze.getFirst());
        }

        /*
        BIG O ANALYSIS:
        O(N^2) This is a linear algorithm for a 2D array. Therefore, it has both rows and columns
        it must go through. Because of this, the algorithm is N*N. N for rows, and N for columns.
        The N in this case represents the dimensions of the Maze.
        rowMarker != null runs based off of the number of rows there are. This is based on the dimension.
        OL      IL              total
        n = 0   n = 0,1…n-1     n - 1
        n = 1   n = 
         */
    }

    /**
     * Outputting the mazes in a text file. Also taking in user input and
     * returning the chosenMaze in a 2D array structure.
     *
     * @param fileName Name of the file
     * @return The chosen maze in 2D array structure
     */
    public String[][] outputMazes(String fileName) {
        File textFile = new File(fileName);
        Scanner s = new Scanner(System.in);
        String[][] chosenMaze = new String[5][5];
        String[] allMazes;
        //Initializing allMazes based on if it is the pre generated mazes or user generated mazes.
        if (fileName.equals("mazes.txt")) {
            allMazes = new String[5];
        } else { //This is for "playerMazes.txt"
            allMazes = new String[playerFileLength];
        }
        int num = 0; //Maze number
        FileReader in;
        BufferedReader readFile;
        String lineOfText;
        //Initializing each element in the allMazes array to avoid null elements
        for (int i = 0; i < allMazes.length; i++) {
            allMazes[i] = "";
        }
        //Step 1: Reading in the text file for all the generated mazes
        try {
            // Read in the file using the FileReader
            in = new FileReader(textFile);
            readFile = new BufferedReader(in); // Passing the FileReader Object to the BufferedReader

            // Print out the document
            while ((lineOfText = readFile.readLine()) != null) {
                lineOfText = lineOfText.replace("\n", "");
                System.out.println(lineOfText);
                //IF the line of text is a number
                if (isNum(lineOfText)) {
                    num = Integer.parseInt(lineOfText);
                    num--; //The numbers are 1,2,3, which are one index greater than what can be used in the array
                } else {
                    //Based on the number the outputting is on, add the current line of text
                    allMazes[num] += lineOfText;
                }
            }
            // Close the input stream for both the FileReader and the BufferedReader 
            readFile.close();
            in.close();

            // Exception Handling: used to prevent the program from crashing if the file does not exist or is corrupted 
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        boolean validInput = false;
        System.out.print("Please input the number of the maze you would like to play: ");
        String input = s.nextLine();
        int chosenNum = -1;
        int length = allMazes.length;
        while (validInput == false) {
            //IF the users input is a number and between 0 and the length of the number of mazes in the file, then the input is valid.
            //otherwise it is not
            if (isNum(input)) {
                chosenNum = Integer.parseInt(input);
            }
            if (chosenNum > 0 && chosenNum <= length) {
                validInput = true;
            } else {
                System.out.println("This is invalid input. "
                        + "Please input something valid, which is an int between 1 and " + length + " inclusive.");
                input = s.nextLine();
            }
        }
        //Splitting up the array based on the users chosen number and the logic of dimensions
        String finalChosenMaze = allMazes[chosenNum - 1];
        String[] splitArray = finalChosenMaze.split(" ");
        //Creating a 2D array from the array we currently have
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                //"x" represents the array number based on the current x and y coordinate of the 2D array "grid"
                int x = (i * 5) + j;
                chosenMaze[i][j] = splitArray[x];
            }
        }
        return chosenMaze;
    }

    /**
     * Outputting whether or not a given String is a int or not
     *
     * @param strNum Given string
     * @return Boolean whether it is a number or not
     */
    public boolean isNum(String strNum) {
        //Using a try catch to achieve the goal
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Writing the 2D array into the playerMazes file
     *
     * @param chosenMaze 2D Array
     */
    public void writeToFile(String[][] chosenMaze) {
        playerFileLength++; //Updating the number I am on for the player Mazes
        // Create a new File (which is stored in the project)
        File dataFile = new File("playerMazes.txt");

        // Declare the BufferedWriter Object
        BufferedWriter writeFile;
        try {
            out = new FileWriter(dataFile, true); //"out" is used again in the clearFile() method, so it is global

            // Pass the FileWriter to the BufferedWriter
            writeFile = new BufferedWriter(out);
            //Writing the number of the maze we are on 
            writeFile.write(playerFileLength + "");
            writeFile.newLine();
            // //Convert 2D array to Regular String array based on row #
            for (int i = 0; i < chosenMaze.length; i++) {
                writeFile.write(convertArray(chosenMaze[i]));
                writeFile.newLine();
            }

            // Close the FileWriter and BufferedWriter stream 
            writeFile.close();
            out.close();

            // Exception: if not able to write to file
        } catch (IOException e) {
        }
    }

    /**
     * Getting player file length. Represents the number of Mazes the user has
     * chosen to save
     *
     * @return player file length
     */
    public int getPlayerFileLength() {
        return playerFileLength;
    }

    /**
     * Getting the grid of the maze. This is not the 2D array, just the
     * LinkedGrid object we had created previously
     *
     * @return LinkedGrid grid
     */
    public LinkedGrid getMaze() {
        return maze;
    }

    /**
     * Converting a String array into a readable maze structure for writing into
     * the file
     *
     * @param arr String Array to be converted
     * @return String that had been converted
     */
    public String convertArray(String[] arr) {
        String returnVal = "";
        int len = arr.length;
        //FOR each element, add a space and add it to the return value
        for (int i = 0; i < len; i++) {
            returnVal += arr[i] + " ";
        }
        return returnVal;
    }

    /**
     * Resets the current LinkedGrid back to the original
     */
    public void resetMaze() {
        //Can just recall the LinkedGrid object constructor because of the resetting of attributes in the constructor
        maze = new LinkedGrid();
    }

    /**
     * Clears the file of the current playerMazes textFile for each run
     */
    public void clearFile() {
        try {
            // Create a new FileWriter with the 'false' flag to overwrite the file
            out = new FileWriter("playerMazes.txt", false);
            out.close();
            playerFileLength = 0; // Reset the playerFileLength counter because the file no longer has anything in it
        } catch (IOException e) {
        }
    }

}
