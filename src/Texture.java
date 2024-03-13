import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Texture {
    public static Color getColor(int tileType) {
        return switch (tileType) {
            case 1 -> Color.RED;
            case 2 -> Color.GREEN;
            case 3 -> Color.BLUE;
            case 4 -> Color.ORANGE;
            default -> Color.YELLOW;
        };
    }

    public static void drawFloorAndCeiling(GraphicsContext gc, int screenWidth, int screenHeight) {
        // Set the floor color
        gc.setFill(Color.rgb(100, 100, 100));
        gc.fillRect(0, screenHeight / 2 + 1, screenWidth, screenHeight / 2);

        // Set the ceiling color
        gc.setFill(Color.rgb(150, 150, 200));
        gc.fillRect(0, 0, screenWidth, screenHeight / 2);
    }
}