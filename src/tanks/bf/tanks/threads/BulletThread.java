package tanks.bf.tanks.threads;

import tanks.ActionField;
import tanks.bf.tanks.Bullet;
import tanks.bf.tanks.T34;

public class BulletThread implements Runnable {
    private ActionField af;
    private Bullet bullet;
    private boolean allowFire = false;

    public BulletThread(Bullet bullet, ActionField af) {
        this.af = af;
        this.bullet = bullet;
    }


    @Override
    public void run() {
        if (bullet.getTank() instanceof T34 && af.getBulletDefender().isDestroyed()) {
            af.setBulletDefender(bullet);
            allowFire = true;
        } else if (!(bullet.getTank() instanceof T34) && af.getBulletAgressor().isDestroyed()){
            af.setBulletAgressor(bullet);
            allowFire = true;
        }

        try {
            if (!bullet.getTank().isDestroyed() && allowFire && !af.isGameIsOver()) {
                bullet.setDestroyed(false);
                af.processFire(bullet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
