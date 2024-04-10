import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Raycast extends Application {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private static final int MAP_WIDTH = 24;
    private static final int MAP_HEIGHT = 24;

    private static final int[][] worldMap = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 2, 2, 0, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 4, 0, 0, 0, 0, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 4, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private double posX = 22, posY = 12;
    private double dirX = -1, dirY = 0;
    private double planeX = 0, planeY = 0.66;
    private boolean up, down, right, left;

    private GraphicsContext gc;
    private long lastTime = System.nanoTime();
    private double deltaTime = 0;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) up = true;
            if (e.getCode() == KeyCode.S) down = true;
            if (e.getCode() == KeyCode.D) right = true;
            if (e.getCode() == KeyCode.A) left = true;
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) up = false;
            if (e.getCode() == KeyCode.S) down = false;
            if (e.getCode() == KeyCode.D) right = false;
            if (e.getCode() == KeyCode.A) left = false;
        });

        primaryStage.setTitle("Raycaster");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                long currentTime = System.nanoTime();
                deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
                lastTime = currentTime;

                // Calculate movement and rotation speeds
                double moveSpeed = deltaTime * 5.0;
                double rotSpeed = deltaTime * 3.0;

                if (up) {
                    double newX = posX + dirX * moveSpeed;
                    double newY = posY + dirY * moveSpeed;
                    if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT && worldMap[(int) newX][(int) newY] == 0) {
                        posX = newX;
                        posY = newY;
                    }
                }
                if (down) {
                    double newX = posX - dirX * moveSpeed;
                    double newY = posY - dirY * moveSpeed;
                    if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT && worldMap[(int) newX][(int) newY] == 0) {
                        posX = newX;
                        posY = newY;
                    }
                }
                if (right) {
                    double oldDirX = dirX;
                    dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
                    dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
                    double oldPlaneX = planeX;
                    planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
                    planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
                }
                if (left) {
                    double oldDirX = dirX;
                    dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
                    dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
                    double oldPlaneX = planeX;
                    planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
                    planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
                }

                gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

                for (int x = 0; x < SCREEN_WIDTH; x++) {
                    double cameraX = 2 * x / (double) SCREEN_WIDTH - 1;
                    double rayDirX = dirX + planeX * cameraX;
                    double rayDirY = dirY + planeY * cameraX;

                    int mapX = (int) posX;
                    int mapY = (int) posY;

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

                    int lineHeight = (int) (SCREEN_HEIGHT / perpWallDist);

                    int drawStart = -lineHeight / 2 + SCREEN_HEIGHT / 2;
                    if (drawStart < 0) drawStart = 0;
                    int drawEnd = lineHeight / 2 + SCREEN_HEIGHT / 2;
                    if (drawEnd >= SCREEN_HEIGHT) drawEnd = SCREEN_HEIGHT - 1;

                    Color color = switch (worldMap[mapX][mapY]) {
                        case 1 -> Color.RED;
                        case 2 -> Color.GREEN;
                        case 3 -> Color.BLUE;
                        case 4 -> Color.ORANGE;
                        default -> Color.YELLOW;
                    };

                    if (side == 1) color = color.darker();

                    gc.setFill(color);
                    gc.fillRect(x, drawStart, 1, drawEnd - drawStart);
                }
            }
        };

        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}