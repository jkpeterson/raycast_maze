import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Render extends Application {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static int MAP_WIDTH;
    private static int MAP_HEIGHT;
    private static int[][] worldMap;

    //starting position
    private static double posX, posY;
    private double dirX = -1, dirY = 0;
    private double planeX = 0, planeY = 0.66;
    private boolean up, down, left, right, turnRight, turnLeft;

    private GraphicsContext gc;

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

        primaryStage.setTitle("Raycaster");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                double moveSpeed = 0.05;
                double rotSpeed =  0.02;

                if (up) {
                    movePlayer(moveSpeed, 0);
                }
                if (down) {
                    movePlayer(-moveSpeed, 0);
                }
                if (left) {
                    movePlayer(0, -moveSpeed);
                }
                if (right) {
                    movePlayer(0, moveSpeed);
                }
                if (turnRight) {
                    double oldDirX = dirX;
                    dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
                    dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
                    double oldPlaneX = planeX;
                    planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
                    planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
                }
                if (turnLeft) {
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

    private void movePlayer(double xOffset, double yOffset) {
        double newX = posX + xOffset * dirX + yOffset * planeX;
        double newY = posY + xOffset * dirY + yOffset * planeY;
        if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT && worldMap[(int) newX][(int) newY] == 0) {
            posX = newX;
            posY = newY;
        }
    }

    public static void main(String[] args) {
        MazeBuilder.readFile("resources/Maps/demoMap1.txt");
        posX = MazeBuilder.getStartX();
        posY = MazeBuilder.getStartY();
        MAP_WIDTH = MazeBuilder.getRows();
        MAP_HEIGHT = MazeBuilder.getColumns();
        worldMap = MazeBuilder.getMaze();
        System.out.println("Use <wasd> to walk, <qe> to turn");
        launch(args);
    }
}