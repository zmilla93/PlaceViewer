package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.datawrangler.DataDownloader;
import com.zrmiller.core.datawrangler.legacy.DataWrangler;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.downloader.BaseDownloaderPanel;
import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDownloadProgressPanel extends BaseDownloaderPanel {

    protected final JProgressBar progressBar = new JProgressBar();
    protected final JProgressBar progressBarLower = new JProgressBar();
    protected final JLabel labelUpper = new JLabel();
    protected final JLabel labelLower = new JLabel();
    protected final JButton cancelButton = new JButton("Cancel");
    protected final int DOWNLOADER_PROGRESS_FPS = 10;
    protected Timer timer;
    protected DataWrangler wrangler;
    protected DataDownloader downloader;
    private static final int TIMER_DELAY_MILLISECONDS = 200;

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
        gc.insets = new Insets(10, inset, 0, inset);
        centerPanel.add(progressBarLower, gc);
        gc.gridy++;
        progressBarLower.setVisible(false);
        addWestButton(cancelButton);
    }

    protected abstract void bindWrangler();

    protected abstract void bindDownloader();

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

    public void setDownloader(DataDownloader downloader){
        this.downloader = downloader;
        bindDownloader();
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
