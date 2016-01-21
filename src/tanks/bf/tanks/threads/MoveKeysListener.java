package tanks.bf.tanks.threads;

import tanks.bf.tanks.Direction;
import tanks.bf.tanks.AbstractTank;
import tanks.bf.tanks.Action;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class MoveKeysListener extends KeyAdapter {

    private volatile boolean isKeyPressed;
    private AbstractTank tank;
    private volatile Action action;
    private Direction direction;
    private KeyEvent keyEvent;


    public MoveKeysListener(AbstractTank tank) {
        this.tank = tank;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public boolean isKeyPressed() {
        return isKeyPressed;
    }

    public void setIsKeyPressed(boolean isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (isMovingKey(e.getKeyCode())) {
            setIsKeyPressed(false);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (isKeyPressed() && tank.isTankActing()) {
            return;
        }

        if (isMovingKey(e.getKeyCode())) {
            
            keyEvent = e;
            int keyCode = e.getKeyCode();

            switch (keyCode) {
                case 38: direction = Direction.UP;
                    break;
                case 40: direction = Direction.DOWN;
                    break;
                case 37: direction = Direction.LEFT;
                    break;
                case 39: direction = Direction.RIGHT;
                    break;
            }

            if (tank.getDirection().equals(direction)) {
                action = Action.MOVE;
            } else {
                action = convertDirectionToAction(direction);
            }

            tank.addAction(action);
            setIsKeyPressed(true);
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

    private boolean isMovingKey(int k) {
        return (k == 38 || k == 40 || k == 37 || k == 39);
    }
}
