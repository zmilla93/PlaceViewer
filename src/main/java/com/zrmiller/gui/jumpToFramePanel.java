package com.zrmiller.gui;

import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class jumpToFramePanel extends JPanel {

    private final JButton jumpToFrameButton = new JButton("Jump to Frame");
    private final JTextField frameInput = new JTextField("100000000");

    public jumpToFramePanel(){
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();

        add(frameInput, gc);
        gc.gridx++;
        add(jumpToFrameButton, gc);
        gc.gridx++;
    }

}
