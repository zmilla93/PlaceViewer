package com.zrmiller.gui.windows;

import com.zrmiller.App;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.strings.References;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.gui.MainMenuBar;
import com.zrmiller.gui.MainPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements IDatasetListener {

    private final Container container = getContentPane();
    private final MainMenuBar mainMenuBar = new MainMenuBar();
    private final MainPanel mainPanel = new MainPanel();

    public MainFrame() {
        super(References.APP_NAME);
        setIconImage(App.APP_ICON.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setSize(1200, 1000);
        container.setLayout(new BorderLayout());

        container.add(mainMenuBar, BorderLayout.NORTH);
        container.add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        App.datasetManager.addListener(this);
    }

    public void showCard(MainPanel.Card card) {
        mainPanel.showCard(card);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        setTitle(References.APP_NAME + " - " + dataset);
    }
}
