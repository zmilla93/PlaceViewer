package com.zrmiller.gui.downloader;

import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.DownloadManager;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloaderPanel2017 extends DownloaderDatasetPanel {

    private final DatasetManagerFrame datasetManagerFrame;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JLabel fileSizeLabel = new JLabel();
    private final JLabel directoryLabel = new JLabel();

    public DownloaderPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super();
        this.datasetManagerFrame = datasetManagerFrame;

        deleteButton.setText("Delete 2017");
        downloadButton.setText("Download 2017");

        cardPanel.add(infoPanel(), "P1");
        cardPanel.add(completePanel(), "P2");

        cardLayout.show(cardPanel, "P1");
        centerPanel.add(cardPanel, BorderLayout.CENTER);
        addListeners();
        validateData();
    }

    private JPanel completePanel() {
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

    private JPanel infoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        infoPanel.add(new JLabel("Download Size: 1 GB"), gc);
        gc.gridy++;
        infoPanel.add(new JLabel("Compressed Size: 162 MB"), gc);
        gc.gridy++;
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
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("kil");
//                DataWrangler2017 wrangler2017 = new DataWrangler2017();
//                System.out.println(wrangler2017.deleteData());
//                validateData();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String confirm = JOptionPane.showInputDialog(self, "Are you sure you want to delete this dataset?\nType '2017' to confirm.", "Delete 2017 Dataset", JOptionPane.PLAIN_MESSAGE);
                if (confirm != null && confirm.equals("2017")) {
                    DataWrangler2017 dataWrangler2017 = new DataWrangler2017();
                    dataWrangler2017.deleteData();
                    validateData();
                }
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

    private void updateDirectory() {

    }

}
