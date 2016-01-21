package tanks.bf;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Rock extends SimpleBFObject implements Destroyable {
    private boolean isDestroyed;

    public Rock(int x, int y, BattleField bf) {
        super(x, y, bf);
        iBFObject = bf.imageLoader.iRock;
    }

    @Override
    public void draw(Graphics g) {
        if (isDestroyed()) {
            iBFObject = bf.imageLoader.iCrushedRock;
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


    @Override
    public void destroy() {
        isDestroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
