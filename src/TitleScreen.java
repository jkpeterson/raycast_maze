import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class TitleScreen extends Application {

    private Stage primaryStage;
    private BorderPane root;
    private VBox mainMenu;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Button playButton = textButton("Play");
        Button optionsButton = textButton("Options");
        Button creditsButton = textButton("Credits");
        Button quitButton = textButton("Quit Game");

        // Button listeners
        playButton.setOnAction(e -> showPlay());
        creditsButton.setOnAction(e -> showCredits());
        optionsButton.setOnAction(e -> showOptions());
        quitButton.setOnAction(e -> primaryStage.close());

        // Title
        Text title = new Text("Raycast 3D Maze\n");
        title.setFont(new Font(35));
        title.setStyle("-fx-fill: white;");

        // Layout container
        mainMenu = new VBox(20, title, playButton, optionsButton, creditsButton, quitButton);
        mainMenu.setAlignment(Pos.CENTER);

        root = new BorderPane(mainMenu);

        // Load background image
        Image backgroundImage = new Image("title_background.png");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        root.setBackground(new Background(background));

        Scene scene = new Scene(root, RaycastMaze.SCREEN_WIDTH, RaycastMaze.SCREEN_HEIGHT);
        primaryStage.setTitle("Raycast 3D Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button textButton(String buttonText) {
        Button button = new Button(buttonText);
        button.setStyle("-fx-text-fill: #CCCCCC; -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px;");
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 18px;");
            button.setCursor(javafx.scene.Cursor.HAND);
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-text-fill: #CCCCCC; -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px;");
            button.setCursor(javafx.scene.Cursor.DEFAULT);
        });
        return button;
    }

    private void startRandomMaze() {
        try {
            RaycastMaze raycastMaze = new RaycastMaze(true, "");
            raycastMaze.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMazeFromFile(String mapName) {
        try {
            RaycastMaze raycastMaze = new RaycastMaze(false, mapName);
            raycastMaze.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPlay() {
        List<String> mapNames = Arrays.asList("demoMap", "level1", "level2");
        VBox mapButtons = new VBox(20);
        mapButtons.setAlignment(Pos.CENTER);
        for (String mapName : mapNames) {
            Button mapButton = textButton(mapName);
            mapButton.setOnAction(e -> loadMazeFromFile(mapName));
            mapButtons.getChildren().add(mapButton);
        }

        Button randomMazeButton = textButton("Randomized Maze");
        Button backButton = new Button("Back");

        randomMazeButton.setOnAction(e -> startRandomMaze());
        backButton.setOnAction(e -> root.setCenter(mainMenu));

        VBox creditsPane = new VBox(20, randomMazeButton, mapButtons, backButton);
        creditsPane.setAlignment(Pos.CENTER);

        root.setCenter(creditsPane);
    }

    private void showOptions() {
        List<String> resolutions = Arrays.asList("800x600", "1024x768", "1280x720", "1920x1080");
        VBox resolutionButtons = new VBox(20);
        resolutionButtons.setAlignment(Pos.CENTER);
        for (String resolution : resolutions) {
            Button resolutionButton = textButton(resolution);
            resolutionButton.setOnAction(e -> {
                //change resolution
            });
            resolutionButtons.getChildren().add(resolutionButton);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> root.setCenter(mainMenu));

        VBox optionsPane = new VBox(20);
        optionsPane.getChildren().addAll(resolutionButtons, backButton);
        optionsPane.setAlignment(Pos.CENTER);

        root.setCenter(optionsPane);
    }


    private void showCredits() {
        Text creditsText = new Text("Credits:\n");
        creditsText.setFont(new Font(20));
        creditsText.setStyle("-fx-fill: white;");

        Font italicFont = Font.font("Arial", FontPosture.ITALIC, 16);
        Text developers = new Text("Developed by:\nJulia Peterson\nRaymond Chen");
        developers.setFont(italicFont);
        developers.setStyle("-fx-fill: white;");

        Text version = new Text("Version 0.0.1");
        version.setFont(italicFont);
        version.setStyle("-fx-fill: white;");

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> root.setCenter(mainMenu));

        VBox creditsPane = new VBox(20, creditsText, developers, version, backButton);
        creditsPane.setAlignment(Pos.CENTER);

        root.setCenter(creditsPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}