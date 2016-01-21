package tanks.bf.tanks.threads;

import tanks.ActionField;
import tanks.bf.tanks.Tank;

public class TankThread implements Runnable {
    private Tank tank;
    private ActionField af;

    public TankThread(Tank tank, ActionField af) {
        this.tank = tank;
        this.af = af;
    }

    @Override
    public void run() {
        try {
            while (!af.isGameIsOver() && !tank.isDestroyed()) {

                if (af.tanksActions.size() >= 2 && af.tanksActions.element().getTank().equals(tank)) {
                    Thread.currentThread().sleep(200);
                }
                if (tank.actionsSize() > 0 && !tank.isTankActing()) {
                    tank.pushAction();
                }

                Thread.currentThread().sleep(500);

                if (af.isGameIsOver() && !tank.isDestroyed()) {
                    System.out.println(tank.getClass().getSimpleName() + " says: game is over");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
