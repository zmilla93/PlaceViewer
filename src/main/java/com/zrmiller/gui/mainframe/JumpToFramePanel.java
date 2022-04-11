package com.zrmiller.gui.mainframe;

import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.misc.NumberDocumentFilter;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JumpToFramePanel extends JPanel {

    private final PlacePlayer player;
    private final CanvasPanel canvasPanel;

    private final JButton jumpToFrameButton = new JButton("Jump to Frame");
    private final JTextField frameInput = new JTextField("0");

    public JumpToFramePanel(CanvasPanel canvasPanel, PlacePlayer player) {
        this.canvasPanel = canvasPanel;
        this.player = player;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.insets = new Insets(2, 0, 2, 2);

        ((PlainDocument) frameInput.getDocument()).setDocumentFilter(new NumberDocumentFilter());

        add(frameInput, gc);
        gc.gridx++;
        add(jumpToFrameButton, gc);
        gc.gridx++;

        addListeners();
    }

    private void addListeners() {
        jumpToFrameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int targetFrame = getTargetFrame();
                if (targetFrame == -1) return;
                // FIXME:
//                player.jumpToFrame(targetFrame);
                canvasPanel.panToPixel(1000, 1000);
                canvasPanel.tryRepaint(true);
            }
        });
    }

    private int getTargetFrame() {
        String input = frameInput.getText();
        if (input.length() == 0) return -1;
        return Integer.parseInt(input);
    }

}
