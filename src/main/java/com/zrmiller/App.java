package com.zrmiller;

import com.zrmiller.core.DataDownloader2022;
import com.zrmiller.core.PlaceParser2022;
import com.zrmiller.core.utility.PlaceInfo;

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

        System.out.println("byte : " + Byte.MAX_VALUE);
        System.out.println("Short : " + Short.MAX_VALUE);
        System.out.println("Int : " + Integer.MAX_VALUE);

        DataDownloader2022 dataDownloader = new DataDownloader2022();
        long startTime = System.nanoTime();
        dataDownloader.minifyFileBinary("Place_Tiles__2022_Original_0.txt", "Place_1_Micro.placetiles");

        long endTime = System.nanoTime();
        long elapsed = endTime - startTime;
        System.out.println("Converted to binary in " + elapsed / 1000000);

        startTime = System.nanoTime();
        dataDownloader.minifyFile("Place_Tiles__2022_Original_0.txt", "Place_1_Mini.txt");
        endTime = System.nanoTime();
        elapsed = endTime - startTime;
        System.out.println("Converted to reg in " + elapsed / 1000000);

        PlaceParser2022 parser2022 = new PlaceParser2022();
        parser2022.readAll();
//        parser2022.read();

        System.out.println(PlaceInfo.TIME_CORRECTION_2022);
    }
}
