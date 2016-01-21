package tanks;

import tanks.bf.BattleField;
import tanks.bf.tanks.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Ivan on 25.12.2015.
 */
public class ReplayReader {

    private List<String> actionsFromFile;
    private Queue<ActionEntry> replayActions;
    private final String ACTIONSFILENAME = "actions/actions.txt";
    private File actionsFile;

    private ActionField af;
    private BattleField bf;

    private String defenderName;
    private String aggressorName;
    private String[] startPositions = new String[2];

    public ReplayReader(ActionField af, BattleField bf) {
        this.af = af;
        this.bf = bf;
        defenderName = af.defender.getClass().getSimpleName();
        aggressorName = af.aggressor.getClass().getSimpleName();
        actionsFile = new File(ACTIONSFILENAME);
        readActionsFile();
        locateStartPositions();
    }

    public String getDefenderName() {
        return defenderName;
    }

    public String getAggressorName() {
        return aggressorName;
    }

    protected void readActionsFile() {

        String str;
        List<String> actions = new ArrayList<>();

        try (
                FileInputStream fis = new FileInputStream(actionsFile);
                InputStreamReader reader = new InputStreamReader(fis);
                BufferedReader bReader = new BufferedReader(reader);
        ) {
            while ((str = bReader.readLine()) != null) {
                actions.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        actionsFromFile = actions;
    }

    private void locateStartPositions() {

        for (int i = 0; i < 2; i++) {

            String startLine = actionsFromFile.get(0);
            startPositions[i] = startLine.substring(startLine.indexOf(':') + 2, startLine.indexOf('('));
            actionsFromFile.remove(0);

        }
    }

    public String[] getStartPositions() {
        return startPositions;
    }

    public Queue<ActionEntry> getReplayActions() {

        replayActions = new ConcurrentLinkedQueue<>();

        for (String s : actionsFromFile) {

            if (s.substring(0,9).equals("GAME OVER")) {
                break;
            }

            String tank = s.substring(0, s.indexOf(':'));
            String action = s.substring(s.indexOf(':') + 2, s.indexOf('('));
            String time = s.substring(s.indexOf('(') + 1, s.indexOf(')'));

            ActionEntry ae = new ActionEntry();
            ae.setTank(specifyTank(tank));
            ae.setAction(specifyAction(action));
            ae.setActionTime(Long.parseLong(time));

            replayActions.add(ae);
        }

        return replayActions;
    }

    private Tank specifyTank (String tank) {

        switch (tank) {
            case "T34": return new T34(af,bf);
            case "Tiger": return new Tiger(af,bf);
            case "BT7": return new BT7(af,bf);
        }
        return null;
    }

    private Action specifyAction(String action) {

        if (action.substring(0,4).equals("MOVE")) {
            action = "MOVE";
        } else if (action.substring(0,4).equals("FIRE")) {
            action = "FIRE";
        }

        switch (action) {
            case "MOVE": return Action.MOVE;
            case "FIRE": return Action.FIRE;
            case "TURN_UP": return Action.TURN_UP;
            case "TURN_DOWN": return Action.TURN_DOWN;
            case "TURN_LEFT": return Action.TURN_LEFT;
            case "TURN_RIGHT": return Action.TURN_RIGHT;
        }

        return null;
    }

}
