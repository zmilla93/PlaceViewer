package com.zrmiller;

import com.zrmiller.core.DatasetManager;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.datawrangler.DataWrangler2022;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.parser.PlaceParser2022;
import com.zrmiller.gui.FrameManager;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class App {

    public static ImageIcon APP_ICON = new ImageIcon(Objects.requireNonNull(App.class.getResource("/place.png")));
    public static DatasetManager datasetManager = new DatasetManager();

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
    }

    private static void testParse() {
        PlaceParser2022 p = new PlaceParser2022();
        p.openStream();
        while (true) {
            try {
                TileEdit edit = p.readNextLine();
                if (edit == null)
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void downloadFullDataset() {
        DataWrangler2022 d = new DataWrangler2022();
        d.downloadAndProcessFullDataset();
    }

    private static void testNewDownload() {
        DataWrangler2022 d = new DataWrangler2022();
//        d.downloadFile("CoolTest.gzip", "https://placedata.reddit.com/data/canvas-history/2022_place_canvas_history-000000000000.csv.gzip");
        d.downloadUnzipAndCompress(23);
    }

    private static void tempDownloadDataset() {
        DataWrangler2022 dataDownloader = new DataWrangler2022();
        dataDownloader.downloadUnzipAndCompress(23);
    }

    public static Dataset dataset(){
        return datasetManager.currentDataset();
    }

}
