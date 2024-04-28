import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class RaycastMaze extends Application {

    public static int SCREEN_WIDTH = 800;
    public static int SCREEN_HEIGHT = 600;
    private Player player;
    private Render render;
    public static final long LOOP_LENGTH = 17;
    private GraphicsContext gc;
    boolean up, down, right, left;
    private long lastTime = System.currentTimeMillis();
    private long startTime;
    private long endTime;
    private double deltaTime = 0;
    private boolean isRandomMaze;
    private String selectedMap;
    private AnimationTimer timer;
    public static int[][] worldMap;
    public static boolean textures = true;

    public RaycastMaze(boolean isRandomMaze, String selectedMap) {
        this.isRandomMaze = isRandomMaze;
        this.selectedMap = selectedMap;
    }

    public static void setScreenResolution(int width, int height) {
        SCREEN_WIDTH = width;
        SCREEN_HEIGHT = height;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	//creates player, renderer, and javafx canvas
        player = new Player();
        render = new Render();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Pane root = new Pane(canvas);

        //generates a random maze, or loads the selected maze from the menu
        if (isRandomMaze) {
            // Generate a random maze
            MazeGenerator mg = new MazeGenerator();
            worldMap = mg.makeNewMaze();
            player.setStartPosition(mg.startX);
        } else {
            // Load a maze from the file
            MazeBuilder.readFile("resources/Maps/" + selectedMap + ".txt");
            worldMap = MazeBuilder.getMaze();
            player.setStartPosition(MazeBuilder.getRows()-1.5);
        }

        //exit button to title screen
        Button exitButton = backButton(primaryStage);
        root.getChildren().add(exitButton);
        
        //enables keyboard inputs
        Scene scene = new Scene(root);
        primaryStage.setTitle("Raycast 3D Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) up = true;
            if (e.getCode() == KeyCode.S) down = true;
            if (e.getCode() == KeyCode.D) right = true;
            if (e.getCode() == KeyCode.A) left = true;
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) up = false;
            if (e.getCode() == KeyCode.S) down = false;
            if (e.getCode() == KeyCode.D) right = false;
            if (e.getCode() == KeyCode.A) left = false;
        });
        startGameLoop(worldMap, primaryStage);
    }

    private Button backButton(Stage primaryStage) {
    	//performs all the necessary actions to return to the title screen menu
        Image exitIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/exit_icon.png")));
        ImageView exitImageView = new ImageView(exitIcon);
        exitImageView.setFitWidth(30);
        exitImageView.setFitHeight(30);
        exitImageView.setPreserveRatio(true);

        Button backButton = new Button();
        backButton.setGraphic(exitImageView);
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setLayoutX(SCREEN_WIDTH - 50);
        backButton.setLayoutY(10);

        backButton.setOnMouseEntered(e -> backButton.setCursor(javafx.scene.Cursor.HAND));
        backButton.setOnAction(e -> {
            stopGameLoop();
            primaryStage.close();
            new TitleScreen().start(new Stage());
        });
        return backButton;
    }

    private void startGameLoop(int[][] worldMap, Stage primaryStage) {
    	//primary game loop starts here
        Player.setReachedExit(false);
        //starts maze timer
        startTime = System.currentTimeMillis();

        timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
            	//calculates the time the previous frame took
            	//this is so movement will be independent of the framerate
                long currentTime = System.currentTimeMillis();
                deltaTime = (currentTime - lastTime) / 1_000.0;
                lastTime = currentTime;
                //clears the screen, draws the floor and ceiling to prepare for new frame
                gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                render.drawFloorAndCeiling(gc);
                //player movement happens, then needs to check if the player won the maze
                player.move(deltaTime, worldMap, up, down, right, left);
                if (Player.reachedExit) {
                    endTime = System.currentTimeMillis();
                    stopGameLoop();
                    ExitScreen.showExit(primaryStage, startTime, endTime, isRandomMaze);
                }
                //if the player didn't win the maze, we render the graphics for that frame
                //based on the new position of the player
                render.update(player, gc);
            }

        };
        timer.start();
    }

    private void stopGameLoop() {
        if (timer != null) {
            timer.stop();
        }
    }


}