package com.zrmiller;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.DataWrangler2022;
import com.zrmiller.core.parser.PlaceParser2022;
import com.zrmiller.core.utility.PlaceInfo;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Objects;


public class App {

    public static ImageIcon APP_ICON = new ImageIcon(Objects.requireNonNull(App.class.getResource("/place.png")));

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
//                FrameManager.init();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(PlaceInfo.TIME_CORRECTION_2022);
        System.out.println("file count : " + PlaceInfo.fileOrder.length);
//        testParse();

        DataWrangler2017 dataWrangler2017 = new DataWrangler2017("D:/Place/2017/", "Place_2017_Original.txt");
//        dataWrangler2017.downloadFile();
        dataWrangler2017.sortAndMinify("Place_2017_Original.txt", "Place_2017.placetiles", false);

        Timestamp start = Timestamp.valueOf("2022-04-01 12:44:10.315");
        Timestamp end = Timestamp.valueOf("2022-04-05 00:14:00.207");
        System.out.println("START TIME : " + start.getTime());
        System.out.println("END TIME : " + end.getTime());
        System.out.println("ELAPSED TIME : " + (end.getTime() - start.getTime()));

        System.out.println("2017:" + (PlaceInfo.FINAL_TIME_2017 - PlaceInfo.INITIAL_TIME_2017));
        System.out.println("2022:" + (PlaceInfo.FINAL_TIME_2022 - PlaceInfo.INITIAL_TIME_2022));

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
