package com.zrmiller.gui;

import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class WindowContainer extends JPanel implements ICanvasListener {

    private CanvasPanel canvasPanel = new CanvasPanel();
    private JPanel sidebarPanel = new JPanel(new BorderLayout());
    private JPanel southPanel = new JPanel(new BorderLayout());
    private JLabel positionLabel = new JLabel("Pos -");
    private JLabel frameCountLabel = new JLabel("-");
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

        sidebarPanel.add(new JButton("ASJFKLSDJAF"), BorderLayout.CENTER);
        sidebarPanel.add(new SeparatorPanel(), BorderLayout.EAST);
        GridBagConstraints gc = ZUtil.getGC();

        // South Panel
        JPanel southRightPanel = new JPanel();
        southRightPanel.add(zoomLabel);
        southPanel.add(positionLabel, BorderLayout.CENTER);
        southPanel.add(frameCountLabel, BorderLayout.WEST);
        southPanel.add(southRightPanel, BorderLayout.EAST);
        southPanel.add(new SeparatorPanel(), BorderLayout.NORTH);
    }

    private void addPanels() {
        add(new PlayerControlPanel(canvasPanel.getPlayer()), BorderLayout.WEST);
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
