package com.zrmiller.gui.exporting;

import com.zrmiller.core.strings.References;
import com.zrmiller.core.utility.PlaceCanvas;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.FrameManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ExportGifWindow extends JFrame {

    JLabel selectionLabel = new JLabel();

    JSlider fpsSlider = new JSlider();
    JTextField tilesPerSecondInput = new JTextField("10000000");
    JTextField startFrameInput = new JTextField("10000000");
    JTextField endFrameInput = new JTextField("10000000");

    private int startFrame;
    private int endFrame;
    private int fps;

    protected Container container = getContentPane();
    private PlaceCanvas canvas;
    JLabel estimatedTimeLabel = new JLabel();

    public ExportGifWindow() {
        setTitle(References.APP_NAME + " - Export GIF");
        container.setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 200));

        JLabel playbackLabel = new JLabel("Tiles per Second");
        JLabel fpsInfoLabel = new JLabel("GIF FPS");
        JLabel fpsLabel = new JLabel("24");
        JLabel info1 = new JLabel("Click and drag right mouse to create a selection.");
        JLabel info2 = new JLabel("Tap right click to clear selection.");


        fpsLabel.setPreferredSize(fpsLabel.getPreferredSize());

        fpsSlider.setValue(24);
        fpsSlider.setMinimum(1);
        fpsSlider.setMaximum(30);
        fpsSlider.setPreferredSize(new Dimension(100, fpsSlider.getPreferredSize().height));
        fpsSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fpsLabel.setText(Integer.toString(fpsSlider.getValue()));
            }
        });

        GridBagConstraints gc = ZUtil.getGC();

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.add(info1, gc);
        gc.gridy++;
        infoPanel.add(info2, gc);

        JPanel startEndPanel = new JPanel(new GridBagLayout());
        gc = ZUtil.getGC();
        startEndPanel.add(new JLabel("Start Frame"), gc);
        gc.gridx++;
        startEndPanel.add(startFrameInput, gc);
        gc.gridx = 0;
        gc.gridy++;
        startEndPanel.add(new JLabel("End Frame"), gc);
        gc.gridx++;
        startEndPanel.add(endFrameInput, gc);


        gc = ZUtil.getGC();
        JPanel speedPanel = new JPanel(new GridBagLayout());
        speedPanel.add(playbackLabel, gc);
        gc.gridx++;
        speedPanel.add(tilesPerSecondInput, gc);
        gc.gridx++;

        gc = ZUtil.getGC();

        JPanel fpsPanel = new JPanel(new GridBagLayout());
        fpsPanel.add(fpsInfoLabel, gc);
        gc.gridx++;
        fpsPanel.add(fpsSlider);
        gc.gridx++;
        fpsPanel.add(fpsLabel);

        gc = ZUtil.getGC();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(startEndPanel, gc);
        gc.gridy++;
        panel.add(speedPanel, gc);
        gc.gridy++;
        panel.add(fpsPanel, gc);
        gc.gridy++;
        panel.add(infoPanel, gc);
        gc.gridy++;

        container.add(panel, BorderLayout.CENTER);
        pack();

    }

    private void readInputs() {
        startFrame = Integer.parseInt(startFrameInput.getText());
        endFrame = Integer.parseInt(endFrameInput.getText());
        fps = fpsSlider.getValue();
    }

    private void updateEstimatedTime() {
        int totalFrames = endFrame - startFrame;
        if (totalFrames < 1) {
            estimatedTimeLabel.setText("Invalid Range");
        }
        float time = totalFrames / FrameManager.canvasPanel.getPlayer().getSpeed();
//        estimatedTimeLabel.setText("Estimated Time: " + time + " seconds");
    }

    public void setCanvas(PlaceCanvas canvas) {
        this.canvas = canvas;
    }

}
