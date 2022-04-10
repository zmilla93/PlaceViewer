package com.zrmiller.gui.downloader;

import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.DataWrangler2022;
import com.zrmiller.core.datawrangler.DownloadManager;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloaderPanel2022 extends CardDownloaderPanel {

    private final JButton deleteButton = new JButton("Delete 2022");
    private final JButton downloadButton = new JButton("Download 2022");

    private JLabel partialFileCountLabel = new JLabel();
    private JLabel fileSizeLabel = new JLabel();

    public enum Panel {UNINSTALLED, PARTIALLY_INSTALLED, FULLY_INSTALLED}

    public DownloaderPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);

        DownloaderInfoPanel uninstalledPanel = new DownloaderInfoPanel();
        uninstalledPanel.addText("Total Download Size: 11.4 GB");
        uninstalledPanel.addText("Total Compressed Size: 1.49 GB");
        uninstalledPanel.addText("File Count: " + PlaceInfo.FILE_COUNT_2022);
        uninstalledPanel.addText("Files will be downloaded, unzipped, then compressed one at a time.");
        uninstalledPanel.addText("This process can be paused.");

        DownloaderInfoPanel partiallyInstalledPanel = new DownloaderInfoPanel();
        partiallyInstalledPanel.addText("Dataset Partially Installed");
        partiallyInstalledPanel.addComponent(partialFileCountLabel);

        DownloaderInfoPanel fullyInstalledPanel = new DownloaderInfoPanel();
        fullyInstalledPanel.addText("Dataset Installed");
        fullyInstalledPanel.addComponent(fileSizeLabel);
        fullyInstalledPanel.addText("File Count: " + PlaceInfo.FILE_COUNT_2022);

        cardPanel.add(uninstalledPanel, Panel.UNINSTALLED.toString());
        cardPanel.add(partiallyInstalledPanel, Panel.PARTIALLY_INSTALLED.toString());
        cardPanel.add(fullyInstalledPanel, Panel.FULLY_INSTALLED.toString());
        showPanel(Panel.UNINSTALLED);

        addEastButton(deleteButton);
        addEastButton(downloadButton);

        addListeners();

        validateData();
    }

    public void showPanel(Panel panel) {
        cardLayout.show(cardPanel, panel.toString());
    }

    private void addListeners() {
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataWrangler2022 wrangler2022 = DownloadManager.runDownload2022();
                datasetManagerFrame.getProgressPanel2022().setWrangler(wrangler2022);
                datasetManagerFrame.swapToProgress2022();
            }
        });
    }

    public void validateData() {
        int fileCount = DataValidator.validateFileCount2022();
        if (fileCount == PlaceInfo.FILE_COUNT_2022) {
            cardLayout.show(cardPanel, Panel.FULLY_INSTALLED.toString());
            downloadButton.setEnabled(false);
            deleteButton.setEnabled(true);
        } else if (fileCount < PlaceInfo.FILE_COUNT_2022 && fileCount > 0) {
            cardLayout.show(cardPanel, Panel.PARTIALLY_INSTALLED.toString());
            partialFileCountLabel.setText("File Count: " + fileCount + " / " + PlaceInfo.FILE_COUNT_2022);
            downloadButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            cardLayout.show(cardPanel, Panel.UNINSTALLED.toString());
            downloadButton.setEnabled(true);
            deleteButton.setEnabled(false);
        }
    }

}
