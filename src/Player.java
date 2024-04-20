import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;

public class Player {
	
	
	public static double x;
	public static double y;
	public static int angle;
	static final int TURN_SPEED = 100;
	static final int MOVE_SPEED = 1;
	static boolean up, down, right, left;
	
	public Player() {
		x = 0;
		y = 0;
		angle = 0;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getDirX() {
		return Math.cos(Math.toRadians(angle));
	}
	public double getDirY() {
		return Math.sin(Math.toRadians(angle));
	}
	public double getPlaneX() {
		return -Math.sin(Math.toRadians(angle));
	}
	public double getPlaneY() {
		return Math.cos(Math.toRadians(angle));
	}
	public int getAngle() {
		return angle;
	}
	
	public void move(double deltaTime, Boolean up, Boolean down, Boolean right, Boolean left) {
		//System.out.println(angle);
        if (left) {
        	angle -= TURN_SPEED * deltaTime;
        }
		if (right) {
			angle += TURN_SPEED * deltaTime;
		}
		if (up) {
			x += MOVE_SPEED * deltaTime * Math.cos(angle);
			y += MOVE_SPEED * deltaTime * Math.sin(angle);			
		}
		else if (down) {
			x -= MOVE_SPEED * deltaTime * Math.cos(angle);
			y -= MOVE_SPEED * deltaTime * Math.sin(angle);			
		}
		
	}

}
