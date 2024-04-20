public class CameraPlane {
    private double posX;
    private double posY;
    private double dirX;
    private double dirY;
    private double planeX;
    private double planeY;

    public CameraPlane(double posX, double posY, double dirX, double dirY, double planeX, double planeY) {
        this.posX = posX;
        this.posY = posY;
        this.dirX = dirX;
        this.dirY = dirY;
        this.planeX = planeX;
        this.planeY = planeY;
    }

    public void update(double deltaTime, int[][] worldMap, int mapWidth, int mapHeight, boolean up, boolean down, boolean right, boolean left) {
        // Calculate movement and rotation speeds
        double moveSpeed = deltaTime * 5.0;
        double rotSpeed = deltaTime * 3.0;

        if (up) {
            double newX = posX + dirX * moveSpeed;
            double newY = posY + dirY * moveSpeed;
            if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight && worldMap[(int) newX][(int) newY] == 0) {
                posX = newX;
                posY = newY;
            }
        }
        if (down) {
            double newX = posX - dirX * moveSpeed;
            double newY = posY - dirY * moveSpeed;
            if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight && worldMap[(int) newX][(int) newY] == 0) {
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
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public double getPlaneX() {
        return planeX;
    }

    public double getPlaneY() {
        return planeY;
    }
}