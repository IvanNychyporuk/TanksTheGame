package tanks.bf.tanks.threads;

import tanks.ActionField;
import tanks.bf.tanks.AbstractTank;
import tanks.bf.tanks.Action;

/**
 * Created by Ivan on 14.10.2015.
 */
public class KeysListenerThread implements Runnable {

    private ActionField af;
    private AbstractTank tank;
    private volatile MoveKeysListener moveKL;
    private volatile FireKeyListener fireKL;
    private volatile int moveKeyCode;
    private volatile Action action = Action.NONE;


    public KeysListenerThread(ActionField af) {
        this.af = af;
        this.tank = af.defender;
        moveKL = new MoveKeysListener(tank);
        fireKL = new FireKeyListener(this, tank);
    }

    public int getMoveKeyCode() {
        return moveKeyCode;
    }

    public void setMoveKeyCode(int moveKeyCode) {
        this.moveKeyCode = moveKeyCode;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    private void resetMoveKeyCode() {
        moveKeyCode = 0;
    }

    @Override
    public void run() {
        af.setFocusable(true);
        af.requestFocusInWindow();
        af.addKeyListener(moveKL);
        af.addKeyListener(fireKL);

    }

    protected void checkIfMoveKeyPressed() {

        if (moveKL.isKeyPressed()) {
            moveKL.setIsKeyPressed(false);
            moveKL.keyPressed(moveKL.getKeyEvent());
        }

    }


}
