import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Render {
	double rayDirX;
	double rayDirY;
    double sideDistX;
    double sideDistY;
    double deltaDistX;
    double deltaDistY;

	public void update(Player player, int[][] worldMap, GraphicsContext gc) {
        for (int x = 0; x < RaycastMaze.SCREEN_WIDTH; x++) {
            double cameraX = 2 * x / (double) RaycastMaze.SCREEN_WIDTH - 1;
            rayDirX = player.getDirX() + player.getPlaneX() * cameraX;
            rayDirY = player.getDirY() + player.getPlaneY() * cameraX;

            int mapX = (int) player.getX();
            int mapY = (int) player.getY();



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

            if (side == 0) {
            	perpWallDist = (sideDistX - deltaDistX);
            }
            else {
            	perpWallDist = (sideDistY - deltaDistY);
            }

            int lineHeight = (int) (RaycastMaze.SCREEN_HEIGHT / perpWallDist);

            int drawStart = -lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawStart < 0) {
            	drawStart = 0;
            }
            int drawEnd = lineHeight / 2 + RaycastMaze.SCREEN_HEIGHT / 2;
            if (drawEnd >= RaycastMaze.SCREEN_HEIGHT) {
            	drawEnd = RaycastMaze.SCREEN_HEIGHT - 1;
            }

            Color color = getColor(worldMap[mapX][mapY]);

            if (side == 1) {
            	color = color.darker();
            }

            gc.setFill(color);
            gc.fillRect(x, drawStart, 1, drawEnd - drawStart);
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
