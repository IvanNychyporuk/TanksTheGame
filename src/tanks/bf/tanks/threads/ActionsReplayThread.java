package tanks.bf.tanks.threads;

import tanks.ActionField;
import tanks.bf.tanks.ActionEntry;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ivan on 30.12.2015.
 */
public class ActionsReplayThread implements Runnable {

    private ActionField af;
    private Timer aggressorTimer;
    private Timer defenderTimer;

    public ActionsReplayThread(ActionField af) {
        this.af = af;
        aggressorTimer = new Timer();
        defenderTimer = new Timer();
    }

    private TimerTask createAggressorTask(ActionEntry ae) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    af.processAction(ae.getAction(), af.aggressor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private TimerTask createDefenderTask(ActionEntry ae) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    af.processAction(ae.getAction(), af.defender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    @Override
    public void run() {

        while (af.tanksActions.size() > 0) {
            ActionEntry ae = af.tanksActions.poll();

            if (ae.getTank().getClass().getSimpleName().equals(af.aggressor.getClass().getSimpleName())) {
                aggressorTimer.schedule(createAggressorTask(ae), ae.getActionTime());
            } else {
                defenderTimer.schedule(createDefenderTask(ae), ae.getActionTime() + 600);
            }
        }
    }

}
