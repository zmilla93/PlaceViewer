package com.zrmiller.gui;

import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class PlayerControlPanel extends JPanel {

    private PlacePlayer player;

    JButton playButton = new JButton("Play");
    JButton pauseButton = new JButton("Pause");
    JButton resetButton = new JButton("Reset");

    public PlayerControlPanel(PlacePlayer player) {
        this.player = player;
        setLayout(new GridBagLayout());

        GridBagConstraints gc = ZUtil.getGC();
        add(pauseButton, gc);
        gc.gridx++;
        add(playButton);
        gc.gridx++;
        add(resetButton);

        addListeners();
    }

    private void addListeners() {
        playButton.addActionListener(e -> player.play());
        pauseButton.addActionListener(e -> player.pause());
        resetButton.addActionListener(e -> player.reset());
    }

}
