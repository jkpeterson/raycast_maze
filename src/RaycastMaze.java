import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RaycastMaze extends Application {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    private Player player;
    private Render render;
	public static final long LOOP_LENGTH = 17;
	private GraphicsContext gc;
	boolean up, down, right, left;
    private long lastTime = System.currentTimeMillis();
	private double deltaTime = 0;
    private boolean isRandomMaze;

    private static void sleep(long time)
    {
        if(time > 0)
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public RaycastMaze(boolean isRandomMaze) {
        this.isRandomMaze = isRandomMaze;
    }

	@Override
	public void start(Stage primaryStage) throws Exception {

        int[][] worldMap;
        if (isRandomMaze) {
            // Generate a random maze
            MazeGenerator mg = new MazeGenerator();
            worldMap = mg.makeNewMaze();
        } else {
            // Load a maze from the file
            MazeBuilder.readFile("resources/Maps/demoMap.txt");
            worldMap = MazeBuilder.getMaze();
        }

        player = new Player();
        render = new Render();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Pane root = new Pane(canvas);
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
        AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long arg0) {
				long currentTime = System.currentTimeMillis();
                deltaTime = (currentTime - lastTime) / 1_000.0;
                lastTime = currentTime;
	    		//do stuff
        		gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                render.drawFloorAndCeiling(gc);
	    		player.move(deltaTime,worldMap,up,down,right,left);
	    		render.update(player, worldMap, gc);
	    		sleep(LOOP_LENGTH - (long)deltaTime);
			}

        };
        timer.start();

	}

}