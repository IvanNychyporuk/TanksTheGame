package tanks.bf;

import tanks.bf.tanks.Direction;
import tanks.bf.tanks.Tank;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Random;

public class BattleField implements Drawable {

    public static final String BRICK = "B";
    public static final String EAGLE = "E";
    public static final String ROCK = "R";
    public static final String WATER = "W";

    private Tank defender;
    private Tank agressor;
    private Eagle eagle;
    public ImageLoader imageLoader = new ImageLoader();
    private String defenderLocation;
    private String agressorStartLocation;
    private Direction defenderDirection;
    private int bfWidth = 592;
    private int bfHeight = 592;


    private String[][] battleFieldTemplate = {
            {"B", "B", "B", "B", "B", "B", "B", "B", "B"},
            {"B", " ", " ", " ", " ", " ", " ", " ", "B"},
            {"B", "B", "B", " ", " ", " ", "B", "B", "B"},
            {"B", " ", "R", "R", " ", " ", " ", " ", "B"},
            {"B", "B", "R", "R", " ", "R", "R", "B", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "B", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "B", "B"},
            {"B", " ", " ", "R", "R", "R", "B", " ", "B"},
            {"B", " ", " ", "B", "E", "B", "B", " ", "B"}
    };

    private SimpleBFObject[][] battleField = new SimpleBFObject[9][9];

    public BattleField() {
        drawBattleField();
    }

    public BattleField(String[][] battleField) {
        this.battleFieldTemplate = battleField;
        drawBattleField();
    }

    public String getQuadrantXY(int v, int h) {
        return (v - 1) * 64 + "_" + (h - 1) * 64;
    }

    public String getQuadrant(int x, int y) {
        // input data should be correct
        return y / 64 + "_" + x / 64;
    }

    private void drawBattleField() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String coordinates = getQuadrantXY(i + 1, j + 1);
                int separator = coordinates.indexOf("_");
                int y = Integer.parseInt(coordinates
                        .substring(0, separator));
                int x = Integer.parseInt(coordinates
                        .substring(separator + 1));

                String obj = battleFieldTemplate[i][j];
                SimpleBFObject bfObject;
                if (obj.equals(BRICK)) {
                    bfObject = new Brick(x, y, this);
                } else if (obj.equals(ROCK)) {
                    bfObject = new Rock(x, y, this);
                } else if (obj.equals(EAGLE)) {
                    bfObject = new Eagle(x, y, this);
                    eagle = (Eagle) bfObject;
                } else if (obj.equals(WATER)) {
                    bfObject = new Water(x, y, this);
                } else {
                    bfObject = new Blank(x, y, this);
                }
                battleField[i][j] = bfObject;
            }
        }
    }

    public boolean eagleIsDestroyed() {
        if (eagle.isDestroyed()) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        drawGround(g);
        for (int j = 0; j < battleField.length; j++) {
            for (int k = 0; k < battleField.length; k++) {
                if (!(battleField[j][k] instanceof Water)) {
                    battleField[j][k].draw(g);
                }
            }
        }
    }

    public void drawGround(Graphics g) {
        g.drawImage(imageLoader.iGround, 0, 0, 585, 585, new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        });
    }

    public void drawWater(Graphics g) {
        for (int j = 0; j < battleField.length; j++) {
            for (int k = 0; k < battleField.length; k++) {
                if (battleField[j][k] instanceof Water) {
                    battleField[j][k].draw(g);
                }
            }
        }
    }

    public void destroyObject(int v, int h) {
        if (battleField[v][h] instanceof Destroyable) {
            Destroyable bfObject = (Destroyable) battleField[v][h];
            bfObject.destroy();
        }
    }

    public SimpleBFObject scanQuadrant(int v, int h) {

        if (v > 8 || v < 0 || h > 8 || h < 0) {
            return null;
        }

        return battleField[v][h];
    }

    public String getAggressorStartLocation() {
        if (agressorStartLocation == null) {
            Random r = new Random();
            int i = r.nextInt(2);
            int k = r.nextInt(2);
            int y = (i == 1) ? 64 : 192;
            int x = 384;
            if (y == 64) {
                x = (k == 1) ? 128 : 384;
            }
            agressorStartLocation = String.valueOf(x) + "_" + String.valueOf(y);
//            System.out.println("agressor: " + agressorStartLocation);
        }
        return agressorStartLocation;
    }

    public String getAgressorLocation() {
        if (agressor == null) {
            getAggressorStartLocation();
            int agressorY = Integer.valueOf(agressorStartLocation.substring(0, agressorStartLocation.indexOf("_")));
            int agressorX = Integer.valueOf(agressorStartLocation.substring(agressorStartLocation.indexOf("_") + 1));

            return getQuadrant(agressorX, agressorY);
        } else {
            return getQuadrant(agressor.getX(), agressor.getY());
        }
    }

    public String getDefenderLocation() {
        return getQuadrant(defender.getX(), defender.getY());
    }


    public Direction getDefenderDirection() {
        return defenderDirection;
    }

    public void setDefenderDirection(Direction defenderDirection) {
        this.defenderDirection = defenderDirection;
    }

    public int getBfWidth() {
        return bfWidth;
    }

    public int getBfHeight() {
        return bfHeight;
    }

    public Tank getDefender() {
        return defender;
    }

    public void setDefender(Tank defender) {
        this.defender = defender;
    }

    public Tank getAgressor() {
        return agressor;
    }

    public void setAgressor(Tank agressor) {
        this.agressor = agressor;
    }
}
