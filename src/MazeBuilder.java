import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MazeBuilder {
	//
	// Class: MazeBuilder
	//
	// Description:
	// Object that builds a maze from a file, used for level maps that will be consistent
	// between copies of the game.
    private static int rows, columns;
    //2D array representing the maze.
    private static int[][] maze;

	//
	// readFile()
	//
	// Input: filename of the map to build
	// Output: Nothing
	// Uses a scanner to read in the provided file of that filename.
    // builds the map as a 2D array of ints and stores it in its maze variable.
    public static void readFile(String filename) {
        try {
        	//opens the file and reads it line by line as an array of ints
        	//the game uses as map data
            FileReader file = new FileReader(filename);
            Scanner input = new Scanner(file);
            rows = input.nextInt();
            columns = input.nextInt();
            maze = new int[rows][columns];
            for (int a = 0; a < rows; a++) {
                for (int b = 0; b < columns; b++) {
                    maze[a][b] = input.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
	//
	// printMaze()
	//
	// Input: Nothing
	// Output: Nothing
	// Prints out the maze currently stored in its maze variable
    public static void printMaze() {
        // print maze
        for (int a = 0; a < rows; a++) {
            for (int b = 0; b < columns; b++) {
                System.out.print(" " + maze[a][b]);
            }
            System.out.println();
        }
    }

    public static int[][] getMaze() {
        return maze;
    }
    public static int getRows() {
        return rows;
    }
    public static int getColumns() {
        return columns;
    }
}
