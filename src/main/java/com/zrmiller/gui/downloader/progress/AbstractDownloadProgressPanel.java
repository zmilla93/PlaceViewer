package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.datawrangler.DataWrangler;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.downloader.BaseDownloaderPanel;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDownloadProgressPanel extends BaseDownloaderPanel {

    protected final JProgressBar progressBar = new JProgressBar();
    protected final JLabel labelUpper = new JLabel();
    protected final JLabel labelLower = new JLabel();
    protected final JButton cancelButton = new JButton("Cancel");
    protected final int DOWNLOADER_PROGRESS_FPS = 10;
    protected Timer timer;
    protected DataWrangler wrangler;

    public AbstractDownloadProgressPanel(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
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
    }

    protected abstract void bindWrangler();

    public void setInfoUpper(String text) {
        labelUpper.setText(text);
    }

    public void setInfoLower(String text) {
        labelLower.setText(text);
    }

    public void setWrangler(DataWrangler wrangler) {
        this.wrangler = wrangler;
        bindWrangler();
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
