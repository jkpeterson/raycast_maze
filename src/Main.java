public class Main {
	
	
	public static final long LOOP_LENGTH = 17;
	
	
	public static void main(String[] args) {
        //String filename = "resources/Maps/demoMap.txt";
        //MazeBuilder.readFile(filename);
		//System.out.println(System.getProperty("javafx.runtime.version"));
        //gameLoop();
    }
    
    public static void gameLoop() {
    	long delta = 0;
    	Player p = new Player();
    	while(true) {
    		final long startTime = System.currentTimeMillis();
    		//do stuff
    		p.move(delta);
    		delta = (System.currentTimeMillis() - startTime);
    		sleep(LOOP_LENGTH - delta);
    		System.out.println(delta);
    	}
    		
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
}
