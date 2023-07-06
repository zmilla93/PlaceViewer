package com.zrmiller.gui.mainframe;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.enums.PlaybackSpeed;
import com.zrmiller.core.managers.DatasetManager;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.mainframe.listeners.IPlayerControllerListener;
import com.zrmiller.modules.colortheme.components.FlatColorIconButton;
import com.zrmiller.modules.listening.ListenManagerPanel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Objects;

public class PlayerControlPanel extends ListenManagerPanel<IPlayerControllerListener> implements IPlayerControllerListener, IDatasetListener {

    private final JButton stopButton = new FlatColorIconButton("/icons/media-stop.png");
    private final FlatColorIconButton playPauseButton = new FlatColorIconButton("/icons/media-play.png", "/icons/media-pause.png");
    private final FlatColorIconButton exportButton = new FlatColorIconButton("/icons/export-outline.png", "/icons/export.png");
    private final JSlider speedSlider = new JSlider();
    private final JLabel speedLabel = new JLabel();
    private final JComboBox<PlaybackSpeed> speedCombo = new JComboBox<>();
    private final JComponent[] components;

    private final JPanel exportPanel;

    public PlayerControlPanel(JPanel exportPanel) {
        this.exportPanel = exportPanel;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        for (PlaybackSpeed speed : PlaybackSpeed.values()) {
            speedCombo.addItem(speed);
        }
        add(Box.createHorizontalStrut(4));
        gc.gridx++;
        add(playPauseButton);
        gc.gridx++;
        add(stopButton);
        gc.gridx++;
        add(exportButton);
        gc.gridx++;
        add(speedCombo);
        gc.gridx++;
        add(speedSlider);
        gc.gridx++;
        add(speedLabel);
        gc.gridx++;

        addListeners();
        playPauseButton.setToolTipText("Play/Pause");
        stopButton.setToolTipText("Stop");
        speedCombo.setToolTipText("Adjust speed slider range");
        exportButton.setToolTipText("Export PNG");
        speedSlider.setToolTipText("Playback speed");
        speedSlider.setPreferredSize(new Dimension(120, getPreferredSize().height));
        speedCombo.setSelectedItem(PlaybackSpeed.FAST);
        speedSlider.setValue(500000);
        components = new JComponent[]{playPauseButton, stopButton, exportButton, speedSlider, speedCombo};
        DatasetManager.addDatasetListener(this);
    }

    private void setPlaybackSpeed(PlaybackSpeed speed) {
        speedSlider.setMinimum(speed.MINIMUM_SPEED);
        speedSlider.setMaximum(speed.MAXIMUM_SPEED);
    }

    private void addListeners() {
        stopButton.addActionListener(e -> {
            for (IPlayerControllerListener listener : listeners) {
                listener.onStop();
            }
        });
        playPauseButton.addActionListener(e -> {
            for (IPlayerControllerListener listener : listeners) {
                listener.onTogglePlayPause();
            }
        });
        exportButton.addActionListener(e -> {
            boolean visible = !exportPanel.isVisible();
            exportPanel.setVisible(visible);
            setExportIcon(visible);
        });
        speedCombo.addActionListener(e -> setPlaybackSpeed((PlaybackSpeed) Objects.requireNonNull(speedCombo.getSelectedItem())));
        speedSlider.addChangeListener(e -> {
            int speed = speedSlider.getValue();
            String info = speed > 1 ? "Tiles per Second" : "Tile per second";
            speedLabel.setText(NumberFormat.getInstance().format(speed) + " " + info);
            for (IPlayerControllerListener listener : listeners) {
                listener.onSpeedChange(speedSlider.getValue());
            }
        });
    }

    public void setExportIcon(boolean visible) {
        if (visible) exportButton.setIconIndex(1);
        else exportButton.setIconIndex(0);
    }

    @Override
    public void onListenerAdded(IPlayerControllerListener listener) {
        super.onListenerAdded(listener);
        listener.onSpeedChange(speedSlider.getValue());
    }

    @Override
    public void onPlay() {
        playPauseButton.setIconIndex(1);
    }

    @Override
    public void onPause() {
        playPauseButton.setIconIndex(0);
    }

    @Override
    public void onStop() {
        // Stop will always trigger pause first, so nothing needs to be done here
    }

    @Override
    public void onTogglePlayPause() {
        // Do nothing
    }

    @Override
    public void onSpeedChange(int tilesPerSecond) {
        // Do nothing
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        boolean enabled = dataset != null;
        if (!enabled) setExportIcon(false);
        for (JComponent component : components) {
            component.setEnabled(enabled);
        }
        requestFocus();
    }

}
