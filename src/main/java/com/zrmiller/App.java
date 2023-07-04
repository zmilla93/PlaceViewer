package com.zrmiller;

import com.zrmiller.core.datawrangler.DataDownloader;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.DatasetManager;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.gui.FrameManager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class App {

    public static ImageIcon APP_ICON = new ImageIcon(Objects.requireNonNull(App.class.getResource("/place.png")));

    public static void main(String[] args) {
        // Load save data
        SaveManager.settings.loadFromDisk();

        // Create GUI
        try {
            SwingUtilities.invokeAndWait(() -> {
                FrameManager.init();
                FrameManager.tryShowDataset(SaveManager.settings.data.preferredDataset);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Validate datasets
        DataValidator.runValidation2017();
        DataValidator.runValidation2022();
        DatasetManager.changeDataset(null);

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(App::shutdown));
    }

    public static Dataset dataset() {
        return DatasetManager.getDataset();
    }

    private static void shutdown() {
        if (DataDownloader.activeDownloader != null) {
            DataDownloader.activeDownloader.cancelDownload();
        }
    }

}
