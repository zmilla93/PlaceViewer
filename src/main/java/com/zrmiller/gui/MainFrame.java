package com.zrmiller.gui;

import com.zrmiller.App;
import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Container container = getContentPane();

    public MainFrame() {
        super("PlaceViewer");
        setIconImage(App.APP_ICON.getImage());
        setSize(1200, 1000);
        container.setLayout(new BorderLayout());
        CanvasPanel canvasPanel = new CanvasPanel();
//        container.add(new JButton(), BorderLayout.NORTH);
        container.add(canvasPanel, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel sidebarContainer = new JPanel(new BorderLayout());
        JPanel sidebar = new JPanel(new GridBagLayout());
        JButton runButton = new JButton("Rerun shit");
        GridBagConstraints gc = ZUtil.getGC();
        sidebar.add(Box.createHorizontalStrut(150), gc);
        gc.gridy++;
        gc.fill = GridBagConstraints.HORIZONTAL;
        sidebar.add(runButton, gc);
        gc.gridy++;

        sidebarContainer.add(sidebar, BorderLayout.NORTH);
        container.add(sidebarContainer, BorderLayout.WEST);


    }

}
