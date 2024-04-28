public class Player {
	//
	// Class: Player
	//
	// Description:
	// Object that represents the player character.  Takes in the javafx inputs and performs the
	// movement calculations based on the elapsed time of the previous frame.
	// Controls are tank-like, left and right rotate the view and forward and back move.
	
	public static double x;
	public static double y;
	public static int angle;
	static final int TURN_SPEED = 100;
	static final int MOVE_SPEED = 1;
	static final int MAP_WIDTH = 24;
	static final int MAP_HEIGHT = 24;
	static boolean up, down, right, left;

	static boolean reachedExit = false;

	//
	// Player()	Constructor
	//
	// Input: Nothing
	// Output: Nothing
	// Player's default position is in the center of the maze at angle 0.
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
		//used to get the position of the camera plane
		return -Math.sin(Math.toRadians(angle));
	}
	public double getPlaneY() {
		//used to get the position of the camera plane
		return Math.cos(Math.toRadians(angle));
	}
	public int getAngle() {
		return angle;
	}
	public static void setReachedExit(boolean reachedExit) {
		Player.reachedExit = reachedExit;
	}
	//
	// setStartPosition()
	//
	// Input: starting x position
	// Output: Nothing
	// The player always starts at the top of the maze facing south.
	// The only thing that changes is the x value of the start position,
	// so this function will reset the player and place them in the given start.
	public void setStartPosition(double startX) {
		//every map has a unique starting x value, but all start at a y value of 1.5
		x = startX;
		y = 1.5;
		angle = 180;
	}
	//
	// move()
	//
	// Input: deltaTime of last frame, the current map, the javafx inputs from the keyboard
	// Output: Nothing
	// Runs calculations every single frame to change the angle, speed, and duration of the previous frame to find the
	// players new position.
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
