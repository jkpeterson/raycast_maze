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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
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
        Button guideButton = textButton("Guide");
        Button quitButton = textButton("Quit Game");

        // Button listeners
        playButton.setOnAction(e -> showPlay());
        optionsButton.setOnAction(e -> showOptions());
        creditsButton.setOnAction(e -> showCredits());
        guideButton.setOnAction(e -> showGuide());

        quitButton.setOnAction(e -> primaryStage.close());

        // Title
        Text title = new Text("Raycast 3D Maze\n");
        title.setFont(new Font(35));
        title.setStyle("-fx-fill: white;");

        // Layout container
        mainMenu = new VBox(15, title, playButton, optionsButton, creditsButton, guideButton, quitButton);
        mainMenu.setAlignment(Pos.CENTER);

        root = new BorderPane(mainMenu);

        // Load background image
        Image backgroundImage = new Image("Images/title_background.png");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        root.setBackground(new Background(background));

        Scene scene = new Scene(root, 800, 600);
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
    	//starts the game loop while telling it to generate a maze randomly
        try {
            RaycastMaze raycastMaze = new RaycastMaze(true, "");
            raycastMaze.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMazeFromFile(String mapName) {
    	//starts the game loop while passing in the map to load
        try {
            RaycastMaze raycastMaze = new RaycastMaze(false, mapName);
            raycastMaze.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPlay() {
    	//when the play button is pressed, it shows a list of maps to play,
    	//as well as the option for a random map
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

        VBox playPane = new VBox(20, randomMazeButton, mapButtons, backButton);
        playPane.setAlignment(Pos.CENTER);
        root.setCenter(playPane);
    }

    private String selectedResolution = "Select";
    private void showOptions() {
    	//shows the options menu
    	//the player can select the resolution and if they want textured graphics
        Text optionsTitle = new Text("Options");
        optionsTitle.setFont(new Font(25));
        optionsTitle.setStyle("-fx-fill: white;");
        Text resolutionLabel = new Text("Resolution");
        resolutionLabel.setFont(new Font(15));
        resolutionLabel.setStyle("-fx-fill: #CCCCCC;");
        List<String> resolutions = Arrays.asList("800x600", "1024x768", "1280x720", "1920x1080", "2560x1440");
        ObservableList<String> options = FXCollections.observableArrayList(resolutions);
        ComboBox<String> resolutionComboBox = new ComboBox<>(options);
        resolutionComboBox.setValue(selectedResolution);

        Button applyButton = new Button("Apply");
        applyButton.setOnAction(e -> {
            selectedResolution = resolutionComboBox.getValue();
            if (!selectedResolution.equals("Select")) {
                String[] parts = selectedResolution.split("x");
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                RaycastMaze.setScreenResolution(width, height);
            }
        });
        ToggleButton textureButton = new ToggleButton("Textures");
        //ensures the toggle box reflects the current value
        textureButton.setSelected(RaycastMaze.textures);
        textureButton.setOnAction(e -> {
        	RaycastMaze.textures = !RaycastMaze.textures;
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> root.setCenter(mainMenu));

        VBox optionsPane = new VBox(20);
        optionsPane.getChildren().addAll(optionsTitle, resolutionLabel, resolutionComboBox, textureButton, applyButton, backButton);
        optionsPane.setAlignment(Pos.CENTER);
        root.setCenter(optionsPane);
    }

    private void showGuide() {
    	//shows basic instructions for the player
        Text creditsText = new Text("Guide:\n\nMovement Controls:\nW: Move Forward\nS: Move Backward\nCamera Rotation Controls:\nA: Rotate Camera Left\nD: Rotate Camera Right\nGameplay Tips:\nNavigate through the maze cautiously, pay attention to details in the environment.");
        creditsText.setFont(new Font(15));
        creditsText.setStyle("-fx-fill: white;");


        Button backButton = new Button("Back");
        backButton.setOnAction(e -> root.setCenter(mainMenu));

        VBox creditsPane = new VBox(20, creditsText, backButton);
        creditsPane.setAlignment(Pos.CENTER);
        root.setCenter(creditsPane);
    }

    private void showCredits() {
    	//shows the developer credits
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