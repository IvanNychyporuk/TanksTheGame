package tanks.bf;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Brick extends SimpleBFObject implements Destroyable {
    private volatile boolean isDestroyed;


    public Brick(int x, int y, BattleField bf) {
        super(x, y, bf);
        iBFObject = bf.imageLoader.iBrick;
        isDestroyed = false;
    }

    public void destroy() {
        isDestroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void draw(Graphics g) {
        if (isDestroyed) {
            iBFObject = bf.imageLoader.iCrushedBrick;
            g.drawImage(iBFObject, x, y, 64, 64, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
        } else {
            super.draw(g);
        }

    }
}
