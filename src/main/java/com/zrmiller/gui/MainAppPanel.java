package com.zrmiller.gui;

import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class MainAppPanel extends JPanel implements ICanvasListener {

    private final CanvasPanel canvasPanel = new CanvasPanel();

    private final JPanel sidebarPanel = new JPanel(new BorderLayout());
    private final JPanel southPanel = new JPanel(new BorderLayout());
    private final JLabel positionLabel = new JLabel("Pos -");
    private final JLabel frameCountLabel = new JLabel("-");
    private final JLabel zoomLabel = new JLabel("100%");

    private final JPanel northPanel = new JPanel(new BorderLayout());

    public MainAppPanel() {
        setLayout(new BorderLayout());
        buildPanels();

        onPan(new Point(0, 0));
        canvasPanel.addListener(this);
    }

    private void buildPanels() {
        // Sidebar???
        sidebarPanel.add(new JButton("ASJFKLSDJAF"), BorderLayout.CENTER);
        sidebarPanel.add(new SeparatorPanel(), BorderLayout.EAST);
        GridBagConstraints gc = ZUtil.getGC();

        // North Panel
//        northPanel.add(menubar, BorderLayout.NORTH);

        northPanel.add(new PlayerControlPanel(canvasPanel.getPlayer()), BorderLayout.WEST);
        northPanel.add(new jumpToFramePanel(canvasPanel, canvasPanel.getPlayer()), BorderLayout.EAST);
//        northPanel.add(speedLabelPanel, BorderLayout.EAST);

        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(northPanel, BorderLayout.CENTER);
//        northContainer.add(new SeparatorPanel(), BorderLayout.SOUTH);
        northContainer.add(new SeparatorPanel(), BorderLayout.SOUTH);


        // South Panel
        JPanel southRightPanel = new JPanel();
        southRightPanel.add(zoomLabel);
//        southPanel.add(positionLabel, BorderLayout.CENTER);
        southPanel.add(frameCountLabel, BorderLayout.WEST);
        southPanel.add(southRightPanel, BorderLayout.EAST);
        southPanel.add(new SeparatorPanel(), BorderLayout.NORTH);

        // Main Panel
        add(northContainer, BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
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
