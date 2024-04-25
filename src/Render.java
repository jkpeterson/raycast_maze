import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Render {
	BufferedImage[] textures = new BufferedImage[3];
	Color[][] screen = new Color[RaycastMaze.SCREEN_WIDTH][RaycastMaze.SCREEN_HEIGHT];
	int texHeight = 128;
    int texWidth = 128;
	
	public Render() {
		try {
			BufferedImage wall = ImageIO.read(new File("resources/Textures/brick_mossy.png"));
			BufferedImage door = ImageIO.read(new File("resources/Textures/door.png"));
			textures[1] = wall;
			textures[2] = door;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(Player player, int[][] worldMap, GraphicsContext gc) {
		PixelWriter pw = gc.getPixelWriter();
		screen = new Color[RaycastMaze.SCREEN_WIDTH][RaycastMaze.SCREEN_HEIGHT];
		for (int x = 0; x < RaycastMaze.SCREEN_WIDTH; x++) {
            double cameraX = 2 * x / (double) RaycastMaze.SCREEN_WIDTH - 1;
            double rayDirX = player.getDirX() + player.getPlaneX() * cameraX;
            double rayDirY = player.getDirY() + player.getPlaneY() * cameraX;

            int mapX = (int) player.getX();
            int mapY = (int) player.getY();

            double sideDistX;
            double sideDistY;

            double deltaDistX = (rayDirX == 0) ? Double.POSITIVE_INFINITY : Math.abs(1 / rayDirX);
            double deltaDistY = (rayDirY == 0) ? Double.POSITIVE_INFINITY : Math.abs(1 / rayDirY);

            double perpWallDist;

            int stepX;
            int stepY;

            int hit = 0;
            int side = 0;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (player.getX() - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - player.getX()) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (player.getY() - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - player.getY()) * deltaDistY;
            }


            while (hit == 0) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                if (worldMap[mapX][mapY] > 0) hit = 1;
            }

            if (side == 0) perpWallDist = (sideDistX - deltaDistX);
            else perpWallDist = (sideDistY - deltaDistY);

            int lineHeight = (int) (RaycastMaze.SCREEN_HEIGHT / perpWallDist);

            int drawStart = -lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawEnd >= RaycastMaze.SCREEN_HEIGHT) drawEnd = RaycastMaze.SCREEN_HEIGHT - 1;
            
            //calculate value of wallX
            double wallX; //where exactly the wall was hit
            if (side == 0) {
            	wallX = player.getY() + perpWallDist * rayDirY;
            }
            else {
            	wallX = player.getX() + perpWallDist * rayDirX;
            }
            wallX -= Math.floor((wallX));
            //x coordinate on the texture
            int texX = (int)(wallX * (double)texWidth);
            if(side == 0 && rayDirX > 0) {
            	texX = texWidth - texX - 1;
            }
            if(side == 1 && rayDirY < 0) {
            	texX = texWidth - texX - 1;
            }
            double texStep = 1.0 * texHeight / lineHeight;
            double texPos = (drawStart - RaycastMaze.SCREEN_HEIGHT / 2 + lineHeight / 2) * texStep;

            for (int y = drawStart; y < drawEnd; y++) {
				int texY = (int)texPos & (texHeight - 1);
				texPos += texStep;
				int intColor = textures[worldMap[mapX][mapY]].getRGB(texX,texY);
				int r = (intColor>>16)&0xFF;
				int g = (intColor>>8)&0xFF;
				int b = (intColor)&0xFF;
				Color color = Color.rgb(r, g, b);
				  
				screen[x][y] = color;
            }
            //Color color = getColor(worldMap[mapX][mapY]);

            //if (side == 1) color = color.darker();

            //gc.setFill(color);
            //gc.fillRect(x, drawStart, 1, drawEnd - drawStart);
        }
		drawScreen(pw);
            

	}
	public void drawScreen(PixelWriter pw) {
		for(int x = 0; x<RaycastMaze.SCREEN_WIDTH; x++) {
			for(int y = 0; y<RaycastMaze.SCREEN_HEIGHT; y++) {
				Color color = screen[x][y];
				if( color != null) {
					pw.setColor(x,y,screen[x][y]);
				}	
			}
		}
		
	}
    public void drawFloorAndCeiling(GraphicsContext gc) {
            // Set the floor color
            gc.setFill(Color.rgb(100, 100, 100));
            gc.fillRect(0, RaycastMaze.SCREEN_HEIGHT / 2 + 1, RaycastMaze.SCREEN_WIDTH, RaycastMaze.SCREEN_HEIGHT / 2);

            // Set the ceiling color
            gc.setFill(Color.rgb(150, 150, 200));
            gc.fillRect(0, 0, RaycastMaze.SCREEN_WIDTH, RaycastMaze.SCREEN_HEIGHT / 2);
    }
    public static Color getColor(int tileType) {
        return switch (tileType) {
            case 1 -> Color.RED;    // wall
            case 2 -> Color.GREEN;  // exit
            case 3 -> Color.BLUE;
            case 4 -> Color.ORANGE;
            default -> Color.YELLOW;
        };
    }
}
