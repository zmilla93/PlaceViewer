package com.zrmiller;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.datawrangler.DataWrangler2022;
import com.zrmiller.core.TEST_Parser2022;
import com.zrmiller.core.parser.PlaceParser2022;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.gui.FrameManager;

import javax.swing.*;
import java.io.IOException;
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
        System.out.println(PlaceInfo.TIME_CORRECTION_2022);
        System.out.println("file count : " + PlaceInfo.fileOrder.length);
//        testParse();
    }


    private static void testParse() {
        PlaceParser2022 p = new PlaceParser2022("D:/Place/2022-Binary/", "Place_2022_INDEX.placetiles");
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
        d.downloadUnzipAndMinify(23);
    }

    private static void tempDownloadDataset() {
        DataWrangler2022 dataDownloader = new DataWrangler2022();
        dataDownloader.downloadUnzipAndMinify(23);
    }


}
