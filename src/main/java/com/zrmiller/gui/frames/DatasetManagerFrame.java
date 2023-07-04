package com.zrmiller.gui.frames;

import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.downloader.DownloaderPanel2017;
import com.zrmiller.gui.downloader.DownloaderPanel2022;
import com.zrmiller.gui.downloader.progress.AbstractDownloadProgressPanel;
import com.zrmiller.gui.downloader.progress.DownloaderProgressPanel2017;
import com.zrmiller.gui.downloader.progress.DownloaderProgressPanel2022;
import com.zrmiller.modules.colortheme.ColorManager;
import com.zrmiller.modules.colortheme.IThemeListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DatasetManagerFrame extends JDialog implements IThemeListener {

    private final JButton browseButton = new JButton("Select Folder");
    private final JFileChooser fileChooser = new JFileChooser();

    private final JLabel directoryLabel = new JLabel();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final DownloaderPanel2017 downloaderPanel2017 = new DownloaderPanel2017(this);
    private final DownloaderPanel2022 downloaderPanel2022 = new DownloaderPanel2022(this);
    private final AbstractDownloadProgressPanel downloadProgressPanel2017 = new DownloaderProgressPanel2017(this);
    private final AbstractDownloadProgressPanel downloadProgressPanel2022 = new DownloaderProgressPanel2022(this);

    public DatasetManagerFrame() {
        setTitle("Dataset Manager");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setModal(true);
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        setMinimumSize(new Dimension(500, 300));
        setSize(500, 300);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("2017 Data", downloaderPanel2017);
        tabbedPane.addTab("2022 Data", downloaderPanel2022);

        directoryLabel.setText(SaveManager.settings.data.dataDirectory);

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel browsePanel = new JPanel(new FlowLayout());
        browsePanel.add(browseButton);
        browsePanel.add(directoryLabel);
        updateDirectory();

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets.left = 15;
        infoPanel.add(new JLabel("Select a folder to store downloaded files."), gc);
        gc.gridy++;
        infoPanel.add(new JLabel("Subfolders will be created automatically for each year."), gc);

        JPanel browseBufferPanel = new JPanel(new BorderLayout());
        browseBufferPanel.add(browsePanel, BorderLayout.WEST);

        topPanel.add(browseBufferPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.WEST);
        JPanel topBufferPanel = new JPanel(new BorderLayout());
        topBufferPanel.add(topPanel, BorderLayout.CENTER);

        JPanel datasetPanel = new JPanel(new BorderLayout());
        datasetPanel.add(topBufferPanel, BorderLayout.NORTH);
        datasetPanel.add(tabbedPane, BorderLayout.CENTER);

        cardPanel.add(datasetPanel, "P1");
        cardPanel.add(downloadProgressPanel2017, "P2");
        cardPanel.add(downloadProgressPanel2022, "P3");
        cardLayout.show(cardPanel, "P1");

        container.add(new JSeparator(), BorderLayout.NORTH);
        container.add(cardPanel, BorderLayout.CENTER);

        addListeners();
        ColorManager.addListener(this);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }



    private void addListeners() {
        JDialog self = this;
        browseButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(self) == JFileChooser.APPROVE_OPTION) {
                String path = String.valueOf(fileChooser.getSelectedFile());
                directoryLabel.setText(path);
                SaveManager.settings.data.dataDirectory = path + File.separator;
                SaveManager.settings.saveToDisk();
                updateDirectory();
                DataValidator.runValidation2017();
                DataValidator.runValidation2022();
            }
        });
    }

    private void updateDirectory(){
        String dir = SaveManager.settings.data.dataDirectory;
        File file = new File(dir);
        if(file.isDirectory()){
            fileChooser.setCurrentDirectory(file);
            updateDirectoryLabel();
        }
    }

    private void updateDirectoryLabel() {
        String text = SaveManager.settings.data.dataDirectory == null ? "No Folder Selected" : SaveManager.settings.data.dataDirectory;
        directoryLabel.setText(text);
    }

    public void swapToDownloader() {
        cardLayout.show(cardPanel, "P1");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public void swapToProgress2017() {
        cardLayout.show(cardPanel, "P2");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public void swapToProgress2022() {
        cardLayout.show(cardPanel, "P3");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public AbstractDownloadProgressPanel getProgressPanel2017() {
        return downloadProgressPanel2017;
    }

    public AbstractDownloadProgressPanel getProgressPanel2022() {
        return downloadProgressPanel2022;
    }

    @Override
    public void onThemeChange() {
        fileChooser.updateUI();
    }
}
