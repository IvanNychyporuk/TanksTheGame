package tanks.bf.tanks.threads;

import tanks.ActionField;
import tanks.bf.tanks.AbstractTank;
import tanks.bf.tanks.Action;
import tanks.bf.tanks.ActionEntry;
import tanks.bf.tanks.Tank;

public class ActionsThread implements Runnable {

    private ActionField af;
    private Tank tank;

    public ActionsThread(Tank tank, ActionField af) {
        this.tank = tank;
        this.af = af;
    }

    @Override
    public void run() {

        while (!af.isGameIsOver()) {

            try {
                if (af.tanksActions.size() > 0 && af.tanksActions.element().getTank().equals(tank)) {

                    ActionEntry ae = af.tanksActions.poll();
                    Action action = ae.getAction();
                    Tank tank = ae.getTank();

                    while (tank.isTankActing()) {
                        Thread.currentThread().sleep(10);
                    }

                    af.processAction(action, tank);

                    Thread.sleep(50);

                } else {
                    Thread.currentThread().sleep(10);
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

    }
}
