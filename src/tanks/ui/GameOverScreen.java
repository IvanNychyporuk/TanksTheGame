package tanks.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Ivan on 05.01.2016.
 */
public class GameOverScreen extends JPanel {

    protected JFrame frame;
    private JPanel p;
    private JLabel gameResultText;
    private JLabel playAgainQuestion;
    private JButton playAgainButton;
    private JButton showReplay;

    public GameOverScreen(){

        frame = new JFrame("Game Over");
        frame.setBounds(400, 250, 400, 300);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gameResultText = new JLabel();
        gameResultText.setFont(new Font("Arial", 0, 20));

        playAgainQuestion = new JLabel();
        playAgainQuestion.setFont(new Font("Arial", 0, 16));

        playAgainQuestion.setText("Do you want to retry?");
        playAgainButton = new JButton("Play again");
        showReplay = new JButton("Show replay");

        p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        p.add(gameResultText,c);
        c.gridy = 1;
        p.add(playAgainQuestion,c);
        c.gridy = 2;
        c.gridwidth = 1;
        p.add(playAgainButton, c);
        c.gridx = 1;
        p.add(showReplay, c);

        frame.add(p);
        frame.setAlwaysOnTop(true);
//        frame.setVisible(true);

    }

    public void setPlayAgainButtonListener(ActionListener listener) {
        playAgainButton.addActionListener(listener);
    }

    public void setShowReplayButtonListener(ActionListener listener) {
        showReplay.addActionListener(listener);
    }

    public void setGameResultText(String result) {
        this.gameResultText.setText(result);
    }

    public String getGameResultText() {
        return gameResultText.getText();
    }

}
