package com.zrmiller.gui.mainframe;

import com.zrmiller.core.enums.PlaybackSpeed;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.mainframe.listeners.IPlayerControllerListener;
import com.zrmiller.modules.colortheme.components.FlatColorIconButton;
import com.zrmiller.modules.listening.ListenManagerPanel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Objects;

public class PlayerControlPanel extends ListenManagerPanel<IPlayerControllerListener> {

    private JButton resetButton = new FlatColorIconButton("/icons/media-stop.png");
    private JButton playButton = new FlatColorIconButton("icons/media-play.png");
    private JButton pauseButton = new FlatColorIconButton("icons/media-pause.png");
    private JSlider speedSlider = new JSlider();
    private JLabel speedLabel = new JLabel();
    private JComboBox<PlaybackSpeed> speedCombo = new JComboBox<>();

    public PlayerControlPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        for (PlaybackSpeed speed : PlaybackSpeed.values()) {
            speedCombo.addItem(speed);
        }

        add(speedCombo);
        gc.gridx++;
        add(speedSlider);
        gc.gridx++;
        add(playButton);
        gc.gridx++;
        add(pauseButton, gc);
        gc.gridx++;
        add(resetButton);
        gc.gridx++;
        add(speedLabel);
        gc.gridx++;
        speedSlider.setPreferredSize(new Dimension(100, getPreferredSize().height));
        addListeners();
        speedCombo.setSelectedItem(PlaybackSpeed.FAST);
        speedSlider.setValue(100000);
    }

    private void setPlaybackSpeed(PlaybackSpeed speed) {
        speedSlider.setMinimum(speed.MINIMUM_SPEED);
        speedSlider.setMaximum(speed.MAXIMUM_SPEED);
    }

    private void addListeners() {
        resetButton.addActionListener(e -> {
            for (IPlayerControllerListener listener : listeners) {
                listener.onReset();
            }
        });
        playButton.addActionListener(e -> {
            for (IPlayerControllerListener listener : listeners) {
                listener.onPlay();
            }
        });
        pauseButton.addActionListener(e -> {
            for (IPlayerControllerListener listener : listeners) {
                listener.onPause();
            }
        });
        speedSlider.addChangeListener(e -> {
            for (IPlayerControllerListener listener : listeners) {
                listener.onSpeedChange(speedSlider.getValue());
            }
        });
        speedCombo.addActionListener(e -> setPlaybackSpeed((PlaybackSpeed) Objects.requireNonNull(speedCombo.getSelectedItem())));
//        spee.addActionListener(e -> {
//            for(IPlayerControllerListener listener : listeners){
//                listener.onReset();
//            }
//        });

//        resetButton.addActionListener(e -> player.stop());
//        playButton.addActionListener(e -> player.play());
//        pauseButton.addActionListener(e -> player.pause());
//        speedCombo.addActionListener(e -> setPlaybackSpeed((PlaybackSpeed) Objects.requireNonNull(speedCombo.getSelectedItem())));
        speedSlider.addChangeListener(e -> {
            int speed = speedSlider.getValue();
            String info = speed > 1 ? "Tiles per Second" : "Tile per second";
            speedLabel.setText(NumberFormat.getInstance().format(speed) + " " + info);
//            player.setSpeed(speedSlider.getValue());
        });
    }

}
