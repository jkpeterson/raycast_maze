import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Render extends Application {

    // Size of the grid / how many cells per row/column
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;

    // Size of each cell
    private static final double CELL_WIDTH = 50;
    private static final double CELL_HEIGHT = 50;

    /**
     * Entry point for the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        // Create a Canvas to render the grid
        Canvas canvas = new Canvas(GRID_WIDTH * CELL_WIDTH, GRID_HEIGHT * CELL_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the grid
        drawGrid(gc);

        // Group to hold the Canvas
        Group root = new Group();
        root.getChildren().add(canvas);

        // Create the Scene
        Scene scene = new Scene(root);

        // Set up the Stage
        primaryStage.setTitle("Basic Render Engine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Draw the grid on the canvas.
     *
     * @param gc the graphics context of the canvas
     */
    private void drawGrid(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                // Set the color grid cell
                    gc.setFill(javafx.scene.paint.Color.WHITE);

                // Draw the grid cell
                gc.fillRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

                // Draw grid lines
                gc.setStroke(javafx.scene.paint.Color.BLACK);
                gc.strokeRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
