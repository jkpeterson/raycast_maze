import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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

    private static void sleep(long time) {
        if (time > 0)
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

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

        player = new Player();
        render = new Render();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Pane root = new Pane(canvas);

        int[][] worldMap;
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

        // Exit button to Title Screen
        Button exitButton = backButton(primaryStage);
        root.getChildren().add(exitButton);

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
        Player.setReachedExit(false);
        startTime = System.currentTimeMillis();

        timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                long currentTime = System.currentTimeMillis();
                deltaTime = (currentTime - lastTime) / 1_000.0;
                lastTime = currentTime;
                //do stuff
                gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                render.drawFloorAndCeiling(gc);
                player.move(deltaTime, worldMap, up, down, right, left);
                if (Player.reachedExit) {
                    endTime = System.currentTimeMillis();
                    primaryStage.close();
                    showExit();
                    stopGameLoop();
                }
                render.update(player, worldMap, gc);
                sleep(LOOP_LENGTH - (long) deltaTime);
            }

        };
        timer.start();
    }

    private void stopGameLoop() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void showExit() {
        ExitScreen.showExit(startTime, endTime);
    }

}