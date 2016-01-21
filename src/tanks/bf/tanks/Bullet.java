package tanks.bf.tanks;

import java.awt.Color;
import java.awt.Graphics;

import tanks.bf.Destroyable;
import tanks.bf.Drawable;

public class Bullet implements Drawable, Destroyable {

    private int speed = 5;

    private int x;
    private int y;
    private Direction direction;
    private Tank tank;

    private volatile boolean destroyed;

    public Bullet(int x, int y, Direction direction, Tank tank) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = true;
        this.tank = tank;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Tank getTank() {
        return tank;
    }

    public void updateX(int x) {
        this.x += x;
    }

    public void updateY(int y) {
        this.y += y;
    }

    @Override
    public void draw(Graphics g) {
        if (tank instanceof T34) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(new Color(255, 255, 0));
        }

        if (!destroyed) {
//            g.setColor(new Color(255, 255, 0));
            g.fillOval(this.x, this.y, 10, 10);
        }
    }

    public void destroy() {
        destroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }
}
