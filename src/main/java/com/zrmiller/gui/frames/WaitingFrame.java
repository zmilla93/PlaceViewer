package com.zrmiller.gui.frames;

import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class WaitingFrame extends JDialog {

    private final JPanel infoPanel = new JPanel(new GridBagLayout());
    private final JLabel[] labels = new JLabel[5];
    private int showHide = 0;

    private static final int INSET_VERTICAL = 40;
    private static final int INSET_HORIZONTAL = 80;

    public WaitingFrame() {
        setLayout(new BorderLayout());
        GridBagConstraints gc = ZUtil.getGC();
        setModalityType(ModalityType.APPLICATION_MODAL);
        setModal(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        gc.insets = new Insets(INSET_VERTICAL, INSET_HORIZONTAL, INSET_VERTICAL, INSET_HORIZONTAL);
        wrapperPanel.add(infoPanel, gc);

        gc = ZUtil.getGC();
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel();
            labels[i] = label;
            infoPanel.add(label, gc);
            gc.gridy++;
        }

        add(new JSeparator(), BorderLayout.NORTH);
        add(wrapperPanel, BorderLayout.CENTER);
        setResizable(false);
    }

    public void showFrame(String title, String... infoList) {
        setTitle(title);
        for (int i = 0; i < labels.length; i++) {
            String info = i < infoList.length ? infoList[i] : null;
            JLabel label = labels[i];
            if (info == null) label.setVisible(false);
            else {
                label.setText(info);
                label.setVisible(true);
            }
        }
        pack();
        revalidate();
        repaint();
        setLocationRelativeTo(null);
        showHide++;
        if (showHide > 0) setVisible(true);
    }

    public void hideFrame() {
        showHide--;
        setVisible(false);
    }

}
