package com.zrmiller.gui;

import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class WindowContainer extends JPanel implements ICanvasListener {

    private CanvasPanel canvasPanel = new CanvasPanel();
    private JPanel sidebarPanel = new JPanel(new GridBagLayout());
    private JPanel southPanel = new JPanel(new BorderLayout());
    private JLabel positionLabel = new JLabel("Pos -");
    private JLabel frameLabel = new JLabel("Unloaded");
    private JLabel zoomLabel = new JLabel("100%");

    public WindowContainer() {
        setLayout(new BorderLayout());
        // Panels
        buildPanels();
        addPanels();

        onPan(new Point(0, 0));
        canvasPanel.addListener(this);
    }

    private void buildPanels() {

        sidebarPanel.add(new JButton("ASJFKLSDJAF"));
        GridBagConstraints gc = ZUtil.getGC();

        // South Panel
        JPanel southRightPanel = new JPanel();
        southRightPanel.add(zoomLabel);
        southPanel.add(positionLabel, BorderLayout.CENTER);
        southPanel.add(frameLabel, BorderLayout.WEST);
        southPanel.add(southRightPanel, BorderLayout.EAST);
    }

    private void addPanels() {
        add(sidebarPanel, BorderLayout.WEST);
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
}
