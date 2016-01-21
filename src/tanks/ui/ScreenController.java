package tanks.ui;

import tanks.ActionField;
import tanks.bf.tanks.threads.KeysListenerThread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScreenController {

    private StartScreen ss;
    private GameOverScreen gos;
    private ActionField af;
    private ExecutorService afExecutor;
    private ExecutorService keysListenerExecutor;
    private String aggressorName;

    public ScreenController() {
       createStartScreen();
    }

    private void createStartScreen() {
        ss = new StartScreen();
        ss.setStartButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            aggressorName = ss.getAggressorName();
                            af = new ActionField(aggressorName);
                            switchPanelToActonField();
                            startKeyListenerThread();
                            af.runTheGame();
                            checkIsGameIsOver();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                afExecutor = Executors.newSingleThreadExecutor();
                afExecutor.submit(thread);
            }
        });
        ss.getMainPanel().setVisible(true);
    }

    private void checkIsGameIsOver() throws InterruptedException {

        while (!af.isGameIsOver()) {
            Thread.currentThread().sleep(1000);
        }
        showGameOverScreen();
    }

    private void startKeyListenerThread() {
        keysListenerExecutor = Executors.newSingleThreadExecutor();
        keysListenerExecutor.submit(new KeysListenerThread(af));
    }

    private void switchPanelToActonField() {
        ss.frame.setContentPane(af);
        ss.frame.setVisible(true);
        ss.frame.setAlwaysOnTop(true);
        ss.frame.pack();
    }

    private void switchPanelToStartScreen(){

        gos.frame.setVisible(false);
        ss.frame.setVisible(false);

        afExecutor.shutdown();
        keysListenerExecutor.shutdown();

        createStartScreen();
    }

    private void switchPanelToReplayScreen() {

        gos.frame.setVisible(false);

        afExecutor.shutdown();
        keysListenerExecutor.shutdown();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    af.prepareForReplay();
                    af.repaint();
                    switchPanelToActonField();
                    af.performReplayAction();
                    checkIsGameIsOver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        afExecutor = Executors.newSingleThreadExecutor();
        afExecutor.submit(thread);

    }

    private String getGameResult() {

        if (af.defender.isDestroyed()){
            return "You were killed!";
        }
        if (af.aggressor.isDestroyed()){
            return "You killed the enemy!";
        }
        if (af.battleField.eagleIsDestroyed()){
            return "Your HQ is destroyed!";
        }
        return null;
    }

    private void showGameOverScreen() {

        gos = new GameOverScreen();
        gos.setGameResultText(getGameResult());

        gos.setPlayAgainButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanelToStartScreen();
            }
        });

        gos.setShowReplayButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                  switchPanelToReplayScreen();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        gos.frame.setVisible(true);
    }

}
