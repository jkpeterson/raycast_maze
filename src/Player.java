import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;

public class Player {
	
	
	public static double x;
	public static double y;
	public static int angle;
	static final int TURN_SPEED = 100;
	static final int MOVE_SPEED = 1;
	static final int MAP_WIDTH = 24;
	static final int MAP_HEIGHT = 24;
	static boolean up, down, right, left;

	static boolean reachedExit = false;

	public Player() {
		x = 12;
		y = 12;
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
	public static void setReachedExit(boolean reachedExit) {
		Player.reachedExit = reachedExit;
	}
	public void setStartPosition(double startX) {
		//every map has a unique starting x value, but all start at a y value of 1.5
		x = startX;
		y = 1.5;
		angle = 180;
	}
	public void move(double deltaTime, int[][] worldMap, Boolean up, Boolean down, Boolean right, Boolean left) {
		//pulls in keyboard inputs from the javafx canvas
		//left and right simply change the camera angle based on the turn speed and millis
		//of the previous frame
        if (left) {
        	angle -= TURN_SPEED * deltaTime;
        	if(angle < 0) {
        		angle += 360;
        	}
        }
		if (right) {
			angle += TURN_SPEED * deltaTime;
			if(angle >= 360) {
				angle -= 360;
			}
		}
		//up and down change the position of the player based on their angle,
		//move speed, and millis of the previous frame
		//this also detects if the new tile they would enter is walkable (either 0 which is air, or 2 which is the exit)
		if (up) {
            double newX = x + (MOVE_SPEED * deltaTime * getDirX());
            double newY = y + (MOVE_SPEED * deltaTime * getDirY());
			if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT && (worldMap[(int) newX][(int) newY] == 0 || worldMap[(int) newX][(int) newY] == 2)) {
				x = newX;
				y = newY;
			}
		}
		else if (down) {
            double newX = x - (MOVE_SPEED * deltaTime * getDirX());
            double newY = y - (MOVE_SPEED * deltaTime * getDirY());
			if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT && (worldMap[(int) newX][(int) newY] == 0 || worldMap[(int) newX][(int) newY] == 2)) {
				x = newX;
				y = newY;
			}
		}
		//if the player is inside the exit tile, the end screen is triggered
		if (worldMap[(int)x][(int)y] == 2) {
			reachedExit = true;
		}
		
	}

}
