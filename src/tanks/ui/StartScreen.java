package tanks.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Ivan on 05.01.2016.
 */
public class StartScreen extends JPanel {

    protected JFrame frame;
    private JPanel p;
    private JLabel chooseEnemyLabel;
    private JRadioButton tigerButton;
    private JRadioButton bt7Button;
    private JButton startButton;

    public StartScreen() {

        frame = new JFrame("Tanks. The game");
        frame.setMinimumSize(new Dimension(592, 614));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        p = new JPanel();
        chooseEnemyLabel = new JLabel("Choose Your Enemy");
        ButtonGroup group = new ButtonGroup();
        tigerButton = new JRadioButton("Tiger");
        tigerButton.setSelected(true);
        bt7Button = new JRadioButton("BT7");
        startButton = new JButton("Start Game");
        group.add(tigerButton);
        group.add(bt7Button);

        p.add(tigerButton);
        p.add(bt7Button);
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20,20,20,20);
        c.gridx = 1;
        c.gridy = 0;
        frame.add(chooseEnemyLabel,c);
        c.insets = new Insets(20,20,20,20);
        c.gridy = 1;
        frame.add(p,c);
        c.gridy = 2;
        frame.add(startButton,c);

//        frame.setAlwaysOnTop(true);
    }

    public String getAggressorName() {
        if (tigerButton.isSelected()) {
            return "Tiger";
        }
        return "BT7";
    }

    public JFrame getMainPanel() {
        return frame;
    }

    public void setStartButtonActionListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }
}
