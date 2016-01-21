package tanks.bf.tanks.threads;

import tanks.ActionField;

public class ThreadRepaint implements Runnable {
    private ActionField af;

    public ThreadRepaint(ActionField af) {
        this.af = af;
    }

    @Override
    public void run() {
        while(!af.isGameIsOver()) {
            try {
                Thread.currentThread().sleep(1000/60);
                af.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
