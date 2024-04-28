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
			//loads all the texture files and adds them to the texture array
			//0 represents air so no texture is loaded in index 0
			BufferedImage wall = ImageIO.read(new File("resources/Textures/brick_mossy.png"));
			BufferedImage door = ImageIO.read(new File("resources/Textures/door.png"));
			textures[1] = wall;
			textures[2] = door;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(Player player, GraphicsContext gc) {
		PixelWriter pw = gc.getPixelWriter();
		screen = new Color[RaycastMaze.SCREEN_WIDTH][RaycastMaze.SCREEN_HEIGHT];
		double posX = player.getX();
		double posY = player.getY();
		//iterates through every horizontal pixel of the screen, and fires a ray at that angle
		for (int x = 0; x < RaycastMaze.SCREEN_WIDTH; x++) {
			//calculates the direction of the ray as a vector using the player's position and the
			//camera plane position
            double cameraX = 2 * x / (double) RaycastMaze.SCREEN_WIDTH - 1;
            double rayDirX = player.getDirX() + player.getPlaneX() * cameraX;
            double rayDirY = player.getDirY() + player.getPlaneY() * cameraX;

            //this variable will represent the map tile that the ray has hit
            int mapX = (int) posX;
            int mapY = (int) posY;

            //the motion of the array is captured in 'steps' essentially
            //each time the ray hits a grid line it will check for a solid tile
            double sideDistX;
            double sideDistY;
            double deltaDistX;
            double deltaDistY;

            if(rayDirX == 0) {
            	deltaDistX = Double.POSITIVE_INFINITY; 
            }
            else {
            	deltaDistX = Math.abs(1 / rayDirX);
            }
            if (rayDirY == 0) {
            	deltaDistY = Double.POSITIVE_INFINITY;
            }
            else {
            	deltaDistY = Math.abs(1 / rayDirY);
            }

            int stepX;
            int stepY;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (posX - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - posX) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (posY - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - posY) * deltaDistY;
            }

            //this loop represents the motion of the ray through space
            //it will continue forward via a step until it hits a tile
            //it will also use the side distance variables to determine if the ray hit the side of a wall
            boolean hit = false;
            boolean side = false;
            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = false;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = true;
                }
                if (RaycastMaze.worldMap[mapX][mapY] > 0) {
                	hit = true;
                }
            }

            //when the ray has hit, it will calculate the distance the array traveled to a perpendicular wall
            double perpWallDist;
            if (!side) {
            	perpWallDist = (sideDistX - deltaDistX);
            }
            else {
            	perpWallDist = (sideDistY - deltaDistY);
            }
            
            //then it uses the length of the ray to figure out how long the vertical
            //line of pixels to render should be
            //if the line of pixels would go offscreen it just renders it from top to bottom

            int lineHeight = (int) (RaycastMaze.SCREEN_HEIGHT / perpWallDist);

            int drawStart = -lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawStart < 0) {
            	drawStart = 0;
            }
            int drawEnd = lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawEnd >= RaycastMaze.SCREEN_HEIGHT) {
            	drawEnd = RaycastMaze.SCREEN_HEIGHT - 1;
            }
            

            //the rendering itself differs depending on if textures are enabled

            if (RaycastMaze.textures) {
            	//with textures, it calculates where on the wall the ray hit
                double wallX;
                if (!side) {
                	wallX = posY + perpWallDist * rayDirY;
                }
                else {
                	wallX = posX + perpWallDist * rayDirX;
                }
                wallX -= Math.floor((wallX));
                //then uses that information to determine what x value of the texture to read
                //and how many pixels on the texture are equal to one pixel on screen
                int texX = (int)(wallX * (double)texWidth);
                if(!side && rayDirX > 0) {
                	texX = texWidth - texX - 1;
                }
                if(side && rayDirY < 0) {
                	texX = texWidth - texX - 1;
                }
                double texStep = 1.0 * texHeight / lineHeight;
                double texPos = (drawStart - RaycastMaze.SCREEN_HEIGHT / 2 + lineHeight / 2) * texStep;
            	textureRender(x, mapX, mapY, side, drawStart, drawEnd, texX, texStep, texPos);
            }
            else {
                //without textures it will just draw a solid line of a single color
            	basicRender(x, mapX, mapY, side, drawStart, drawEnd);
            }
        }
		drawScreen(pw);
            

	}
	private void basicRender(int x, int mapX, int mapY, boolean side, int drawStart, int drawEnd) {
		//iterates through each pixel of the ray's vertical slice
		//grabs untextured box color of that space and adds a line to the screen array
        Color color = getColor(RaycastMaze.worldMap[mapX][mapY]);
        if (side) {
        	color = color.darker();
        }
        for (int y = drawStart; y < drawEnd; y++) {
        	screen[x][y] = color;
        }
	}
	private void textureRender(int x, int mapX, int mapY, boolean side, int drawStart, int drawEnd, int texX, double texStep, double texPos) {
		//iterates through each pixel of this vertical slice
		//uses the texture pixels to load the rgb value and set that pixel in the screen array
        for (int y = drawStart; y < drawEnd; y++) {
			int texY = (int)texPos & (texHeight - 1);
			texPos += texStep;
			//grabs a binary int of the color data so it has to be bitshifted to get the rgb channels
			int intColor = textures[RaycastMaze.worldMap[mapX][mapY]].getRGB(texX,texY);
			int r = (intColor>>16)&0xFF;
			int g = (intColor>>8)&0xFF;
			int b = (intColor)&0xFF;
			Color color = Color.rgb(r, g, b);
			if (side) {
				color = color.darker();
			}
			screen[x][y] = color;
        }
	}
	public void drawScreen(PixelWriter pw) {
		//iterates through screen array to render all the pixels set this frame
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
    	//returns the solid color based on what tile the ray hit
    	//currently only using red and green
        return switch (tileType) {
            case 1 -> Color.RED;    // wall
            case 2 -> Color.GREEN;  // exit
            case 3 -> Color.BLUE;
            case 4 -> Color.ORANGE;
            default -> Color.YELLOW;
        };
    }
}
