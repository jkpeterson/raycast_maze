import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RaycastMaze extends Application {
	
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
	public static final long LOOP_LENGTH = 17;
	private GraphicsContext gc;
	boolean up, down, right, left;
    private long lastTime = System.currentTimeMillis();
	private double deltaTime = 0;
    
    public static void gameLoop() {

    		
    }
    
    private static void sleep(long time)
    {
        if(time > 0)
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Raycaster");
        primaryStage.setScene(scene);
        primaryStage.show();
    	Player p = new Player();
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
	    		p.move(deltaTime,up,down,right,left);
	    		sleep(LOOP_LENGTH - (long)deltaTime);
			}
        	
        };
        timer.start();
		
	}
    public static void main(String[] args) {
        launch();
    }
}