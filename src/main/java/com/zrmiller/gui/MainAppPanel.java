package com.zrmiller.gui;

import com.zrmiller.core.utility.PlaceInfo;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class MainAppPanel extends JPanel implements ICanvasListener {

    private final CanvasPanel canvasPanel = new CanvasPanel();

    private final JPanel southPanel = new JPanel(new BorderLayout());
    private final JLabel positionLabel = new JLabel("Pos -");
    private final JLabel frameCountLabel = new JLabel("-");
    private final JLabel zoomLabel = new JLabel("100%");

    private final JPanel northPanel = new JPanel(new BorderLayout());

    public MainAppPanel() {
        setLayout(new BorderLayout());

        // North Panel
        JPanel northBuffer = new JPanel(new BorderLayout());
        northBuffer.add(new PlayerControlPanel(canvasPanel.getPlayer()), BorderLayout.WEST);
        northBuffer.add(new jumpToFramePanel(canvasPanel, canvasPanel.getPlayer()), BorderLayout.EAST);

        northPanel.add(northBuffer, BorderLayout.CENTER);
        northPanel.add(new SeparatorPanel(), BorderLayout.SOUTH);

        // South Panel
        JPanel southRightPanel = new JPanel();
        southRightPanel.add(zoomLabel);
        southPanel.add(frameCountLabel, BorderLayout.WEST);
        southPanel.add(southRightPanel, BorderLayout.EAST);
        southPanel.add(new SeparatorPanel(), BorderLayout.NORTH);

        // Main Panel
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(canvasPanel, BorderLayout.CENTER);

        onPan(new Point(0, 0));
        addListeners();
    }

    private void addListeners() {
        canvasPanel.addListener(this);
    }

    @Override
    public void onZoom(int zoomLevel) {
        zoomLabel.setText(100 * zoomLevel + "%");
    }

    @Override
    public void onPan(Point point) {
        positionLabel.setText("Pos " + point.x + ", " + point.y);
    }

    @Override
    public void onDraw(int frameCount) {
        frameCountLabel.setText("Frame " + NumberFormat.getInstance().format(frameCount) + " / " + PlaceInfo.CLEAN_LINE_COUNT_FORMATTED);
    }

}
