package tanks.bf.tanks;

import java.util.ArrayList;

import tanks.ActionField;
import tanks.bf.*;

public class T34 extends AbstractTank {

    private ArrayList<SimpleBFObject> untouchableBFObj = new ArrayList<SimpleBFObject>();
    private String enemyLocation;
    private int[] eagleLocation;


    public T34(ActionField af, BattleField bf) {
        super(af, bf, 128, 512, Direction.UP);
        iTank = bf.imageLoader.iT34;
        enemyLocation = bf.getAgressorLocation();
        eagleLocation = searchForEagle();
        identifyUntouchableObjects();
    }

    public T34(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        iTank = bf.imageLoader.iT34;
        enemyLocation = bf.getAgressorLocation();
        eagleLocation = searchForEagle();
        identifyUntouchableObjects();
    }

    private void identifyUntouchableObjects() {

        int eagleY = eagleLocation[0];
        int eagleX = eagleLocation[1];

        for (int i = eagleX - 1; i <= eagleX + 1; i++) {
            for (int k = eagleY - 1; k <= eagleY + 1; k++) {
                if (i >= 0 && i < 9 && k >= 0 && k < 9) {
                    SimpleBFObject bfObject = bf.scanQuadrant(k, i);
                    if (!(bfObject instanceof Blank)) {
                        untouchableBFObj.add(bfObject);
                    }
                }
            }
        }
    }

    public boolean checkForObstruction(int v, int h) {

        if (v > 8 || v < 0 || h > 8 || h < 0) {
            return false;
        }
        if (bf.scanQuadrant(h, v) instanceof Destroyable) {
            Destroyable bfobject = (Destroyable) bf.scanQuadrant(h, v);
            if (untouchableBFObj.contains(bfobject)) {
                return false;
            }
            if (bfobject instanceof Brick && !bfobject.isDestroyed()) {
                return true;
            }
        }

        return false;
    }

    public void refreshActions(){

    }

    public void destroyEnemy() {
        Direction direction = Direction.NONE;
        String tankLocation = bf.getQuadrant(x, y);
        String newEnemyLocation = bf.getAgressorLocation();

        if (!newEnemyLocation.equals(enemyLocation) || actionsSize() == 0) {
            step = 0;
            actionsQueue.clear();

            int enemyY = Integer.parseInt(newEnemyLocation.substring(0, newEnemyLocation.indexOf("_")));
            int enemyX = Integer.parseInt(newEnemyLocation.substring(newEnemyLocation.indexOf("_") + 1));
            tankY = Integer.parseInt(tankLocation.substring(0, tankLocation.indexOf("_")));
            tankX = Integer.parseInt(tankLocation.substring(tankLocation.indexOf("_") + 1));

            overlapX(tankX, tankY, enemyX, enemyY, direction);
            tankX = enemyX;
            overlapY(tankX, tankY, enemyX, enemyY, direction);

            addAction(Action.FIRE);
            enemyLocation = newEnemyLocation;
        }

    }

    private void overlapY(int tankX, int tankY, int enemyX, int enemyY, Direction direction) {

        while (tankY != enemyY) {

            if (tankY < enemyY) {
                if (!direction.equals(Direction.DOWN)) {
                    direction = Direction.DOWN;
                    addAction(Action.TURN_DOWN);
                }

            }
            if (tankY > enemyY) {
                if (!direction.equals(Direction.UP)) {
                    direction = Direction.UP;
                    addAction(Action.TURN_UP);
                }

            }

            if (checkForObstruction(tankX, tankY)) {
                addAction(Action.FIRE);
            }

            if (untouchableBFObj.contains(bf.scanQuadrant(tankY, tankX))) {
                direction = Direction.LEFT;
                addAction(Action.TURN_LEFT);
                updateTempCoordinates();
                addAction(Action.MOVE);
            }

            if (tankX == enemyX) {
                addAction(Action.FIRE);
                addAction(Action.FIRE);
            }
            updateTempCoordinates();
            addAction(Action.MOVE);
        }
    }

    private void overlapX(int tankX, int tankY, int enemyX, int enemyY, Direction direction) {

        while (tankX != enemyX) {

            if (tankX < enemyX) {
                if (!direction.equals(Direction.RIGHT)) {
                    direction = Direction.RIGHT;
                    addAction(Action.TURN_RIGHT);
                }

            }

            if (tankX > enemyX) {
                if (!direction.equals(Direction.LEFT)) {
                    direction = Direction.LEFT;
                    addAction(Action.TURN_LEFT);
                }

            }

            if (checkForObstruction(tankX, tankY)) {
                addAction(Action.FIRE);
            }
            if (untouchableBFObj.contains(bf.scanQuadrant(tankY, tankX))) {
                direction = Direction.UP;
                addAction(Action.TURN_UP);
                updateTempCoordinates();
                addAction(Action.MOVE);
            }
            if (tankY == enemyY) {
                addAction(Action.FIRE);
                addAction(Action.FIRE);
            }
            updateTempCoordinates();
            addAction(Action.MOVE);

        }
    }

}
