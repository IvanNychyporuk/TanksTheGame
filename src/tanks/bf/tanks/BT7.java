package tanks.bf.tanks;

import java.util.ArrayList;
import java.util.Random;

import tanks.ActionField;
import tanks.bf.*;

public class BT7 extends AbstractTank {

    private int[] eagleLocation;
    private int eagleX;
    private int eagleY;

    private ArrayList<SimpleBFObject> untouchableBFObj = new ArrayList<SimpleBFObject>();


    public BT7(ActionField af, BattleField bf) {
        super(af, bf);
        iTank = bf.imageLoader.iBt7;
        movePath = 1;
        identifyUntouchableObjects();
        findTheEagle();
        destroyEagle();
    }

    public BT7(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        iTank = bf.imageLoader.iBt7;
        movePath = 1;
        identifyUntouchableObjects();
        findTheEagle();
        destroyEagle();
    }


    private void findTheEagle() {

        eagleLocation = searchForEagle();
        eagleY = eagleLocation[0];
        eagleX = eagleLocation[1];

    }

    private void identifyUntouchableObjects() {

        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                SimpleBFObject bfObject = bf.scanQuadrant(k, i);
                if (bfObject instanceof Rock) {
                    untouchableBFObj.add(bfObject);
                }
            }
        }
    }

    public void refreshActions() {

        tempDirection = Direction.NONE;
        actionsQueue.clear();
        destroyEagle();

    }


    private void detour() {

        addAction(changeRandomDirection());

        if (checkForObstruction()) {
            addAction(Action.FIRE);
        }

        updateTempCoordinates();
        addAction(Action.MOVE);
        addAction(turnToEagle());

        while (checkForRocks()) {
            addAction(changeRandomDirection());
            updateTempCoordinates();
            addAction(Action.MOVE);
            addAction(turnToEagle());
        }
        if (checkForObstruction()) {
            addAction(Action.FIRE);
        }

        updateTempCoordinates();
        addAction(Action.MOVE);

        if (checkForObstruction()) {
            addAction(Action.FIRE);
        }

        updateTempCoordinates();
        addAction(Action.MOVE);

        addAction(turnToEagle());

        if (checkForObstruction()) {
            addAction(Action.FIRE);
        }

        updateTempCoordinates();
        addAction(Action.MOVE);
        addAction(turnToEagle());
    }

    private boolean checkForRocks() {

        int furtherX = getFurtherQuadrant()[0];
        int furtherY = getFurtherQuadrant()[1];

        if (quadrantIsInField(furtherX, furtherY) && untouchableBFObj.contains(bf.scanQuadrant(furtherY, furtherX))) {
            return true;
        }

        return false;
    }

    private Action turnToEagle() {

        if (isEagleObserved()) {
            return Action.FIRE;
        }

        Direction d = Direction.NONE;
        int turnIndex = 0;
        switch (tempDirection) {
            case UP:
                turnIndex = 1;
                break;
            case DOWN:
                turnIndex = 1;
                break;
        }
        if (turnIndex == 1 && (tankX - eagleX) < 0) {
            d = Direction.RIGHT;
        } else if (turnIndex == 1 && (tankX - eagleX) > 0) {
            d = Direction.LEFT;
        } else if (turnIndex == 0 && (tankY - eagleY) > 0) {
            d = Direction.UP;
        } else {
            d = Direction.DOWN;
        }
        tempDirection = d;
        return convertDirectionToAction(tempDirection);
    }

    public void destroyEagle() {

        tempDirection = direction;
        String tankLocation = bf.getQuadrant(x, y);
        tankY = Integer.parseInt(tankLocation.substring(0, tankLocation.indexOf("_")));
        tankX = Integer.parseInt(tankLocation.substring(tankLocation.indexOf("_") + 1));

        while (tankX != eagleX || tankY != eagleY) {
            overlapX();
            overlapY();
        }

    }

    private void overlapX() {

        while (tankX != eagleX) {

            if (tankX < eagleX) {
                if (!tempDirection.equals(Direction.RIGHT)) {
                    tempDirection = Direction.RIGHT;
                    addAction(Action.TURN_RIGHT);
                }
            }
            if (tankX > eagleX) {
                if (!tempDirection.equals(Direction.LEFT)) {
                    tempDirection = Direction.LEFT;
                    addAction(Action.TURN_LEFT);
                }
            }
            if (isEagleObserved()) {
                addAction(Action.FIRE);
            }
            if (checkForRocks()) {
                detour();
            }

            if (checkForObstruction(tankX, tankY)) {
                addAction(Action.FIRE);
            }

            updateTempCoordinates();
            addAction(Action.MOVE);

        }
    }

    private void overlapY() {

        while (tankY != eagleY) {

            if (tankY < eagleY) {
                if (!tempDirection.equals(Direction.DOWN)) {
                    tempDirection = Direction.DOWN;
                    addAction(Action.TURN_DOWN);
                }
            }
            if (tankY > eagleY) {
                if (!tempDirection.equals(Direction.UP)) {
                    tempDirection = Direction.UP;
                    addAction(Action.TURN_UP);
                }
            }
            if (isEagleObserved()) {
                addAction(Action.FIRE);
            }
            if (checkForRocks()) {
                detour();
            }
            if (checkForObstruction(tankX, tankY)) {
                addAction(Action.FIRE);
            }
            addAction(Action.MOVE);
            updateTempCoordinates();

        }
    }


    private Action convertDirectionToAction(Direction direction) {

        switch (direction) {
            case UP:
                return Action.TURN_UP;
            case DOWN:
                return Action.TURN_DOWN;
            case LEFT:
                return Action.TURN_LEFT;
            case RIGHT:
                return Action.TURN_RIGHT;
        }
        return Action.NONE;
    }

    private Action changeRandomDirection() {
        String directions = "left-right";
        Action a = Action.NONE;
        switch (tempDirection) {
            case LEFT:
                directions = "up-down";
                break;
            case RIGHT:
                directions = "up-down";
                break;
        }
        Random r = new Random();
        int i = r.nextInt(2);
        if (directions.equals("left-right")) {
            switch (i) {
                case 0:
                    a = Action.TURN_LEFT;
                    tempDirection = Direction.LEFT;
                    break;
                case 1:
                    a = Action.TURN_RIGHT;
                    tempDirection = Direction.RIGHT;
                    break;
            }
        } else if (directions.equals("up-down")) {
            switch (i) {
                case 0:
                    a = Action.TURN_UP;
                    tempDirection = Direction.UP;
                    break;
                case 1:
                    a = Action.TURN_DOWN;
                    tempDirection = Direction.DOWN;
                    break;
            }
        }
        return a;
    }

    private boolean isEagleObserved() {

        int differenceY = eagleY - tankY;
        int differenceX = eagleX - tankX;

        if (differenceX == 0) {
            return observeEagleX(differenceY);
        } else if (differenceY == 0) {
            return observeEagleY(differenceX);
        }

        return false;
    }

    private boolean observeEagleX(int differenceY) {

        SimpleBFObject o;

        if (differenceY > 0 && tempDirection.equals(Direction.DOWN)) {

            for (int i = tankY; i < differenceY; i++) {
                o = bf.scanQuadrant(i, tankX);
                if (untouchableBFObj.contains(o)) {
                    return false;
                }
            }
            return true;
        }

        if (differenceY < 0 && tempDirection.equals(Direction.UP)) {

            for (int i = eagleY; i < differenceY; i++) {
                o = bf.scanQuadrant(i, tankX);
                if (untouchableBFObj.contains(o)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private boolean observeEagleY(int differenceX) {

        SimpleBFObject o;

        if (differenceX > 0 && tempDirection.equals(Direction.RIGHT)) {

            for (int i = tankX; i < differenceX; i++) {
                o = bf.scanQuadrant(tankY, i);
                if (untouchableBFObj.contains(o)) {
                    return false;
                }
            }
            return true;
        }

        if (differenceX < 0 && tempDirection.equals(Direction.LEFT)) {

            for (int i = eagleX; i < differenceX; i++) {
                o = bf.scanQuadrant(tankY, i);
                if (untouchableBFObj.contains(o)) {
                    return false;
                }
            }
            return true;
        }

        return false;

    }

}
