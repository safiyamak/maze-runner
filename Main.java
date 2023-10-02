package safiya.summative;

import java.util.Scanner;

public class Main {

    //These are global since they are used multiple times in different functions
    private static Scanner s = new Scanner(System.in);
    private static String[][] game;
    private static int rowCoord = 0;
    private static int colCoord = 0;

    public static void main(String[] args) {
        System.out.println("Welcome to the Maze game!");
        boolean endGame = false;
        boolean wonMaze = false;
        boolean random = false;
        String choice;
        Maze main = new Maze();
        main.clearFile();
        LinkedGrid grid = main.getMaze();
        game = null; //Will be initialized later based on the users input of random or generated maze.
        int length = 5; //Based on my code, all of the mazes are dimension of 5.
        boolean inputtedMaze = false;

        //WHILE: The user has not chosen to end the game, continue playing.
        while (endGame == false) {
            wonMaze = false;
            random = false;
            //IF the user has inputted a maze into the playerMazes file, then we can ask them if they would like to recall it
            if (inputtedMaze) {
                System.out.println("Would you like a random, pre generated maze or user generated maze?");
                System.out.print("Please input \"r\", \"g\", or \"u\" for short: ");
                //Getting the user input of random, generated, or user generated maze.
                choice = getInput("r", "g", "u");
            } else {
                System.out.println("Would you like a random or pre generated maze?");
                System.out.print("Please input \"r\" or \"g\" for short: ");
                //Getting the user input of random or generated maze.
                choice = getInput("r", "g");
            }

            //IF they chose a random, generated, or user generated maze
            if (choice.equals("r")) {
                main.generateMaze(grid.getFirst()); //Using recursive generateMaze call from Maze class
                game = convertToArray(grid); //converting to 2D array for playing the game and printing.
                //I decided to play the game through 2D array since when inputting from the file, I convert to that
                //Therefore it is more efficient to make all the games playable through the same data structure.
                random = true;
            } else if (choice.equals("g")) {
                //Calling the outputMazes method that reads in a textFile and converts it to a 2D array
                game = main.outputMazes("mazes.txt");
            } else {
                game = main.outputMazes("playerMazes.txt");
            }
            //WHILE: The user has not won the maze
            while (wonMaze == false) {
                //Resetting the user moves
                rowCoord = 0;
                colCoord = 0;

                System.out.println("Please input a list of your inputs, starting from the top left corner. Please seperate with a \",\" and only use the beginning of each turn like \"l\" or \"r\"");
                System.out.println("Here is an example of input: l,r,r,d,u,r");
                System.out.println("The \"X\" spots represent a wall where you cannot enter. The \"E\" represents your goal.");
                displayArray(game, length); //Displaying the game

                String[] directionsToComputer = getUserDirections(); //Getting the user input and converting it to an array
                boolean validMove = true;
                //Go through every element in the users input and check if it's a valid move
                for (int i = 0; i < directionsToComputer.length; i++) {
                    validMove = move(directionsToComputer[i], length);
                    if (validMove == false) { //IF it's not a valid move, immediately end the game
                        break;
                    }
                }
                String landingCell = game[rowCoord][colCoord]; //The data in the cell, and whether it's a wall, goal, or random spot. rowCoord represents x coordinate and colCoord represents y coordinate.
                //IF they reached the end goal without hitting a boundary
                if (landingCell.equals("E") && validMove != false) {
                    System.out.println("You won! Good job.");

                    game[rowCoord][colCoord] = "Y"; //Represents "You" or the player
                    displayArray(game, length);

                    wonMaze = true;
                    game[rowCoord][colCoord] = landingCell; //Resetting the maze for potential writing to the file so they can replay it
                    //IF they hit a wall, boundary, or landed in a random spot
                } else if (landingCell.equals(".") || validMove == false) {
                    game[rowCoord][colCoord] = "Y";

                    System.out.println("Your input is wrong! You can view where you landed below. Please try again until you win.");
                    System.out.println("Even though it may appear you are on the winning spot, it just means that you went beyond the boundary.");

                    displayArray(game, length);
                    game[rowCoord][colCoord] = landingCell; //"Resetting" the maze, basically putting the original cell back into it's place before the next run
                }
            }
            //IF they were using a recursively generated maze
            if (random) {

                System.out.println("Would you like to save this maze to play again later?");
                System.out.print("Please input \"y\" or \"n\": ");

                choice = getInput("y", "n"); //Getting their input using input() method that takes in two comparators and gets user input to one or the other.

                if (choice.equals("y")) { //IF they want to save it
                    main.writeToFile(game); //Input the 2D array
                    inputtedMaze = true;

                    System.out.println("Great, it is saved!");
                }
            }

            System.out.println("Would you like to play again?");
            System.out.print("Please input \"y\" or \"n\": ");

            choice = getInput("y", "n");

            if (choice.equals("n")) { //IF they don't want to play again, end game.
                System.out.println("Thanks for playing!");
                endGame = true;
            }
        }

    }

