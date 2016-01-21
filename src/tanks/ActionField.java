package tanks;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import tanks.bf.*;
import tanks.bf.tanks.*;
import tanks.bf.tanks.threads.*;

public class ActionField extends JPanel {

    public JFrame frame;
    protected volatile boolean gameIsOver;
    private boolean isReplay;
    public BattleField battleField;
    public T34 defender;
    public Tank aggressor;
    private Bullet bulletDefender;
    private Bullet bulletAgressor;
    private ActionsWriter writer = new ActionsWriter();
    private ExecutorService fireExecutor;
    private ExecutorService actionExecutor;
    private ExecutorService repaintExecutor;
    public Queue<ActionEntry> tanksActions = new ConcurrentLinkedQueue<>();
    private ReplayReader replayReader;

    public boolean isReplay() {
        return isReplay;
    }

    public void setIsReplay(boolean isReplay) {
        this.isReplay = isReplay;
    }

    public void setBulletDefender(Bullet bulletDefender) {
        this.bulletDefender = bulletDefender;
    }

    public void setBulletAgressor(Bullet bulletAgressor) {
        this.bulletAgressor = bulletAgressor;
    }

    public Bullet getBulletDefender() {
        return bulletDefender;
    }

    public Bullet getBulletAgressor() {
        return bulletAgressor;
    }

    public void runTheGame() throws Exception {

        setIsReplay(false);
        writer.clearActionsFile();
        writer.writeAction("T34 starts at: " + defender.getX() + "_" + defender.getY());
        writer.writeAction(aggressor.getClass().getSimpleName() + " starts at: " + battleField.getAggressorStartLocation());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        actionExecutor = Executors.newFixedThreadPool(2);
        fireExecutor = Executors.newFixedThreadPool(2);
        repaintExecutor = Executors.newSingleThreadExecutor();
        try {
            repaintExecutor.submit(new ThreadRepaint(this));
            executor.submit(new TankThread(aggressor, this));
            executor.submit(new DefenderThread(defender, this));
            actionExecutor.submit(new ActionsThread(aggressor, this));
            actionExecutor.submit(new ActionsThread(defender, this));
            while (!isGameIsOver()) {
                Thread.sleep(100);
            }

        } finally {
            writer.writeAction("GAME OVER");
            executor.shutdown();
            fireExecutor.shutdown();
            actionExecutor.shutdown();
            repaintExecutor.shutdown();
        }

    }

    public void addActionToQueue(Action action, Tank tank) {
        ActionEntry ae = new ActionEntry();
        ae.setAction(action);
        ae.setTank(tank);
        tanksActions.add(ae);
    }


    public void prepareForReplay() throws Exception {

        setIsReplay(true);
        setGameIsOver(false);
        battleField = new BattleField();
        replayReader = new ReplayReader(this, battleField);

        String defenderStart = replayReader.getStartPositions()[0];
        String aggressorStart = replayReader.getStartPositions()[1];

        String defenderName = replayReader.getDefenderName();
        String aggressorName = replayReader.getAggressorName();

        int defenderX = Integer.parseInt(defenderStart.substring(0, defenderStart.indexOf('_')));
        int defenderY = Integer.parseInt(defenderStart.substring(defenderStart.indexOf('_') + 1));

        int aggressorX = Integer.parseInt(aggressorStart.substring(0, aggressorStart.indexOf('_')));
        int aggressorY = Integer.parseInt(aggressorStart.substring(aggressorStart.indexOf('_') + 1));

        System.out.println("REPLAY");

        System.out.println(defenderName + " starts at: " + defenderStart);
        System.out.println(aggressorName + " starts at: " + aggressorStart);

        int[] tanksCoordinates = {defenderX, defenderY, aggressorX, aggressorY};

        prepareToStart(defenderName, aggressorName, tanksCoordinates);

        tanksActions = replayReader.getReplayActions();
    }

    public void performReplayAction() throws Exception {

        actionExecutor = Executors.newSingleThreadExecutor();
        fireExecutor = Executors.newFixedThreadPool(2);
        repaintExecutor = Executors.newSingleThreadExecutor();
        try {
            repaintExecutor.submit(new ThreadRepaint(this));
            actionExecutor.submit(new ActionsReplayThread(this));
            while (!isGameIsOver()) {
                Thread.sleep(1000);
            }
        } finally {
            fireExecutor.shutdown();
            actionExecutor.shutdown();
            repaintExecutor.shutdown();
        }

    }

