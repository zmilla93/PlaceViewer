package com.zrmiller.gui;

import javax.swing.*;

public class DownloaderPanel extends JPanel {

    private JButton download2017 = new JButton("Download 2017 Dataset");
    private JButton downloadButton = new JButton("Download Dataset");
    private JFileChooser fileChooser = new JFileChooser();
    private JButton browseButton = new JButton("Browse");

    public DownloaderPanel() {
        add(new JButton("WOW"));
        add(downloadButton);
        add(browseButton);

        addListeners();
    }

    private void addListeners() {
        JPanel self = this;
        browseButton.addActionListener(e -> fileChooser.showOpenDialog(self));
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (fileChooser != null)
            fileChooser.updateUI();
    }
}
