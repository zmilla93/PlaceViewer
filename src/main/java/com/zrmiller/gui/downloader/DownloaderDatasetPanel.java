package com.zrmiller.gui.downloader;

import javax.swing.*;

public abstract class DownloaderDatasetPanel extends BaseDownloaderPanel {

    protected JButton deleteButton = new JButton("Delete");
    protected JButton downloadButton = new JButton("Download");

    public DownloaderDatasetPanel() {
        deleteButton.setEnabled(false);
        addEastButton(deleteButton);
        addEastButton(downloadButton);
    }

}
