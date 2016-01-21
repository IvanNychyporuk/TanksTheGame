package tanks.bf.tanks.threads;

import tanks.ActionField;
import tanks.bf.tanks.T34;

public class DefenderThread implements Runnable {
    private T34 tank;
    private ActionField af;

    public DefenderThread(T34 tank, ActionField af) {
        this.tank = tank;
        this.af = af;
    }


    @Override
    public void run() {
        try {
            while (!tank.isDestroyed() && !af.isGameIsOver()) {

                if (tank.actionsSize() > 0) {

                    tank.pushAction();

                }

                Thread.sleep(20);

                if (af.isGameIsOver()) {
                    System.out.println(tank.getClass().getSimpleName() + " says: game is over");
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
