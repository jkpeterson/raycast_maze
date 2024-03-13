public class Player {
    private double posX, posY;

    public Player(double startX, double startY) {
        posX = startX;
        posY = startY;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void moveForward(double moveSpeed, double dirX, double dirY, int mapWidth, int mapHeight, int[][] worldMap) {
        double newX = posX + moveSpeed * dirX;
        double newY = posY + moveSpeed * dirY;
        if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight && worldMap[(int) newX][(int) newY] == 0) {
            posX = newX;
            posY = newY;
        }
    }

    public void moveBackward(double moveSpeed, double dirX, double dirY, int mapWidth, int mapHeight, int[][] worldMap) {
        double newX = posX - moveSpeed * dirX;
        double newY = posY - moveSpeed * dirY;
        if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight && worldMap[(int) newX][(int) newY] == 0) {
            posX = newX;
            posY = newY;
        }
    }

    public void strafeLeft(double moveSpeed, double dirX, double dirY, int mapWidth, int mapHeight, int[][] worldMap) {
        double newX = posX - moveSpeed * dirY;
        double newY = posY + moveSpeed * dirX;
        if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight && worldMap[(int) newX][(int) newY] == 0) {
            posX = newX;
            posY = newY;
        }
    }

    public void strafeRight(double moveSpeed, double dirX, double dirY, int mapWidth, int mapHeight, int[][] worldMap) {
        double newX = posX + moveSpeed * dirY;
        double newY = posY - moveSpeed * dirX;
        if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight && worldMap[(int) newX][(int) newY] == 0) {
            posX = newX;
            posY = newY;
        }
    }
}