    /**
     * Outputs a 2D array in a user friendly format
     *
     * @param array The array that is outputted
     * @param dimension the dimension of the array
     */
    public static void displayArray(String[][] array, int dimension) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println("");
        }

    }

    /**
     * Converting a LinkedGrid to 2D Array based on whether it is a wall, empty
     * space, or goal.
     *
     * @param s The LinkedGrid object that needs to be converted
     * @return The 2D array
     */
    public static String[][] convertToArray(LinkedGrid s) {
        Node temp = s.getFirst();
        Node rowMarker = s.getFirst();
        String[][] output = new String[s.getDimension()][s.getDimension()]; //2D array to output

        //FOR every row
        for (int i = 0; i < s.getDimension(); i++) {
            //FOR every column
            for (int j = 0; j < s.getDimension(); j++) {
                //Now we are on the individual cell
                if (temp.equals(s.getLast())) { //Using overwritten .equals method from Node class
                    output[i][j] = "E";
                } else if (temp.getOpen() == true) {
                    output[i][j] = ".";
                } else if (temp.getOpen() == false) {
                    output[i][j] = "X";
                }
                temp = temp.getRight(); //Going to the next Node
            }
            //Going to the Node below
            temp = rowMarker.getDown();
            rowMarker = temp;
        }
        return output;
    }

    /**
     * Getting the valid user input when given a choice between two comparators
     *
     * @param comparator1 The first comparator
     * @param comparator2 The second comparator
     * @return Which one they chose
     */
    public static String getInput(String comparator1, String comparator2) {
        String input = s.nextLine(); //First input
        boolean validInput = false;
        String chosenInput = "";
        //WHILE they haven't inputted something valid
        while (validInput == false) {
            input = input.toLowerCase(); //allows the input to not be case sensitive
            if (input.equals(comparator1)) {
                validInput = true;
                chosenInput = comparator1;
            } else if (input.equals(comparator2)) {
                validInput = true;
                chosenInput = comparator2;
            } else { //They didn't input something valid
                System.out.println("Please input a valid value, either " + comparator1 + " or " + comparator2);
                input = s.nextLine();
            }
        }
        return chosenInput;
    }

    /**
     * Getting the valid user input when given a choice between three
     * comparators
     *
     * @param comparator1 1st comparator
     * @param comparator2 2nd comparator
     * @param comparator3 3rd comparator
     * @return Which one they chose
     */
    public static String getInput(String comparator1, String comparator2, String comparator3) {
        String input = s.nextLine();
        boolean validInput = false;
        String chosenInput = "";
        while (validInput == false) {
            input = input.toLowerCase();
            if (input.equals(comparator1)) {
                validInput = true;
                chosenInput = comparator1;
            } else if (input.equals(comparator2)) {
                validInput = true;
                chosenInput = comparator2;
            } else if (input.equals(comparator3)) { //The only way this differs from the last method is that we added a third comparator
                validInput = true;
                chosenInput = comparator3;
            } else {
                System.out.println("Please input a valid value, either " + comparator1 + ", " + comparator2 + ", or " + comparator3);
                input = s.nextLine();
            }
        }
        return chosenInput;
    }

    /**
     * Getting the user directions to the computer to traverse the maze
     *
     * @return The user directions in an array format
     */
    public static String[] getUserDirections() {
        String[] returnArr; //Array to be returned
        String input = s.nextLine();
        input = input.toLowerCase();
        returnArr = input.split(","); //based on the directions in the console, the user must split put "," between each direction
        //Therefore we split it based on this direction
        boolean validInput = false;
        //WHILE the user hasn't inputted something valid
        while (validInput == false) {
            //IF the user inputted an empty array or one with only commas
            if (returnArr.length == 0) {
                validInput = false;
                System.out.println("This is incorrect. Please input valid directions.");
                input = s.nextLine();
                returnArr = input.split(",");
            } else {
                //Go through each element of the array and check if it's a direction, if it's not, it must be incorrect
                for (int i = 0; i < returnArr.length; i++) {
                    if (returnArr[i].equals("l")) {
                        validInput = true;
                    } else if (returnArr[i].equals("r")) {
                        validInput = true;
                    } else if (returnArr[i].equals("u")) {
                        validInput = true;
                    } else if (returnArr[i].equals("d")) {
                        validInput = true;
                    } else {
                        validInput = false;

                        System.out.println("This is incorrect. Please input valid directions.");

                        input = s.nextLine();
                        returnArr = input.split(",");
                        break;
                    }
                }
            }

        }

        return returnArr;
    }

    /**
     * Moving based on user input, and then checking if that move is valid based
     * on the boundaries and walls of the maze
     *
     * @param move The move they want to do
     * @param dimension The "boundaries" of the maze. Mostly the right, up, and
     * down boundaries
     * @return Whether or not the move was valid
     * 
     * BIG O NOTATION HERE
     */
    public static boolean move(String move, int dimension) {
        if (move.equals("l")) {
            colCoord--;
        } else if (move.equals("r")) {
            colCoord++;
        } else if (move.equals("u")) {
            rowCoord--;
        } else if (move.equals("d")) {
            rowCoord++;
        }
        //IF they went beyond the left or right boundary
        if (colCoord < 0) { //Left boundary
            System.out.println("You went beyond the left dimension!");
            colCoord = 0; //Resetting the colCoord for printing later
            return false;
        } else if (colCoord >= dimension) { //Right boundary
            System.out.println("You went beyond the right dimension!");
            colCoord = dimension - 1;
            return false;
        }
        //IF they went beyond the up or down boundary
        if (rowCoord < 0) { //Up boundary
            System.out.println("You went beyond the top dimension!");
            rowCoord = 0;
            return false;
        } else if (rowCoord >= dimension) { //Down boundary
            System.out.println("test1");
            System.out.println("You went beyond the bottom dimension!");
            rowCoord = dimension - 1;
            return false;
        }

        String currentCell = game[rowCoord][colCoord]; //rowCoord and colCoord work as x and y coordinates of the 2D array "grid"
        //Checking if the user landed on a wall
        if (currentCell.equals("X")) {
            System.out.println("You hit a wall!");
            return false;
        }
        return true;
        
        /*
        BIG O NOTATION:
        
        This is O(1). In this function, it is a series of if statements, which does
        not involve any loops that could cause it to need to do a certain action multiple times.
        */
    }

}
