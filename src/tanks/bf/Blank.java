package tanks.bf;

import java.awt.Color;

public class Blank extends SimpleBFObject {

    public Blank(int x, int y, BattleField bf) {
        super(x, y, bf);
        iBFObject = bf.imageLoader.iGround;
        color = new Color(180, 180, 180, 5);
    }
}
