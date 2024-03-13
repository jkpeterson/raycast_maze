import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MazeBuilder.readFile("resources/Maps/demoMap1.txt");
        double startX = MazeBuilder.getStartX();
        double startY = MazeBuilder.getStartY();
        int mapWidth = MazeBuilder.getRows();
        int mapHeight = MazeBuilder.getColumns();
        int[][] worldMap = MazeBuilder.getMaze();

        Player player = new Player(startX, startY);
        CameraPlane cameraPlane = new CameraPlane(player);
        Render render = new Render(player, cameraPlane, worldMap, mapWidth, mapHeight, SCREEN_WIDTH, SCREEN_HEIGHT);
        render.start(primaryStage);
    }
}