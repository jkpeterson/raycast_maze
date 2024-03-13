import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Render {
    private Player player;
    private CameraPlane cameraPlane;
    private int[][] worldMap;
    private int mapWidth;
    private int mapHeight;
    private int screenWidth;
    private int screenHeight;

    private GraphicsContext gc;
    private boolean up, down, left, right, turnRight, turnLeft;

    public Render(Player player, CameraPlane cameraPlane, int[][] worldMap, int mapWidth, int mapHeight, int screenWidth, int screenHeight) {
        this.player = player;
        this.cameraPlane = cameraPlane;
        this.worldMap = worldMap;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(screenWidth, screenHeight);
        gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) up = true;
            if (e.getCode() == KeyCode.S) down = true;
            if (e.getCode() == KeyCode.D) right = true;
            if (e.getCode() == KeyCode.A) left = true;
            if (e.getCode() == KeyCode.E) turnRight = true;
            if (e.getCode() == KeyCode.Q) turnLeft = true;
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) up = false;
            if (e.getCode() == KeyCode.S) down = false;
            if (e.getCode() == KeyCode.D) right = false;
            if (e.getCode() == KeyCode.A) left = false;
            if (e.getCode() == KeyCode.E) turnRight = false;
            if (e.getCode() == KeyCode.Q) turnLeft = false;
        });

        primaryStage.setTitle("Raycaster Maze");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double moveSpeed = 0.08;
                double rotSpeed = 0.02;

                if (up) {
                    player.moveForward(moveSpeed, cameraPlane.getDirX(), cameraPlane.getDirY(), mapWidth, mapHeight, worldMap);
                }
                if (down) {
                    player.moveBackward(moveSpeed, cameraPlane.getDirX(), cameraPlane.getDirY(), mapWidth, mapHeight, worldMap);
                }
                if (left) {
                    player.strafeLeft(moveSpeed, cameraPlane.getDirX(), cameraPlane.getDirY(), mapWidth, mapHeight, worldMap);
                }
                if (right) {
                    player.strafeRight(moveSpeed, cameraPlane.getDirX(), cameraPlane.getDirY(), mapWidth, mapHeight, worldMap);
                }
                if (turnRight) {
                    cameraPlane.rotateRight(rotSpeed);
                }
                if (turnLeft) {
                    cameraPlane.rotateLeft(rotSpeed);
                }

                gc.clearRect(0, 0, screenWidth, screenHeight);
                Texture.drawFloorAndCeiling(gc, screenWidth, screenHeight);

                for (int x = 0; x < screenWidth; x++) {
                    double cameraX = 2 * x / (double) screenWidth - 1;
                    double rayDirX = cameraPlane.getDirX() + cameraPlane.getPlaneX() * cameraX;
                    double rayDirY = cameraPlane.getDirY() + cameraPlane.getPlaneY() * cameraX;

                    int mapX = (int) player.getPosX();
                    int mapY = (int) player.getPosY();

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
                        sideDistX = (player.getPosX() - mapX) * deltaDistX;
                    } else {
                        stepX = 1;
                        sideDistX = (mapX + 1.0 - player.getPosX()) * deltaDistX;
                    }
                    if (rayDirY < 0) {
                        stepY = -1;
                        sideDistY = (player.getPosY() - mapY) * deltaDistY;
                    } else {
                        stepY = 1;
                        sideDistY = (mapY + 1.0 - player.getPosY()) * deltaDistY;
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

                    int lineHeight = (int) (screenHeight / perpWallDist);

                    int drawStart = -lineHeight / 2 + screenHeight / 2;
                    if (drawStart < 0) drawStart = 0;
                    int drawEnd = lineHeight / 2 + screenHeight / 2;
                    if (drawEnd >= screenHeight) drawEnd = screenHeight - 1;

                    Color color = Texture.getColor(worldMap[mapX][mapY]);

                    if (side == 1) color = color.darker();

                    gc.setFill(color);
                    gc.fillRect(x, drawStart, 1, drawEnd - drawStart);
                }
            }
        };

        timer.start();
    }
}