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

    //    private JMenuBar menubar = new MainMenuBar();
    private JMenu settingsMenu = new JMenu("Settings");
    private JMenuItem settingsMenuItem = new JMenu("Settings");
    private JMenuItem asdf = new JMenuItem("Settings");
    private JMenuItem aaaa = new JMenuItem("Settings");

    private JPanel northPanel = new JPanel(new BorderLayout());

    public WindowContainer() {
        setLayout(new BorderLayout());
        buildPanels();

        onPan(new Point(0, 0));
        canvasPanel.addListener(this);
    }

    private void buildPanels() {
        // Menubar
//        menubar.add(settingsMenu);
//        settingsMenu.add(settingsMenuItem);
//        settingsMenu.add(new JMenuItem("Whoaaa"));
//        settingsMenu.add(new JButton("HMMM"));
//        settingsMenu.add(new JLabel("wowzers"));

        // Sidebar???
        sidebarPanel.add(new JButton("ASJFKLSDJAF"), BorderLayout.CENTER);
        sidebarPanel.add(new SeparatorPanel(), BorderLayout.EAST);
        GridBagConstraints gc = ZUtil.getGC();

        // North Panel
//        northPanel.add(menubar, BorderLayout.NORTH);

        PlayerControlPanel playerControlPanel = new PlayerControlPanel(canvasPanel.getPlayer());

        northPanel.add(playerControlPanel, BorderLayout.WEST);
        northPanel.add(new jumpToFramePanel(), BorderLayout.EAST);
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
