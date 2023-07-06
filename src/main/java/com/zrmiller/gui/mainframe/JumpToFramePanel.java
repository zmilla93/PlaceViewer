package com.zrmiller.gui.mainframe;

import com.zrmiller.core.data.Dataset;
import com.zrmiller.core.managers.DatasetManager;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.misc.NumberDocumentFilter;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class JumpToFramePanel extends JPanel implements IDatasetListener {

    private final PlacePlayer player;

    private final JButton jumpToFrameButton = new JButton("Jump to Frame");
    private final JTextField frameInput = new JTextField(8);

    public JumpToFramePanel(PlacePlayer player) {
        this.player = player;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.insets = new Insets(2, 0, 2, 2);

        ((PlainDocument) frameInput.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        frameInput.setText("0");

        add(frameInput, gc);
        gc.gridx++;
        add(jumpToFrameButton, gc);
        gc.gridx++;

        addListeners();
        DatasetManager.addDatasetListener(this);
    }

    private void addListeners() {
        jumpToFrameButton.addActionListener(e -> {
            int targetFrame = getTargetFrame();
            if (targetFrame == -1) return;
            player.jumpToFrame(targetFrame);
        });
    }

    private int getTargetFrame() {
        String input = frameInput.getText();
        if (input.length() == 0) return -1;
        return Integer.parseInt(input);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        boolean enabled = dataset != null;
        jumpToFrameButton.setEnabled(enabled);
        frameInput.setEnabled(enabled);
    }

}
