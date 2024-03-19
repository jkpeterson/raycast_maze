import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MazeBuilder {
     //Number of rows and columns in the maze.
    public static int rows, columns;
    //2D array representing the maze.
    public static int[][] maze;

    /**
     * Reads map data from a txt file and populates the maze array.
     *
     * @param filename the location of the file to read.
     * @throws FileNotFoundException If the specified file is not found.
     */
    public static void readFile(String filename) {
        try {
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
            // print maze
            for (int a = 0; a < rows; a++) {
                for (int b = 0; b < columns; b++) {
                    System.out.print(" " + maze[a][b]);
                }
                System.out.println();
            }
            System.out.println(filename + " generated");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
