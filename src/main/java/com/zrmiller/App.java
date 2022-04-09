package com.zrmiller;

import com.zrmiller.core.DatasetManager;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.datawrangler.DataWrangler2022;
import com.zrmiller.core.enums.Dataset;
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
        System.out.println("File size : " + PlaceInfo.CLEAN_LINE_COUNT * TileEdit.BYTE_COUNT);
        try {
            SwingUtilities.invokeAndWait(() -> {
                FrameManager.init();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        // FIXME:
        DatasetManager.changeDataset(Dataset.PLACE_2017);
        System.out.println("done");

    }

    private static void testParse() {
        PlaceParser2022 p = new PlaceParser2022("D:/Place/2022-Binary/");
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
