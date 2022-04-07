package com.zrmiller.gui;

import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.modules.styles.components.IconButton;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class PlayerControlPanel extends JPanel {

    private PlacePlayer player;

    private JButton resetButton = new IconButton("/icons/refresh.png");
    private JButton playButton = new IconButton("icons/media-play.png");
    private JButton pauseButton = new IconButton("icons/media-pause.png");

    private JSlider speedSlider = new JSlider();
    private JLabel speedLabel = new JLabel();

    public PlayerControlPanel(PlacePlayer player) {
        this.player = player;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();

        add(resetButton);
        gc.gridx++;
        add(pauseButton, gc);
        gc.gridx++;
        add(playButton);
        gc.gridx++;
        add(speedSlider);
        gc.gridx++;
        add(speedLabel);
        gc.gridx++;

        addListeners();

        speedSlider.setValue(60);
        speedSlider.setMinimum(60);
        speedSlider.setMaximum(10000000);
    }

    private void addListeners() {
        resetButton.addActionListener(e -> player.reset());
        playButton.addActionListener(e -> player.play());
        pauseButton.addActionListener(e -> player.pause());
        speedSlider.addChangeListener(e -> {
            speedLabel.setText(NumberFormat.getInstance().format(speedSlider.getValue()) + " Tiles Per Second");
            player.setSpeed(speedSlider.getValue());
        });
    }

}
