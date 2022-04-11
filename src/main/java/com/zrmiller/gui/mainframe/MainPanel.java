package com.zrmiller.gui.mainframe;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.gui.mainframe.listeners.ICanvasListener;
import com.zrmiller.modules.colortheme.components.SeparatorPanel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class MainPanel extends JPanel implements ICanvasListener, IDatasetListener {

    private final IntroPanel introPanel = new IntroPanel();
    private final CanvasPanel canvasPanel = new CanvasPanel();

    private final JPanel southPanel = new JPanel(new BorderLayout());
    private final JLabel positionLabel = new JLabel("Pos -");
    private final JLabel frameCountLabel = new JLabel("-");
    private final JLabel zoomLabel = new JLabel("100%");

    private final JPanel northPanel = new JPanel(new BorderLayout());

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public MainPanel() {
        setLayout(new BorderLayout());

        // North Panel
        JPanel northBuffer = new JPanel(new BorderLayout());
        northBuffer.add(new PlayerControlPanel(canvasPanel.getPlayer()), BorderLayout.WEST);
        northBuffer.add(new JumpToFramePanel(canvasPanel, canvasPanel.getPlayer()), BorderLayout.EAST);

        northPanel.add(northBuffer, BorderLayout.CENTER);
        northPanel.add(new SeparatorPanel(), BorderLayout.SOUTH);

        // South Panel
        JPanel southRightPanel = new JPanel();
        southRightPanel.add(zoomLabel);
        southPanel.add(frameCountLabel, BorderLayout.WEST);
        southPanel.add(southRightPanel, BorderLayout.EAST);
        southPanel.add(positionLabel, BorderLayout.CENTER);
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
        App.datasetManager.addListener(this);
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
        frameCountLabel.setText("Frame " + NumberFormat.getInstance().format(frameCount) + " / " + PlaceInfo.CLEAN_LINE_COUNT_FORMATTED);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        if (dataset != null) {
            cardLayout.show(cardPanel, Card.CANVAS.toString());
        } else {
            cardLayout.show(cardPanel, Card.INTRO.toString());
        }
    }

}