    public void processAction(Action action, Tank t) throws Exception {

        t.setTankActing(true);
        String consoleMessage = t.getClass().getSimpleName() + ": ";
        if (action == Action.MOVE) {
            consoleMessage += "MOVE " + t.getDirection();
            workUpConsoleMessage(consoleMessage);
            processMove(t);
        } else if (action == Action.FIRE) {
            consoleMessage += "FIRE " + t.getDirection();
            workUpConsoleMessage(consoleMessage);
            processFire(t);
        } else {
            consoleMessage += action.toString();
            workUpConsoleMessage(consoleMessage);
            t.turn(action);
            t.setTankActing(false);

        }

    }

    private void workUpConsoleMessage(String consoleMessage) {
        if (isReplay) {
            System.out.println(consoleMessage);
        } else {
            writer.writeAction(consoleMessage);
        }
    }

    private void processMove(Tank tank) throws Exception {

        Direction direction = tank.getDirection();
        int step = 1;

        int covered = 0;

        String tankQuadrant = getQuadrant(tank.getX(), tank.getY());
        int v = Integer.parseInt(tankQuadrant.split("_")[0]);
        int h = Integer.parseInt(tankQuadrant.split("_")[1]);

        if (direction == Direction.UP) {
            v--;
        } else if (direction == Direction.DOWN) {
            v++;
        } else if (direction == Direction.RIGHT) {
            h++;
        } else if (direction == Direction.LEFT) {
            h--;
        }

        if (checkQuadrant(battleField.scanQuadrant(v,h),tank,direction)) {
            return;
        }

        while (covered < 64) {
            if (direction == Direction.UP) {
                tank.updateY(-step);
            } else if (direction == Direction.DOWN) {
                tank.updateY(step);
            } else if (direction == Direction.LEFT) {
                tank.updateX(-step);
            } else {
                tank.updateX(step);
            }
            covered += step;

            Thread.sleep(tank.getSpeed());
        }

        tank.setTankActing(false);
    }

