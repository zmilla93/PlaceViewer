package com.zrmiller.gui.downloader;

import com.zrmiller.App;
import com.zrmiller.core.datawrangler.DataDownloader2022;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.callbacks.IValidationListener2022;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;

public class DownloaderPanel2022 extends CardDownloaderPanel implements IValidationListener2022 {

    private final JButton deleteButton = new JButton("Delete 2022");
    private final JButton downloadButton = new JButton("Download 2022");

    private final JLabel partialFileCountLabel = new JLabel();
    private final JLabel partialFileSizeLabel = new JLabel();
    private final JLabel fileSizeLabel = new JLabel();

    public enum Panel {UNINSTALLED, PARTIALLY_INSTALLED, FULLY_INSTALLED}

    public DownloaderPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);

        DownloaderInfoPanel uninstalledPanel = new DownloaderInfoPanel();
        uninstalledPanel.addComponent(uninstalledLabel);
        uninstalledPanel.addText("File Count: " + PlaceInfo.FILE_COUNT_2022);
        uninstalledPanel.addText("Total Download Size: 1.49 GB");

        DownloaderInfoPanel partiallyInstalledPanel = new DownloaderInfoPanel();
        partiallyInstalledPanel.addComponent(partiallyInstalledLabel);
        partiallyInstalledPanel.addComponent(partialFileCountLabel);
        partiallyInstalledPanel.addComponent(partialFileSizeLabel);

        DownloaderInfoPanel fullyInstalledPanel = new DownloaderInfoPanel();
        fullyInstalledPanel.addComponent(installedLabel);
        fullyInstalledPanel.addText("File Count: " + PlaceInfo.FILE_COUNT_2022);
        fullyInstalledPanel.addComponent(fileSizeLabel);

        cardPanel.add(uninstalledPanel, Panel.UNINSTALLED.toString());
        cardPanel.add(partiallyInstalledPanel, Panel.PARTIALLY_INSTALLED.toString());
        cardPanel.add(fullyInstalledPanel, Panel.FULLY_INSTALLED.toString());
        showPanel(Panel.UNINSTALLED);

        addEastButton(deleteButton);
        addEastButton(downloadButton);

        addListeners();
        DataValidator.addValidationListener2022(this);
    }

    public void showPanel(Panel panel) {
        cardLayout.show(cardPanel, panel.toString());
    }

    private void addListeners() {
        downloadButton.addActionListener(e -> runDownloadAction());
        deleteButton.addActionListener(e -> runDeleteAction());
    }

    private void runDownloadAction() {
        DataDownloader2022 downloader = new DataDownloader2022();
        datasetManagerFrame.getProgressPanel2022().setDownloader(downloader);
        datasetManagerFrame.swapToProgress2022();
        downloader.run();
    }

    private void runDeleteAction() {
        DownloaderPanel2022 self = this;
        String confirm = JOptionPane.showInputDialog(self, "Are you sure you want to delete this dataset?\n" +
                "Type '2022' to delete.", "Delete 2022 Dataset", JOptionPane.PLAIN_MESSAGE);
        if (confirm != null && confirm.equals("2022")) {
            if (App.dataset() != null && App.dataset().YEAR_STRING.equals(Dataset.PLACE_2022.YEAR_STRING))
                App.datasetManager.changeDataset(null);
            DataDownloader2022 downloader = new DataDownloader2022();
            if (!downloader.deleteData()) {
                JOptionPane.showMessageDialog(self,
                        "One or more files could not be deleted.\n" +
                                "If this problem persists, close this app and manually delete the files.",
                        "Delete Failed", JOptionPane.PLAIN_MESSAGE);
            }
            DataValidator.runValidation2022();
        }
    }

    @Override
    public void onValidation2022(boolean valid, int fileCount, long installSize) {
        if (fileCount == PlaceInfo.FILE_COUNT_2022) {
            cardLayout.show(cardPanel, Panel.FULLY_INSTALLED.toString());
            fileSizeLabel.setText("Total File Size: " + ZUtil.byteCountToString(installSize));
            downloadButton.setEnabled(false);
            deleteButton.setEnabled(true);
        } else if (fileCount < PlaceInfo.FILE_COUNT_2022 && fileCount > 0) {
            cardLayout.show(cardPanel, Panel.PARTIALLY_INSTALLED.toString());
            partialFileCountLabel.setText("File Count: " + fileCount + " / " + PlaceInfo.FILE_COUNT_2022);
            partialFileSizeLabel.setText("Installed File Size: " + ZUtil.byteCountToString(installSize) + " / 1.49 GB");
            downloadButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            cardLayout.show(cardPanel, Panel.UNINSTALLED.toString());
            downloadButton.setEnabled(true);
            deleteButton.setEnabled(false);
        }
    }

}
