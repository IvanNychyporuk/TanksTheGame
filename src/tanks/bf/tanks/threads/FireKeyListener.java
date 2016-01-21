package tanks.bf.tanks.threads;

import tanks.bf.tanks.AbstractTank;
import tanks.bf.tanks.Action;
import tanks.bf.tanks.threads.KeysListenerThread;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Ivan on 14.10.2015.
 */
public class FireKeyListener extends KeyAdapter {

    private AbstractTank tank;
    private volatile boolean isKeyPressed;
    private KeysListenerThread klt;


    public FireKeyListener (KeysListenerThread klt, AbstractTank tank) {
        this.klt = klt;
        this.tank = tank;

    }

    public boolean isKeyPressed() {
        return isKeyPressed;
    }

    public void setIsKeyPressed(boolean isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == 32 && isKeyPressed() == false) {
            setIsKeyPressed(true);
            tank.addAction(Action.FIRE);
            klt.checkIfMoveKeyPressed();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 32) {
            setIsKeyPressed(false);
        }
    }
}
