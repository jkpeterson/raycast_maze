import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Render {
	BufferedImage image;
	
	public Render() {
		try {
			image = ImageIO.read(new File("resources/Textures/tex1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(Player player, int[][] worldMap, GraphicsContext gc) {
		PixelWriter pw = gc.getPixelWriter();
        for (int x = 0; x < RaycastMaze.SCREEN_WIDTH; x++) {
            double cameraX = 2 * x / (double) RaycastMaze.SCREEN_WIDTH - 1;
            double rayDirX = player.getDirX() + player.getPlaneX() * cameraX;
            double rayDirY = player.getDirY() + player.getPlaneY() * cameraX;

            int mapX = (int) player.getX();
            int mapY = (int) player.getY();
            
            double deltaDistX = 0;
            double deltaDistY = 0;

            if (rayDirX == 0) {
            	deltaDistX = Double.POSITIVE_INFINITY;
            }
            else {
            	deltaDistX = Math.abs(1 / rayDirX);
            }
            if (rayDirY == 0) {
            	deltaDistX = Double.POSITIVE_INFINITY;
            }
            else {
            	deltaDistY = Math.abs(1 / rayDirY);
            }

            double perpWallDist;

            int stepX;
            int stepY;

            int hit = 0;
            int side = 0;
            
            double sideDistX = 0;
            double sideDistY = 0;

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
            double wallX;
            if (side == 0) {
            	perpWallDist = (sideDistX - deltaDistX);
            	wallX = player.getY() + perpWallDist * rayDirY;
            }
            else {
            	perpWallDist = (sideDistY - deltaDistY);
            	wallX = player.getX() + perpWallDist * rayDirX;
            }
            wallX -= Math.floor((wallX));

            int lineHeight = (int) (RaycastMaze.SCREEN_HEIGHT / perpWallDist);

            int drawStart = -lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawStart < 0) {
            	drawStart = 0;
            }
            int drawEnd = lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawEnd >= RaycastMaze.SCREEN_HEIGHT) {
            	drawEnd = RaycastMaze.SCREEN_HEIGHT - 1;
            }
//            int texHeight = 128;
//            int texWidth = 128;
//            double texStep = 1.0 / (texHeight * lineHeight);
//            double texPos = (drawStart - RaycastMaze.SCREEN_HEIGHT / 2 + lineHeight / 2) * texStep;
//            int texX = (int)(wallX / texWidth);
//            if(side == 0 && rayDirX > 0) {
//            	texX = texWidth - texX - 1;
//            }
//            if(side == 1 && rayDirY < 0) {
//            	texX = texWidth - texX - 1;
//            }
//            Color color = null;
//            
//            for (int y = drawStart; y < drawEnd; y++) {
//                int texY = (int)texPos;
//                texPos += texStep;
//                int intColor = image.getRGB(texX,texY);
//                int r = (intColor>>16)&0xFF;
//                int g = (intColor>>8)&0xFF;
//                int b = (intColor)&0xFF;
//                color = Color.rgb(r, g, b);
//                pw.setColor(x,y,color);
//            }
           Color color = getColor(worldMap[mapX][mapY]);

           if (side == 1) {
        	   color = color.darker();
           }
           gc.setFill(color);
           gc.fillRect(x, drawStart, 1, drawEnd - drawStart);


        }
        //drawScreen(gc);
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
