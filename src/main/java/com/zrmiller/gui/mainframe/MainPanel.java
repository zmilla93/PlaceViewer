package com.zrmiller.gui.mainframe;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.managers.DatasetManager;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.gui.FrameManager;
import com.zrmiller.gui.mainframe.listeners.ICanvasListener;
import com.zrmiller.modules.colortheme.components.SeparatorPanel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class MainPanel extends JPanel implements ICanvasListener, IDatasetListener {

    private final IntroPanel introPanel = new IntroPanel();
    private final PlayerControlPanel controlPanel;
    private final CanvasPanel canvasPanel;

    private final JPanel southPanel = new JPanel(new BorderLayout());
    private final JLabel positionLabel = new JLabel();
    private final JLabel frameCountLabel = new JLabel("-");
    private final JLabel zoomLabel = new JLabel("100%");

    private final JPanel northPanel = new JPanel(new BorderLayout());

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);


    public MainPanel() {
        setLayout(new BorderLayout());

        ExportPanel exportPanel = new ExportPanel();
        controlPanel = new PlayerControlPanel(exportPanel);
        exportPanel.setControlPanel(controlPanel);
        exportPanel.setVisible(false);
        canvasPanel = new CanvasPanel(controlPanel);
        FrameManager.canvasPanel = canvasPanel;

        // North Panel
        JPanel northBuffer = new JPanel(new BorderLayout());
        northBuffer.add(controlPanel, BorderLayout.WEST);
        northBuffer.add(new JumpToFramePanel(canvasPanel.getPlayer()), BorderLayout.EAST);
        northBuffer.add(new JSeparator(), BorderLayout.SOUTH);

        northPanel.add(northBuffer, BorderLayout.CENTER);
//        northPanel.add(new SeparatorPanel(), BorderLayout.SOUTH);
        northPanel.add(exportPanel, BorderLayout.SOUTH);

        // South Panel
        JPanel positionWrapperPanel = new JPanel(new BorderLayout());
        positionWrapperPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        positionWrapperPanel.add(positionLabel, BorderLayout.CENTER);
        southPanel.add(frameCountLabel, BorderLayout.WEST);
        southPanel.add(positionWrapperPanel, BorderLayout.CENTER);
        southPanel.add(zoomLabel, BorderLayout.EAST);
        southPanel.add(new SeparatorPanel(), BorderLayout.NORTH);

        // Card Panel
        cardPanel.add(introPanel, Card.INTRO.toString());
        cardPanel.add(canvasPanel, Card.CANVAS.toString());

        JPanel p = new JPanel();
        p.add(new JLabel("W"));

        // Main Panel
        add(northPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        cardLayout.show(cardPanel, Card.INTRO.toString());

        onPan(new Point(0, 0));
        addListeners();
        DatasetManager.addDatasetListener(this);
    }

    public PlayerControlPanel getControlPanel() {
        return controlPanel;
    }

    public void showCard(Card card) {
        cardLayout.show(cardPanel, card.toString());
    }

    public enum Card {INTRO, CANVAS}

    private void addListeners() {
        canvasPanel.addListener(this);
    }

    @Override
    public void onZoom(ZoomLevel zoomLevel) {
        zoomLabel.setText(zoomLevel.toString());
    }

    @Override
    public void onPan(Point point) {
        positionLabel.setText("Pos " + point.x + ", " + point.y);
    }

    @Override
    public void onDraw(int frameCount) {
        if (App.dataset() == null)
            frameCountLabel.setText("Frame 0 / 0");
        else
            frameCountLabel.setText("Frame " + NumberFormat.getInstance().format(frameCount) + " / " + App.dataset().FORMATTED_FRAME_COUNT);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        if (dataset != null) {
            cardLayout.show(cardPanel, Card.CANVAS.toString());
        } else {
            cardLayout.show(cardPanel, Card.INTRO.toString());
        }
        positionLabel.setText("Pos 0, 0");
    }

}
