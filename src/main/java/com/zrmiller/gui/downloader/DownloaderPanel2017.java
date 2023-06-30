package com.zrmiller.gui.downloader;

import com.zrmiller.App;
import com.zrmiller.core.datawrangler.DataDownloader2017;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.callbacks.IValidationListener2017;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;

public class DownloaderPanel2017 extends CardDownloaderPanel implements IValidationListener2017 {

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
        DataValidator.addValidationListener2017(this);
    }

    private void runDownload() {
        if (SHOW_TEST_SCREEN) {
            showTestScreen();
            return;
        }
        DataDownloader2017 downloader = new DataDownloader2017();
        downloader.run();
        datasetManagerFrame.getProgressPanel2017().setDownloader(downloader);
        datasetManagerFrame.swapToProgress2017();
        DataValidator.runValidation2017();
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
                if (App.dataset() != null && App.dataset().YEAR_STRING.equals(Dataset.PLACE_2017.YEAR_STRING))
                    App.datasetManager.changeDataset(null);
                DataDownloader2017 downloader = new DataDownloader2017();
                if (!downloader.deleteData()) {
                    JOptionPane.showMessageDialog(self,
                            "Failed to delete data. Make sure the player is stopped, then try again.\n" +
                                    "If this problem persists, close this app and manually delete the files.",
                            "Delete Failed", JOptionPane.PLAIN_MESSAGE);
                }
                DataValidator.runValidation2017();
            }
        });
    }

    @Override
    public void onValidation2017(boolean valid, long fileSize) {
        if (valid) {
            deleteButton.setEnabled(true);
            downloadButton.setEnabled(false);
            fileSizeLabel.setText("File Size: " + ZUtil.byteCountToString(fileSize));
            cardLayout.show(cardPanel, Panel.INSTALLED.toString());
        } else {
            deleteButton.setEnabled(false);
            downloadButton.setEnabled(true);
            cardLayout.show(cardPanel, Panel.UNINSTALLED.toString());
        }
    }

}
