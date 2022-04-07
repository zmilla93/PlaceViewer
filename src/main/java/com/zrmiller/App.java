package com.zrmiller;

import com.zrmiller.core.datawrangler.DataWrangler2022;
import com.zrmiller.core.PlaceParser2022;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.gui.FrameManager;
import com.zrmiller.modules.stopwatch.Stopwatch;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class App {

    public static ImageIcon APP_ICON = new ImageIcon(Objects.requireNonNull(App.class.getResource("/place.png")));

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                FrameManager.init();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    private static void downloadFullDataset(){
        DataWrangler2022 d = new DataWrangler2022();
        d.downloadAndProcessFullDataset();
    }

    private static void testNewDownload() {
        DataWrangler2022 d = new DataWrangler2022();
//        d.downloadFile("CoolTest.gzip", "https://placedata.reddit.com/data/canvas-history/2022_place_canvas_history-000000000000.csv.gzip");
        d.downloadUnzipAndMinify(23);
    }

    private static void tempDownloadDataset() {
        DataWrangler2022 dataDownloader = new DataWrangler2022();
        dataDownloader.downloadUnzipAndMinify(23);
    }

    private static void tempConvertDataset() {
        DataWrangler2022 dataDownloader = new DataWrangler2022();

        // Binary Convert
        Stopwatch.start();
        dataDownloader.minifyFile("Place_Tiles__2022_Original_0.txt", "Place_1_Micro.placetiles");
        System.out.println("Converted to binary in " + Stopwatch.getElapsedSeconds() + " seconds");

//        // Normal Convert
//        Stopwatch.start();
//        dataDownloader.minifyFile("Place_Tiles__2022_Original_0.txt", "Place_1_Mini.txt");
//        System.out.println("Converted to normal in " + Stopwatch.getElapsedSeconds() + " seconds");

        PlaceParser2022 parser2022 = new PlaceParser2022();

        Stopwatch.start();
        parser2022.readAllBinary();
        System.out.println("Read binary in " + Stopwatch.getElapsedSeconds() + " seconds");

        Stopwatch.start();
        parser2022.readAll();
        System.out.println("Read all in " + Stopwatch.getElapsedSeconds() + " seconds");

        System.out.println(PlaceInfo.TIME_CORRECTION_2022);
    }

}