    private boolean checkQuadrant(SimpleBFObject object, Tank tank, Direction direction) {

        if (object == null) {
            System.out.println("[illegal move border ahead] direction: " + direction
                    + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
            tank.refreshActions();
            tank.setTankActing(false);
            return true;
        } else if (object instanceof Destroyable) {
            Destroyable bfobject = (Destroyable) object;
            if (!bfobject.isDestroyed()) {
                System.out.println("[illegal move obstruction ahead] direction: " + direction
                        + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
                tank.refreshActions();
                tank.setTankActing(false);
                return true;
            }
        }

        return false;
    }

    public void processFire(Tank tank) throws Exception {

        if (!fireExecutor.isShutdown()) {
            fireExecutor.submit(new BulletThread(tank.fire(), this));
        }

    }

    public void processFire(Bullet bullet) throws Exception {

        int step = 1;
        while ((bullet.getX() > -14 && bullet.getX() < 590)
                && (bullet.getY() > -14 && bullet.getY() < 590)) {
            if (bullet.getDirection() == Direction.UP) {
                bullet.updateY(-step);
            } else if (bullet.getDirection() == Direction.DOWN) {
                bullet.updateY(step);
            } else if (bullet.getDirection() == Direction.LEFT) {
                bullet.updateX(-step);
            } else {
                bullet.updateX(step);
            }
            if (processInterception(bullet) || checkBulletOutOfField(bullet)) {
                bullet.destroy();
            }

            Thread.sleep(bullet.getSpeed());
            if (bullet.isDestroyed()) {
                break;
            }
        }
        bullet.getTank().setTankActing(false);
    }

    private boolean checkBulletOutOfField(Bullet bullet) {
        return (bullet.getX() < -13 || bullet.getX() > 589 || bullet.getY() < -13 || bullet.getY() > 589);
    }

    public boolean processInterception(Bullet bullet) {
        String coordinates = getQuadrant(bullet.getX(), bullet.getY());
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        if (y >= 0 && y < 9 && x >= 0 && x < 9) {
            if (battleField.scanQuadrant(y, x) instanceof Destroyable) {
                Destroyable bfObject = (Destroyable) battleField.scanQuadrant(y, x);
                if (!bfObject.isDestroyed() && !(bfObject instanceof Rock)) {
                    battleField.destroyObject(y, x);
                    return true;
                } else if (!bfObject.isDestroyed() && (bullet.getTank() instanceof Tiger)) {
                    battleField.destroyObject(y, x);
                    return true;
                } else if (!bfObject.isDestroyed() && bfObject instanceof Rock && !(bullet.getTank() instanceof Tiger)) {
                    return true;
                }
            }

            int bulletsX = Math.abs(bulletAgressor.getX() - bulletDefender.getX());
            int bulletsY = Math.abs(bulletAgressor.getY() - bulletDefender.getY());

            if (bulletsX < 10 && bulletsY < 10) {
                return true;
            }

            if (bullet.getTank() instanceof T34) {
                return checkAgressorInterception(coordinates);
            } else {
                return checkDefenderInterception(coordinates);
            }
        }
        return false;
    }

    private boolean checkDefenderInterception(String coordinates) {
        if (!defender.isDestroyed() && checkInterception(getQuadrant(defender.getX(), defender.getY()), coordinates)) {
            defender.destroy();
            return true;
        }
        return false;
    }


    private boolean checkAgressorInterception(String coordinates) {
        if (!aggressor.isDestroyed() && checkInterception(getQuadrant(aggressor.getX(), aggressor.getY()), coordinates)) {
            aggressor.destroy();
            return true;
        }
        return false;
    }

    private boolean checkInterception(String object, String quadrant) {

        int oy = Integer.parseInt(object.split("_")[0]);
        int ox = Integer.parseInt(object.split("_")[1]);

        int qy = Integer.parseInt(quadrant.split("_")[0]);
        int qx = Integer.parseInt(quadrant.split("_")[1]);

        if (oy >= 0 && oy < 9 && ox >= 0 && ox < 9) {
            if (oy == qy && ox == qx) {
                return true;
            }
        }
        return false;
    }

    public String getQuadrant(int x, int y) {
        return y / 64 + "_" + x / 64;
    }

    public boolean isGameIsOver() {
        if (aggressor.isDestroyed() || defender.isDestroyed() || battleField.eagleIsDestroyed()) {
            setGameIsOver(true);
        }
        return gameIsOver;
    }

    public void setGameIsOver(boolean gameIsOver) {
        if (gameIsOver && isReplay()) {
            System.out.println("GAME OVER");
        }
        this.gameIsOver = gameIsOver;
    }


    private void prepareToStart(String defenderName, String aggressorName, int[] tanksCoordinates) {

        defender = new T34(this, battleField, tanksCoordinates[0], tanksCoordinates[1], Direction.UP);
        battleField.setDefender(defender);

        if (aggressorName.equals("Tiger")) {
            aggressor = new Tiger(this, battleField,
                    tanksCoordinates[2], tanksCoordinates[3], Direction.RIGHT);

        } else if (aggressorName.equals("BT7")) {
            aggressor = new BT7(this, battleField,
                    tanksCoordinates[2], tanksCoordinates[3], Direction.RIGHT);
        }

        battleField.setAgressor(aggressor);

        bulletDefender = new Bullet(-100, -100, Direction.NONE, defender);
        bulletAgressor = new Bullet(-100, -100, Direction.NONE, aggressor);

        frame = new JFrame("ULTIMATE BATTLE FIELD");
        frame.setLocation(300, 100);
        frame.setMinimumSize(new Dimension(battleField.getBfWidth(), battleField.getBfHeight() + 22));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
    }


    public ActionField() throws Exception {
        this("BT7");
    }

    public ActionField(String aggressorName) throws Exception {
//            prepareToStart(agressorName);
        battleField = new BattleField();
//        defender = new T34(this, battleField);
//        battleField.setDefender(defender);

        String location = battleField.getAggressorStartLocation();

        int[] tanksCoordinates = {128, 512, Integer.parseInt(location.split("_")[0]),
                Integer.parseInt(location.split("_")[1])};

        prepareToStart("T34", aggressorName, tanksCoordinates);

// if (agressorName.equals("Tiger")) {
//            aggressor = new Tiger(this, battleField,
//                    Integer.parseInt(location.split("_")[0]), Integer.parseInt(location.split("_")[1]), Direction.RIGHT);
//
//        } else if (agressorName.equals("BT7")) {
//            aggressor = new BT7(this, battleField,
//                    Integer.parseInt(location.split("_")[0]), Integer.parseInt(location.split("_")[1]), Direction.RIGHT);
//        }
//
//        battleField.setAgressor(aggressor);
//
//        bulletDefender = new Bullet(-100, -100, Direction.NONE, defender);
//        bulletAgressor = new Bullet(-100, -100, Direction.NONE, aggressor);
//
//        frame = new JFrame("ULTIMATE BATTLE FIELD");
//        frame.setLocation(300, 100);
//        frame.setMinimumSize(new Dimension(battleField.getBfWidth(), battleField.getBfHeight() + 22));
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.getContentPane().add(this);
//        frame.pack();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        battleField.draw(g);
        defender.draw(g);
        aggressor.draw(g);
        bulletAgressor.draw(g);
        bulletDefender.draw(g);
        battleField.drawWater(g);

        if (isReplay) {
            drawR(g);
        }

    }

    private void drawR(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        float opacity = 0.7f;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.setColor(Color.RED);
        g2d.setFont(new Font(null, 1, 60));
        g2d.drawString("R", 50, 90);
    }
}