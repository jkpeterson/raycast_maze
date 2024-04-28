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
		//creates a hashmap from walls of the maze to cells of the maze
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
	        //if that cell is currently a solid block, it empties it and its connecting wall
	        //it will then check for all the valid wall/cell combos around that cell and add them to the hashmap
	        if ( baseMap[currX][currY] == 1 )
	        {
	            baseMap[wall[0]][wall[1]] = 0;
	            baseMap[currX][currY] = 0;
	            cellsToCheck.putAll(getSurroundingWalls(currX, currY));
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
	HashMap<int[],int[]> getSurroundingWalls(int x, int y) {
		//a wall/cell combo is considered valid if the cell is currently a solid block
		//and the wall is not on an edge of the map
		HashMap<int[], int[]> surroundingWalls = new HashMap<int[], int[]>();
        if ( x >= 2 && baseMap[x-2][y] == 1 ) {
        	surroundingWalls.put(new int[]{x-1,y},new int[] {x-2,y});
        }
        if ( y >= 2 && baseMap[x][y-2] == 1 ) {
        	surroundingWalls.put(new int[]{x,y-1}, new int[] {x,y-2});
        }   
        if ( x < WIDTH-2 && baseMap[x+2][y] == 1 ) {
        	surroundingWalls.put(new int[]{x+1,y}, new int[] {x+2,y});
        }   
        if ( y < HEIGHT-2 && baseMap[x][y+2] == 1 ) {
        	surroundingWalls.put(new int[]{x,y+1}, new int[] {x,y+2});
        }
        return surroundingWalls;
	}
	int[][] fillEdges(int[][] map) {
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
