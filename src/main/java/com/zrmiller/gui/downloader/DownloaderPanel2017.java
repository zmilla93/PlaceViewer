package com.zrmiller.gui.downloader;

import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.DownloadManager;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;

public class DownloaderPanel2017 extends CardDownloaderPanel {

    private final JLabel fileSizeLabel = new JLabel();
    protected JButton deleteButton = new JButton("Delete");
    protected JButton downloadButton = new JButton("Download");
    private static final boolean SHOW_TEST_SCREEN = false;

    private enum Panel {UNINSTALLED, INSTALLED}

    public DownloaderPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        deleteButton.setText("Delete 2017");
        downloadButton.setText("Download 2017");

        DownloaderInfoPanel uninstalledPanel = new DownloaderInfoPanel();
        uninstalledPanel.addText("Download Size: 1 GB");
        uninstalledPanel.addText("Compressed Size: 162 MB");
        uninstalledPanel.addText("Data will be downloaded into a single file, compressed, then sorted.");

        DownloaderInfoPanel installedPanel = new DownloaderInfoPanel();
        installedPanel.addText("Dataset Installed");
        installedPanel.addComponent(fileSizeLabel);

        cardPanel.add(uninstalledPanel, Panel.UNINSTALLED.toString());
        cardPanel.add(installedPanel, Panel.INSTALLED.toString());

        addEastButton(deleteButton);
        addEastButton(downloadButton);

        addListeners();
        validateData();
    }

    private void runDownload() {
        if (SHOW_TEST_SCREEN) {
            showTestScreen();
            return;
        }
        DataWrangler2017 dataWrangler2017 = DownloadManager.runDownload2017();
        datasetManagerFrame.getProgressPanel2017().setWrangler(dataWrangler2017);
        datasetManagerFrame.swapToProgress2017();
    }

    private void showTestScreen() {
        datasetManagerFrame.getProgressPanel2017().setInfoUpper("Downloading 2017 Dataset...");
        int low = 1245, high = 5835;
        datasetManagerFrame.getProgressPanel2017().setInfoLower(low + " MB / " + high + " MB");
        datasetManagerFrame.getProgressPanel2017().getProgressBar().setMinimum(0);
        datasetManagerFrame.getProgressPanel2017().getProgressBar().setMaximum(high);
        datasetManagerFrame.getProgressPanel2017().getProgressBar().setValue(low);
        datasetManagerFrame.swapToProgress2017();
    }

    private void addListeners() {
        DownloaderPanel2017 self = this;
        downloadButton.addActionListener(e -> {
            runDownload();
        });
        deleteButton.addActionListener(e -> {
            String confirm = JOptionPane.showInputDialog(self, "Are you sure you want to delete this dataset?\n" +
                    "Type '2017' to delete.", "Delete 2017 Dataset", JOptionPane.PLAIN_MESSAGE);
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
            fileSizeLabel.setText("File Size: " + ZUtil.byteCountToString(fileSize));
            cardLayout.show(cardPanel, Panel.INSTALLED.toString());
        } else {
            deleteButton.setEnabled(false);
            downloadButton.setEnabled(true);
            cardLayout.show(cardPanel, Panel.UNINSTALLED.toString());
        }
        if (SHOW_TEST_SCREEN) {
            downloadButton.setText("Download 2017*");
            downloadButton.setEnabled(true);
        }
    }

}
