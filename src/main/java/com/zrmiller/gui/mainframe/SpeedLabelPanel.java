package com.zrmiller.gui.mainframe;

import javax.swing.*;
import java.text.NumberFormat;

public class SpeedLabelPanel extends JPanel {

    private JLabel label = new JLabel();

    public SpeedLabelPanel() {
        add(label);
        updateSpeed(10000000);
        setPreferredSize(getPreferredSize());
    }

    public void updateSpeed(int speed) {
        String info = speed > 1 ? "Tiles per Second" : "Tile per second";
        label.setText(NumberFormat.getInstance().format(speed) + " " + info);
    }

}
