package com.zrmiller.gui.exporting;

import com.zrmiller.App;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.utility.PlaceCanvas;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.FrameManager;
import com.zrmiller.gui.mainframe.listeners.ICanvasListener;
import com.zrmiller.gui.misc.NumberDocumentFilter;
import com.zrmiller.modules.strings.References;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class ExportGifWindow extends JDialog implements ICanvasListener {

    JLabel selectionLabel = new JLabel();

    JSlider fpsSlider = new JSlider();
    JTextField tilesPerSecondInput = new JTextField("10000000");
    JTextField startFrameInput = new JTextField("10000000");
    JTextField endFrameInput = new JTextField("10000000");
    JButton exportButton = new JButton("Export");

    private int startFrame;
    private int endFrame;
    private int tilesPerSecond;
    private int fps;
    private ZoomLevel zoomLevel = ZoomLevel.Zoom_1;

    protected Container container = getContentPane();
    private PlaceCanvas canvas;
    JLabel estimatedTimeLabel = new JLabel();

    public ExportGifWindow() {
        setTitle(References.APP_NAME + " - Export GIF");
        container.setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 200));
        setAlwaysOnTop(true);

        JLabel playbackLabel = new JLabel("Tiles per Second");
        JLabel fpsInfoLabel = new JLabel("GIF FPS");
        JLabel fpsLabel = new JLabel("24");
        JLabel info1 = new JLabel("Click and drag right mouse to create a selection.");
        JLabel info2 = new JLabel("Tap right click to clear selection.");

        startFrameInput.setText("0");
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
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        panel.add(startEndPanel, gc);
        gc.gridy++;
        panel.add(speedPanel, gc);
        gc.gridy++;
        panel.add(fpsPanel, gc);
        gc.gridy++;
        panel.add(infoPanel, gc);
        gc.gridy++;
        panel.add(estimatedTimeLabel, gc);
        gc.gridy++;
        panel.add(exportButton, gc);
        gc.gridy++;

        updateEstimatedTime();
        container.add(new JSeparator(), BorderLayout.NORTH);
        container.add(panel, BorderLayout.CENTER);
        addListeners();
        pack();
        setLocationRelativeTo(FrameManager.mainFrame);
    }

    private void addListeners() {
        addUpdateEstimatedTimeListener(startFrameInput);
        addUpdateEstimatedTimeListener(endFrameInput);
        addUpdateEstimatedTimeListener(tilesPerSecondInput);
        exportButton.addActionListener(e -> {
            readInputs();
            PlaceCanvas canvas = FrameManager.canvasPanel.getCanvas();
            PlaceCanvas renderCanvas = new PlaceCanvas(FrameManager.canvasPanel.getPlayer());
            renderCanvas.zoomLevel = canvas.zoomLevel;
            Rectangle rect;
            if (FrameManager.canvasPanel.getCanvas().selection) {
                rect = canvas.getSelectionBounds();
                System.out.println(rect);
            } else {
                rect = new Rectangle(0, 0, App.dataset().CANVAS_SIZE_X, App.dataset().CANVAS_SIZE_Y);
            }
            renderCanvas.exportGIF(rect.x, rect.y, rect.width, rect.height, renderCanvas.zoomLevel, startFrame, endFrame, tilesPerSecond, fps);
        });
    }

    private void addUpdateEstimatedTimeListener(JTextField textField) {
        ((PlainDocument) textField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateEstimatedTime();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateEstimatedTime();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    private void readInputs() {
        startFrame = startFrameInput.getText().equals("") ? 0 : Integer.parseInt(startFrameInput.getText());
        endFrame = endFrameInput.getText().equals("") ? 0 : Integer.parseInt(endFrameInput.getText());
        tilesPerSecond = tilesPerSecondInput.getText().equals("") ? 0 : Integer.parseInt(tilesPerSecondInput.getText());
        fps = fpsSlider.getValue();
    }

    private void updateEstimatedTime() {
        readInputs();
        int totalFrames = endFrame - startFrame;
        float time = totalFrames / (float) tilesPerSecond;
        if (time <= 0 || Float.isNaN(time) || Float.isInfinite(time)) {
            estimatedTimeLabel.setText("Invalid Range");
            return;
        }
        estimatedTimeLabel.setText("Estimated Time: " + time + " seconds");
    }

    public void setCanvas(PlaceCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void onZoom(ZoomLevel zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    @Override
    public void onPan(Point center) {

    }

    @Override
    public void onDraw(int frameCount) {

    }
}
