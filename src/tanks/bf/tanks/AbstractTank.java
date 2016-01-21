package tanks.bf.tanks;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import tanks.ActionField;
import tanks.bf.*;


public abstract class AbstractTank implements Tank {

    protected Queue<Action> actionsQueue = new ConcurrentLinkedQueue();
    private int speed = 10;
    protected int movePath = 1;


    // 1 - up, 2 - down, 3 - left, 4 - right
    protected Direction direction;
    protected Direction tempDirection;

    // current position on BF
    protected volatile int x;
    protected volatile int y;

    // temp coordinates
    protected int tankX;
    protected int tankY;

    private volatile boolean destroyed;
    private volatile boolean acting;
    protected int step = 0;

    protected BattleField bf;
    protected ActionField af;

    protected Image iTank;

    public AbstractTank(ActionField af, BattleField bf) {
        this(af, bf, 128, 512, Direction.UP);
    }

    public AbstractTank(ActionField af, BattleField bf, int x, int y, Direction direction) {
        this.af = af;
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = false;
    }

    public void pushAction() {
        Action a = actionsQueue.poll();
        af.addActionToQueue(a, this);
    }

    public void addAction(Action a) {
        if (actionsSize() > 250) {
            refreshActions();
        }
        actionsQueue.add(a);
    }

    public boolean isTankActing() {
        return acting;
    }

    public void setTankActing(boolean acting) {
        this.acting = acting;
    }

    public int actionsSize() {
        return actionsQueue.size();
    }


    public void turn(Direction direction) {
        this.direction = direction;
    }

    public void turn(Action action) {
        switch (action) {
            case TURN_UP:
                turn(Direction.UP);
                break;
            case TURN_DOWN:
                turn(Direction.DOWN);
                break;
            case TURN_LEFT:
                turn(Direction.LEFT);
                break;
            case TURN_RIGHT:
                turn(Direction.RIGHT);
                break;
        }
    }

    public void move() {
    }

    public Bullet fire() {
        int bulletX = -100;
        int bulletY = -100;
        if (direction == Direction.UP) {
            bulletX = x + 25;
            bulletY = y;
        } else if (direction == Direction.DOWN) {
            bulletX = x + 25;
            bulletY = y + 64;
        } else if (direction == Direction.LEFT) {
            bulletX = x;
            bulletY = y + 25;
        } else if (direction == Direction.RIGHT) {
            bulletX = x + 64;
            bulletY = y + 25;
        }
        return new Bullet(bulletX, bulletY, direction, this);
    }

    public void draw(Graphics g) {
        if (!destroyed) {

            if (this.getDirection() == Direction.UP) {
                g.drawImage(iTank, x, y, x + 64, y + 64, 0, 400, 200, 600, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });

            } else if (this.getDirection() == Direction.DOWN) {
                g.drawImage(iTank, x, y, x + 64, y + 64, 0, 0, 200, 200, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });

            } else if (this.getDirection() == Direction.LEFT) {
                g.drawImage(iTank, x, y, x + 64, y + 64, 0, 600, 200, 800, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });

            } else {
                g.drawImage(iTank, x, y, x + 64, y + 64, 0, 200, 200, 400, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    public int[] searchForEagle() {
        int[] eagleQuadrant = new int[2];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (bf.scanQuadrant(i, j) instanceof Eagle) {
                    eagleQuadrant[0] = i;
                    eagleQuadrant[1] = j;
                }
            }
        }
        return eagleQuadrant;
    }


    protected boolean checkForObstruction(int v, int h) {
        if (v > 8 || v < 0 || h > 8 || h < 0) {
            return false;
        } else {
            if (bf.scanQuadrant(h, v) instanceof Destroyable) {
                Destroyable bfobject = (Destroyable) bf.scanQuadrant(h, v);
                if (!bfobject.isDestroyed()) {
                    return true;
                }
            }

        }

        return false;
    }

    protected boolean quadrantIsInField(int tankX, int tankY) {
        return (tankX >= 0 && tankX < 9 && tankY >= 0 && tankY < 9);
    }

    protected boolean checkForObstruction() {

        int furtherX = getFurtherQuadrant()[0];
        int furtherY = getFurtherQuadrant()[1];

        return checkForObstruction(furtherX, furtherY);
    }

    protected int[] getFurtherQuadrant() {
        int furtherX = tankX;
        int furtherY = tankY;

        switch (tempDirection) {
            case UP:
                furtherY--;
                break;
            case DOWN:
                furtherY++;
                break;
            case LEFT:
                furtherX--;
                break;
            case RIGHT:
                furtherX++;
                break;
        }
        if (furtherX < 0 || furtherX > 8 || furtherY < 0 || furtherY > 8) {
            return new int[]{tankX, tankY};
        }
        return new int[]{furtherX, furtherY};
    }

    protected void updateTempCoordinates() {
        switch (tempDirection) {
            case UP:
                tankY--;
                break;
            case DOWN:
                tankY++;
                break;
            case LEFT:
                tankX--;
                break;
            case RIGHT:
                tankX++;
                break;
        }
    }

    public void updateX(int x) {
        this.x += x;
    }

    public void updateY(int y) {
        this.y += y;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}