package com.zrmiller;

import com.zrmiller.core.parser.PlaceParser2022;

import java.io.IOException;
import java.text.NumberFormat;

public class StatApp {

    public static void main(String[] args) {
        PlaceParser2022 parser = new PlaceParser2022();
        parser.openStream();
        int i = 0;
        System.out.println("Running...");
        long time = System.currentTimeMillis();
        try {
            while (parser.ready()) {
                parser.readNextLine();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Tile count: " + NumberFormat.getInstance().format(i));
        System.out.println("Run time: " + (time / 1000f) + " seconds");
    }

}
