package tanks.bf;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public class Water extends SimpleBFObject {

    private BufferedImage biWater;


    public Water(int x, int y, BattleField bf) {
        super(x, y, bf);
        biWater = bf.imageLoader.iWater;
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        float opacity = 0.6f;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        g2d.drawImage(biWater, x, y, x + 64, y + 64, x, y, x + 64, y + 64, new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        });


    }
}
