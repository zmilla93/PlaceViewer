package com.zrmiller.gui.downloader;

import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.DownloadManager;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public class DownloaderPanel2017 extends DownloaderDatasetPanel {

    private final JLabel fileSizeLabel = new JLabel();
    protected JButton deleteButton = new JButton("Delete");
    protected JButton downloadButton = new JButton("Download");

    public DownloaderPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        deleteButton.setText("Delete 2017");
        downloadButton.setText("Download 2017");

        cardPanel.add(uninstalledPanel(), "P1");
        cardPanel.add(installedPanel(), "P2");

        cardLayout.show(cardPanel, "P1");
        centerPanel.add(cardPanel, BorderLayout.CENTER);

        addEastButton(deleteButton);
        addEastButton(downloadButton);

        addListeners();
        validateData();
    }

    private JPanel installedPanel() {
        JPanel completePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        completePanel.add(new JLabel("Dataset Installed"), gc);
        gc.gridy++;
        completePanel.add(fileSizeLabel, gc);
        gc.gridy++;
        return completePanel;
    }

    private JPanel uninstalledPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        infoPanel.add(new JLabel("Download Size: 1 GB"), gc);
        gc.gridy++;
        infoPanel.add(new JLabel("Compressed Size: 162 MB"), gc);
        gc.gridy++;
        infoPanel.add(new JLabel("Data will be downloaded into a single file, sorted, compressed, then the original file deleted."), gc);
        gc.gridy++;
        JPanel infoBufferPanel = new JPanel(new GridBagLayout());
        gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        infoBufferPanel.add(infoPanel, gc);
        return infoPanel;
    }

    private void addListeners() {
        DownloaderPanel2017 self = this;
        downloadButton.addActionListener(e -> {
            DataWrangler2017 dataWrangler2017 = DownloadManager.downloadAndMinify2017();
            datasetManagerFrame.getProgressPanel().setWrangler(dataWrangler2017);
            datasetManagerFrame.getProgressPanel().displayDownload2017();
            datasetManagerFrame.swapToDownloadPanel();
        });
        deleteButton.addActionListener(e -> {
            String confirm = JOptionPane.showInputDialog(self, "Are you sure you want to delete this dataset?\nType '2017' to confirm.", "Delete 2017 Dataset", JOptionPane.PLAIN_MESSAGE);
            if (confirm != null && confirm.equals("2017")) {
                DataWrangler2017 dataWrangler2017 = new DataWrangler2017();
                if (!dataWrangler2017.deleteData()) {
                    JOptionPane.showMessageDialog(self,
                            "Failed to delete data. Make sure the player is stopped, then try again.\n" +
                                    "If this problem persists, close this app and manually delete the files.",
                            "Delete Failed", JOptionPane.PLAIN_MESSAGE);
                }
                validateData();
            }
        });
    }

    public void validateData() {
        long fileSize = DataValidator.validate2017();
        if (fileSize > 0) {
            deleteButton.setEnabled(true);
            downloadButton.setEnabled(false);
            fileSizeLabel.setText("File Size: " + fileSize / 1000000 + " MB");
            cardLayout.show(cardPanel, "P2");
        } else {
            deleteButton.setEnabled(false);
            downloadButton.setEnabled(true);
            cardLayout.show(cardPanel, "P1");
        }
    }

}