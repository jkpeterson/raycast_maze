import java.awt.event.KeyEvent;
//import javafx.scene.input.KeyEvent;

public class Player {
	
	
	public static double x;
	public static double y;
	public static int angle;
	static final int TURN_SPEED = 1;
	static final int MOVE_SPEED = 1;
	
	public Player() {
		x = 0;
		y = 0;
		angle = 0;
	}
	
	public void move(long delta) {
		//need to figure out how to use javafx for input
		//on input left:
		angle -= TURN_SPEED * delta;
		//on input right:
		angle += TURN_SPEED * delta;
		//on input forward:
		x += MOVE_SPEED * delta * Math.cos(angle);
		y += MOVE_SPEED * delta * Math.sin(angle);
		//on input backward:
		x -= MOVE_SPEED * delta * Math.cos(angle);
		y -= MOVE_SPEED * delta * Math.sin(angle);
		
	}
}
