import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TitleScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button randomMazeButton = new Button("Play Randomized Maze");
        Button loadFileButton = new Button("Maze Demo");

        // Button listener
        randomMazeButton.setOnAction(e -> startRandomMaze(primaryStage));
        loadFileButton.setOnAction(e -> loadMazeFromFile(primaryStage));

        // Title
        Text title = new Text("Raycast 3D Maze");
        title.setFont(new Font(30));

        // layout container
        VBox root = new VBox(40, title, randomMazeButton, loadFileButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, RaycastMaze.SCREEN_WIDTH, RaycastMaze.SCREEN_HEIGHT);
        primaryStage.setTitle("Raycast 3D Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startRandomMaze(Stage stage) {
        stage.close();
        try {
            RaycastMaze raycastMaze = new RaycastMaze(true);
            raycastMaze.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMazeFromFile(Stage stage) {
        stage.close();
        try {
            RaycastMaze raycastMaze = new RaycastMaze(false);
            raycastMaze.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}