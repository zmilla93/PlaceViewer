package com.zrmiller.gui.downloader;

import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class DownloaderInfoPanel extends JPanel {

    private final JPanel panel = new JPanel();
    private final GridBagConstraints gc = ZUtil.getGC();
    private final int TEXT_INSET = 10;

    /**
     * Adds labels/components line by line.
     */
    public DownloaderInfoPanel() {
        setLayout(new BorderLayout());
        super.add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridBagLayout());
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.insets = new Insets(TEXT_INSET, TEXT_INSET, 0, 0);
    }

    protected void addText(String line) {
        JLabel label = new JLabel(line);
        addText(label);
    }

    protected void addText(JLabel label){
        panel.add(label, gc);
        gc.insets = new Insets(0, TEXT_INSET, 0, 0);
        gc.gridy++;
    }

    protected void addComponent(JComponent component) {
        panel.add(component, gc);
        gc.insets = new Insets(0, TEXT_INSET, 0, 0);
        gc.gridy++;
    }

}
