package com.zrmiller.gui.windows;

import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.downloader.DownloadProgressPanel;
import com.zrmiller.gui.downloader.DownloaderPanel2017;
import com.zrmiller.gui.downloader.DownloaderPanel2022;
import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.IThemeListener;

import javax.swing.*;
import java.awt.*;

public class DatasetManagerFrame extends JFrame implements IThemeListener {

    private JButton browseButton = new JButton("Select Folder");
    private JFileChooser fileChooser = new JFileChooser();

    private JLabel directoryLabel = new JLabel("No folder selected");
    private Container container = getContentPane();
    private JTabbedPane tabbedPane = new JTabbedPane();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private DownloadProgressPanel downloadProgressPanel = new DownloadProgressPanel(this);

    public DatasetManagerFrame() {
        super("Dataset Manager");
        container.setLayout(new BorderLayout());
        setMinimumSize(new Dimension(500, 300));
        setSize(500, 300);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        tabbedPane.addTab("2017", new DownloaderPanel2017(this));
        tabbedPane.addTab("2022", new DownloaderPanel2022());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel browsePanel = new JPanel(new FlowLayout());
        browsePanel.add(browseButton);
        browsePanel.add(directoryLabel);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets.left = 10;
        infoPanel.add(new JLabel("Select a root folder to store downloaded files."), gc);
        gc.gridy++;
        infoPanel.add(new JLabel("Subfolders will automatically be made for each year."), gc);
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(browsePanel, BorderLayout.WEST);
        JPanel topBufferPanel = new JPanel(new BorderLayout());
        topBufferPanel.add(topPanel, BorderLayout.CENTER);

        JPanel datasetPanel = new JPanel(new BorderLayout());
        datasetPanel.add(topBufferPanel, BorderLayout.NORTH);
        datasetPanel.add(tabbedPane, BorderLayout.CENTER);

        cardPanel.add(datasetPanel, "P1");
        cardPanel.add(downloadProgressPanel, "P2");
        cardLayout.show(cardPanel, "P1");

        container.add(new JSeparator(), BorderLayout.NORTH);
        container.add(cardPanel, BorderLayout.CENTER);

        addListeners();
        ColorManager.addListener(this);
        pack();
    }

    private void addListeners() {
        JFrame self = this;
        browseButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(self) == JFileChooser.APPROVE_OPTION)
                directoryLabel.setText(String.valueOf(fileChooser.getSelectedFile()));
        });
    }

    public void swapToDatasetPanel() {
        cardLayout.show(cardPanel, "P1");
    }

    public void swapToDownloadPanel() {
        cardLayout.show(cardPanel, "P2");
    }

    @Override
    public void onThemeChange() {
        fileChooser.updateUI();
    }
}
