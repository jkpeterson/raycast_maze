import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MazeBuilder {
    //Number of rows, columns, and starting position in the maze.
    private static int rows, columns;
    //2D array representing the maze.
    private static int[][] maze;

    /**
     * Reads map data from a txt file and populates the maze array.
     *
     * @param filename the location of the file to read.
     * @throws FileNotFoundException If the specified file is not found.
     */
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
