package com.zrmiller;

import com.zrmiller.core.DataDownloader2022;
import com.zrmiller.core.PlaceParser2022;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.modules.stopwatch.Stopwatch;

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

//        tempDownloadDataset();

        tempConvertDataset();
    }

    private static void tempDownloadDataset() {
        DataDownloader2022 dataDownloader = new DataDownloader2022();
        dataDownloader.runOrder(0);
    }

    private static void tempConvertDataset() {
        System.out.println("byte : " + Byte.MAX_VALUE);
        System.out.println("Short : " + Short.MAX_VALUE);
        System.out.println("Int : " + Integer.MAX_VALUE);

        DataDownloader2022 dataDownloader = new DataDownloader2022();

        // Binary Convert
        Stopwatch.start();
        dataDownloader.minifyFileBinary("Place_Tiles__2022_Original_0.txt", "Place_1_Micro.placetiles");
        System.out.println("Converted to binary in " + Stopwatch.getElapsedSeconds() +" seconds");

        // Normal Convert
        Stopwatch.start();
        dataDownloader.minifyFile("Place_Tiles__2022_Original_0.txt", "Place_1_Mini.txt");
        System.out.println("Converted to binary in " + Stopwatch.getElapsedSeconds() +" seconds");

        PlaceParser2022 parser2022 = new PlaceParser2022();

        Stopwatch.start();
        parser2022.readAllBinary();
        System.out.println("Read binary in " + Stopwatch.getElapsedSeconds() +" seconds");

        Stopwatch.start();
        parser2022.readAll();
        System.out.println("Read all in " + Stopwatch.getElapsedSeconds() +" seconds");

        System.out.println(PlaceInfo.TIME_CORRECTION_2022);
    }

}
