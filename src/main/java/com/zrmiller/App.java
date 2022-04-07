package com.zrmiller;

import com.zrmiller.core.DataDownloader2022;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class App {

    public static ImageIcon APP_ICON = new ImageIcon(Objects.requireNonNull(App.class.getResource("/place.png")));

    public static <Path> void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
//                    FrameManager.init();
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        DataDownloader2022 downloader = new DataDownloader2022();
        downloader.runDownload();
//        downloader.minifyFile("D:/Place/good.txt", "D:/Place/better.txt");

//        downloader.cleanTimestamps("SORT_NAMES_2.txt", "_order.txt");
//        downloader.runDownload();
//        try {
//
//            DataDownloader2022.decompressGzip("D:/Place/Place_Tiles_2022_INDEX.gzip", "D:/Place/good.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
