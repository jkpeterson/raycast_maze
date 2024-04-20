import java.util.Random;
import java.util.LinkedList;
public class MazeGenerator {
	
	final int WIDTH = 24;
	final int HEIGHT = 24;
	private int[][] baseMap = new int[WIDTH][HEIGHT];
	
	public MazeGenerator() {
		for(int i = 0; i < WIDTH; i++) {
			for(int j = 0; j < HEIGHT; j++) {
				baseMap[i][j] = 1;
			}
		}
	}

	public void makeNewMaze() {
		Random rand = new Random();
		int currX = rand.nextInt(WIDTH);
		int currY = rand.nextInt(HEIGHT);
		LinkedList<int[]> cellsToCheck = new LinkedList<>();
		cellsToCheck.add(new int[]{currX, currY, currX, currY});
		while(!cellsToCheck.isEmpty()) {
	        final int[] f = cellsToCheck.remove( rand.nextInt( cellsToCheck.size() ) );
	        currX = f[2];
	        currY = f[3];
	        if ( baseMap[currX][currY] == 1 )
	        {
	            baseMap[f[0]][f[1]] = 0;
	            baseMap[currX][currY] = 0;
	            if ( currX >= 2 && baseMap[currX-2][currY] == 1 ) {
	            	cellsToCheck.add( new int[]{currX-1,currY,currX-2,currY} );
	            }
	            if ( currY >= 2 && baseMap[currX][currY-2] == 1 ) {
	            	cellsToCheck.add( new int[]{currX,currY-1,currX,currY-2} );
	            }   
	            if ( currX < WIDTH-2 && baseMap[currX+2][currY] == 1 ) {
	            	cellsToCheck.add( new int[]{currX+1,currY,currX+2,currY} );
	            }   
	            if ( currY < HEIGHT-2 && baseMap[currX][currY+2] == 1 ) {
	            	cellsToCheck.add( new int[]{currX,currY+1,currX,currY+2} );
	            }
	                
	        }
		}
		for(int x = 0; x < WIDTH; x++) {
			baseMap[x][0] = 1;
			baseMap[x][HEIGHT-1] = 1;
		}
		for(int y = 0; y < HEIGHT; y++) {
			baseMap[WIDTH-1][y] = 1;
			baseMap[0][y] = 1;
		}
		printMaze();

		
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
