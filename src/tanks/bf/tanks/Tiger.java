package tanks.bf.tanks;

import tanks.ActionField;
import tanks.bf.BattleField;

public class Tiger extends AbstractTank {

    private int armor;
    private int speed = 20;
    private String defenderLocation;
    private int defenderX;
    private int defenderY;


    public Tiger(ActionField af, BattleField bf) {
        super(af, bf);
        iTank = bf.imageLoader.iTiger;
        armor = 1;
        destroyDefender();
        defenderLocation = bf.getDefenderLocation();
    }

    public Tiger(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        iTank = bf.imageLoader.iTiger;
        armor = 1;
        destroyDefender();
        defenderLocation = bf.getDefenderLocation();
    }

    @Override
    public void destroy() {

        if (armor > 0) {
            armor--;
        } else {
            super.destroy();
        }
    }


    public void refreshActions() {
        step = 0;
        actionsQueue.clear();
        destroyDefender();
    }

    public void pushAction() {

        String newDefenderLocation = bf.getQuadrant(af.defender.getX(), af.defender.getY());

        if (!newDefenderLocation.equals(defenderLocation)) {
            actionsQueue.clear();
            destroyDefender();
        }

        super.pushAction();
    }


    private void destroyDefender() {

        tempDirection = Direction.NONE;
        String tankLocation = bf.getQuadrant(x, y);
        String newDefenderLocation = bf.getQuadrant(af.defender.getX(), af.defender.getY());

        defenderY = Integer.parseInt(newDefenderLocation.substring(0, newDefenderLocation.indexOf("_")));
        defenderX = Integer.parseInt(newDefenderLocation.substring(newDefenderLocation.indexOf("_") + 1));
        tankY = Integer.parseInt(tankLocation.substring(0, tankLocation.indexOf("_")));
        tankX = Integer.parseInt(tankLocation.substring(tankLocation.indexOf("_") + 1));

        while (tankX != defenderX || tankY != defenderY) {
            overlapX();
            overlapY();
            defenderLocation = newDefenderLocation;
        }

    }

    private void overlapX() {

        while (tankX != defenderX) {
            if (tankX < defenderX) {
                if (!tempDirection.equals(Direction.RIGHT)) {
                    tempDirection = Direction.RIGHT;
                    addAction(Action.TURN_RIGHT);
                }
            }
            if (tankX > defenderX) {
                if (!tempDirection.equals(Direction.LEFT)) {
                    tempDirection = Direction.LEFT;
                    addAction(Action.TURN_LEFT);
                }
            }
            if (tankY == defenderY) {
                addAction(Action.FIRE);
            }
            if (checkForObstruction()) {
                addAction(Action.FIRE);
            }
            updateTempCoordinates();
            addAction(Action.MOVE);
        }

    }

    private void overlapY() {

        while (tankY != defenderY) {

            if (tankY < defenderY) {
                if (!tempDirection.equals(Direction.DOWN)) {
                    tempDirection = Direction.DOWN;
                    addAction(Action.TURN_DOWN);
                }
            }
            if (tankY > defenderY) {
                if (!tempDirection.equals(Direction.UP)) {
                    tempDirection = Direction.UP;
                    addAction(Action.TURN_UP);
                }
            }
            if (tankX == defenderX) {
                addAction(Action.FIRE);
            }
            if (checkForObstruction()) {
                addAction(Action.FIRE);
            }
            updateTempCoordinates();
            addAction(Action.MOVE);

        }
    }

    @Override
    public int getSpeed() {
        return speed;
    }
}

