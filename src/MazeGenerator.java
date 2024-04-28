import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
public class MazeGenerator {
	
	final int WIDTH = 24;
	final int HEIGHT = 24;
	private int[][] baseMap = new int[WIDTH][HEIGHT];
	public double startX;
	private int exitX;
	
	public MazeGenerator() {
		for(int i = 0; i < WIDTH; i++) {
			for(int j = 0; j < HEIGHT; j++) {
				baseMap[i][j] = 1;
			}
		}
	}

	public int[][] makeNewMaze() {
		//generates mazes using prim's maze algorithm
		Random rand = new Random();
		//generates a random location along the second row of the maze
		//this will be the starting point
		int currX = rand.nextInt(WIDTH);
		int currY = 1;
		startX = (double)currX;
		//creates a map from walls of the maze to cells of the maze
		HashMap<int[], int[]> cellsToCheck = new HashMap<int[], int[]>();
		cellsToCheck.put(new int[]{currX, currY}, new int[] {currX, currY});
		while(!cellsToCheck.isEmpty()) {
			//picks a random cell/wall combination
			ArrayList<int[]> walls = new ArrayList<int[]>(cellsToCheck.keySet());
	        final int[] wall = walls.remove(rand.nextInt(walls.size()));
	        final int[] cell = cellsToCheck.get(wall);
	        cellsToCheck.remove(wall);
	        currX = cell[0];
	        currY = cell[1];
	        //if that cell is currently a solid block, it empties it
	        //it will then add all of it's surrounding walls to the map as a key
	        //with the cell that wall is blocking off as the value
	        //but only if that cell is also solid
	        if ( baseMap[currX][currY] == 1 )
	        {
	            baseMap[wall[0]][wall[1]] = 0;
	            baseMap[currX][currY] = 0;
	            if ( currX >= 2 && baseMap[currX-2][currY] == 1 ) {
	            	cellsToCheck.put(new int[]{currX-1,currY},new int[] {currX-2,currY});
	            }
	            if ( currY >= 2 && baseMap[currX][currY-2] == 1 ) {
	            	cellsToCheck.put(new int[]{currX,currY-1}, new int[] {currX,currY-2});
	            }   
	            if ( currX < WIDTH-2 && baseMap[currX+2][currY] == 1 ) {
	            	cellsToCheck.put(new int[]{currX+1,currY}, new int[] {currX+2,currY});
	            }   
	            if ( currY < HEIGHT-2 && baseMap[currX][currY+2] == 1 ) {
	            	cellsToCheck.put(new int[]{currX,currY+1}, new int[] {currX,currY+2});
	            }
	                
	        }
		}
		//eventually the algorithm will run out of cell/wall combinations that fit the above criteria
		//we then fill in the outer edges of the map so the player can't enter the void
		baseMap = fillEdges(baseMap);
		//picks a random empty spot in the last row of the map to be the exit
		for(int x = 0; x < WIDTH; x++) {
			if (baseMap[x][HEIGHT-2] == 0) {
				exitX = x;
			}
		}
		baseMap[(int)exitX][HEIGHT-2] = 2;
		//prints and returns the maze
		printMaze();
		return baseMap;
	}
	public int[][] fillEdges(int[][] map) {
		for(int x = 0; x < WIDTH; x++) {
			map[x][0] = 1;
			map[x][HEIGHT-1] = 1;
		}
		for(int y = 0; y < HEIGHT; y++) {
			map[WIDTH-1][y] = 1;
			map[0][y] = 1;
		}
		return map;
	}
	public void printMaze() {
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				System.out.print(baseMap[x][y]);
			}
			System.out.println();
		}
	}
}
