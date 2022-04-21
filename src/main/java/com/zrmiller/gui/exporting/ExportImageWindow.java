package com.zrmiller.gui.exporting;

import com.zrmiller.core.enums.ExportSelection;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.strings.References;
import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class ExportImageWindow extends JFrame {

    public ExportImageWindow(){
        setTitle(References.APP_NAME + " - Export PNG");
        setLayout(new BorderLayout());

        JTextField fileNameInput = new JTextField();
        JComboBox<ExportSelection> selectionCombo = new JComboBox<>();
        JComboBox<ZoomLevel> zoomCombo = new JComboBox<>();
        for(ExportSelection selection : ExportSelection.values()) selectionCombo.addItem(selection);
        for(ZoomLevel zoom : ZoomLevel.values()) zoomCombo.addItem(zoom);
        JLabel info1 = new JLabel("Click and drag right mouse to create a selection.");
        JLabel info2 = new JLabel("Tap right click to clear selection.");
        JButton exportButton = new JButton("Export");

        JPanel filePanel = new JPanel(new GridBagLayout());
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        JPanel zoomPanel = new JPanel(new GridBagLayout());
        JPanel infoPanel = new JPanel(new GridBagLayout());
        JPanel panel = new JPanel(new GridBagLayout());


        GridBagConstraints gc= ZUtil.getGC();

        filePanel.add(new JLabel("File Name"), gc);
        gc.gridx++;
        filePanel.add(fileNameInput, gc);
        gc.gridx++;

        gc= ZUtil.getGC();

        selectionPanel.add(new JLabel("Area"), gc);
        gc.gridx++;
        selectionPanel.add(selectionCombo, gc);
        gc.gridx++;

        gc= ZUtil.getGC();

        zoomPanel.add(new JLabel("Zoom Level"), gc);
        gc.gridx++;
        zoomPanel.add(zoomCombo, gc);
        gc.gridx++;

        infoPanel.add(info1, gc);
        gc.gridy++;
        infoPanel.add(info2, gc);
        gc.gridy++;

        gc= ZUtil.getGC();

        panel.add(filePanel, gc);
        gc.gridy++;
        panel.add(selectionPanel, gc);
        gc.gridy++;
        panel.add(zoomPanel, gc);
        gc.gridy++;
        panel.add(infoPanel, gc);
        gc.gridy++;

        add(panel, BorderLayout.CENTER);
        pack();
//        revalidate();
//        repaint();
    }

}
