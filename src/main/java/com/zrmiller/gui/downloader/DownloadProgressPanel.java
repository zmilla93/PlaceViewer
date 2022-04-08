package com.zrmiller.gui.downloader;

import com.zrmiller.core.datawrangler.DataWrangler;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloadProgressPanel extends BaseDownloaderPanel {

    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel labelUpper = new JLabel();
    private final JLabel labelLower = new JLabel();
    private final JButton cancelButton = new JButton("Cancel");
    private final DatasetManagerFrame datasetManagerFrame;
    private final Timer timer;

    private static int i = 0;
    private DataWrangler wrangler;

    public DownloadProgressPanel(DatasetManagerFrame datasetManagerFrame) {
        this.datasetManagerFrame = datasetManagerFrame;
        centerPanel.setLayout(new GridBagLayout());
        progressBar.setMaximum(0);
        progressBar.setMaximum(100);

        GridBagConstraints gc = ZUtil.getGC();
        centerPanel.add(labelUpper, gc);
        gc.gridy++;
        centerPanel.add(labelLower, gc);
        gc.gridy++;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        int inset = 100;
        gc.insets = new Insets(0, inset, 0, inset);
        centerPanel.add(progressBar, gc);
        gc.gridy++;
        addWestButton(cancelButton);
        addListeners();

        timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progressBar.setValue(wrangler.getProgress());
            }
        });
    }

    public void setInfoUpper(String text) {
        labelUpper.setText(text);
    }

    public void setInfoLower(String text) {
        labelLower.setText(text);
    }

    private void addListeners() {
        cancelButton.addActionListener(e -> datasetManagerFrame.swapToDatasetPanel());
    }

    public void setWrangler(DataWrangler wrangler) {
        this.wrangler = wrangler;
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

}
