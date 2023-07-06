package com.zrmiller.gui.mainframe;

import com.zrmiller.App;
import com.zrmiller.core.data.Dataset;
import com.zrmiller.core.data.References;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.exporting.IExportCallback;
import com.zrmiller.core.managers.DatasetManager;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.utility.PlaceCanvas;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.FrameManager;
import com.zrmiller.modules.colortheme.components.FlatColorIconButton;

import javax.swing.*;
import java.awt.*;

public class ExportPanel extends JPanel implements IDatasetListener {

    // Insets
    private static final int INSET_SMALL = 0;
    private static final int INSET_LARGE = 4;

    // Controls
    private final JButton exportButton = new JButton("Export PNG");
    private final JTextField fileNameInput = new JTextField(15);
    private final JComboBox<ZoomLevel> zoomCombo = new JComboBox<>();
    private final JButton openFolderButton = new JButton("Open Folder");
    private final JButton closeButton = new FlatColorIconButton("/icons/close.png");

    // Canvas
    private PlaceCanvas renderCanvas;
    private PlayerControlPanel controlPanel;

    public ExportPanel() {

        // Add values to zoom combo box
        for (ZoomLevel zoomLevel : ZoomLevel.values()) {
            if (!zoomLevel.zoomOut && zoomLevel.modifier > 4) break;
            zoomCombo.addItem(zoomLevel);
        }
        zoomCombo.setSelectedItem(ZoomLevel.Zoom_1);

        // Layout
        setLayout(new BorderLayout());
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.fill = 1;

        controlsPanel.add(Box.createHorizontalStrut(7), gc);
        gc.gridx++;
        controlsPanel.add(new JLabel("File Name"), gc);
        gc.gridx++;
        controlsPanel.add(fileNameInput, gc);
        gc.gridx++;
        gc.insets.left = INSET_LARGE;
        controlsPanel.add(new JLabel("Zoom Level"), gc);
        gc.gridx++;
        gc.insets.left = INSET_SMALL;
        controlsPanel.add(zoomCombo, gc);
        gc.gridx++;
        gc.insets.left = INSET_LARGE;
        controlsPanel.add(exportButton, gc);
        gc.gridx++;
        gc.insets.left = INSET_SMALL;
        controlsPanel.add(openFolderButton, gc);
        gc.gridx++;
        controlsPanel.add(closeButton, gc);
        gc.gridx++;

        // Main Panel
        add(controlsPanel, BorderLayout.WEST);
        add(new JSeparator(), BorderLayout.SOUTH);

        addListeners();
        DatasetManager.addDatasetListener(this);
    }

    public void setControlPanel(PlayerControlPanel panel) {
        this.controlPanel = panel;
    }

    private void addListeners() {
        openFolderButton.addActionListener(e -> {
            if (!DataValidator.isDataDirectoryValid()) return;
            ZUtil.openExplorer(References.getExportFolder());
        });
        closeButton.addActionListener(e -> {
            setVisible(false);
            controlPanel.setExportIcon(false);
        });
        exportButton.addActionListener(e -> exportPNG());
    }

    private void exportPNG() {
        if (App.dataset() == null) return;
        PlaceCanvas displayCanvas = FrameManager.canvasPanel.getCanvas();
        renderCanvas = new PlaceCanvas(FrameManager.canvasPanel.getPlayer());
        renderCanvas.zoomLevel = (ZoomLevel) zoomCombo.getSelectedItem();
        Rectangle rect;
        if (displayCanvas.selection) {
            rect = displayCanvas.getSelectionBounds();
        } else {
            rect = new Rectangle(0, 0, App.dataset().CANVAS_SIZE_X, App.dataset().CANVAS_SIZE_Y);
        }
        IExportCallback callback = () -> {
            renderCanvas.exportCleanup();
            renderCanvas = null;
            System.gc();
        };
        String fileName = fileNameInput.getText();
        if (fileName.endsWith(".png")) {
            fileName = fileName.replaceAll("\\.png", "");
            fileNameInput.setText(fileName);
        }
        FrameManager.canvasPanel.getPlayer().pause();
        renderCanvas.exportImage(fileName, rect.x, rect.y, rect.width, rect.height, renderCanvas.zoomLevel, callback);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        if (dataset == null) setVisible(false);
    }
}
