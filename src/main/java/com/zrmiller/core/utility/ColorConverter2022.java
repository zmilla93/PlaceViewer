package com.zrmiller.core.utility;

import java.awt.*;
import java.util.HashMap;

/**
 * Handles converting between string values, int keys, and actual Colors for 2022
 */
public class ColorConverter2022 {

    private final HashMap<String, Short> colorToIntMap;

    public static final Color[] intToColorArr = new Color[]{
            new Color(109, 0, 26),
            new Color(190, 0, 57),
            new Color(255, 69, 0),
            new Color(255, 168, 0),
            new Color(255, 214, 53),
            new Color(255, 248, 184),
            new Color(0, 163, 104),
            new Color(0, 204, 120),
            new Color(126, 237, 86),
            new Color(0, 117, 111),
            new Color(0, 158, 170),
            new Color(0, 204, 192),
            new Color(36, 80, 164),
            new Color(54, 144, 234),
            new Color(81, 233, 244),
            new Color(73, 58, 193),
            new Color(106, 92, 255),
            new Color(148, 179, 255),
            new Color(129, 30, 159),
            new Color(180, 74, 192),
            new Color(228, 171, 255),
            new Color(222, 16, 127),
            new Color(255, 56, 129),
            new Color(255, 153, 170),
            new Color(109, 72, 47),
            new Color(156, 105, 38),
            new Color(255, 180, 112),
            new Color(0, 0, 0),
            new Color(81, 82, 82),
            new Color(137, 141, 144),
            new Color(212, 215, 217),
            new Color(255, 255, 255),
    };

    public ColorConverter2022() {
        colorToIntMap = new HashMap<>();
        colorToIntMap.put("#6D001A", (short) 0);
        colorToIntMap.put("#BE0039", (short) 1);
        colorToIntMap.put("#FF4500", (short) 2);
        colorToIntMap.put("#FFA800", (short) 3);
        colorToIntMap.put("#FFD635", (short) 4);
        colorToIntMap.put("#FFF8B8", (short) 5);
        colorToIntMap.put("#00A368", (short) 6);
        colorToIntMap.put("#00CC78", (short) 7);
        colorToIntMap.put("#7EED56", (short) 8);
        colorToIntMap.put("#00756F", (short) 9);
        colorToIntMap.put("#009EAA", (short) 10);
        colorToIntMap.put("#00CCC0", (short) 11);
        colorToIntMap.put("#2450A4", (short) 12);
        colorToIntMap.put("#3690EA", (short) 13);
        colorToIntMap.put("#51E9F4", (short) 14);
        colorToIntMap.put("#493AC1", (short) 15);
        colorToIntMap.put("#6A5CFF", (short) 16);
        colorToIntMap.put("#94B3FF", (short) 17);
        colorToIntMap.put("#811E9F", (short) 18);
        colorToIntMap.put("#B44AC0", (short) 19);
        colorToIntMap.put("#E4ABFF", (short) 20);
        colorToIntMap.put("#DE107F", (short) 21);
        colorToIntMap.put("#FF3881", (short) 22);
        colorToIntMap.put("#FF99AA", (short) 23);
        colorToIntMap.put("#6D482F", (short) 24);
        colorToIntMap.put("#9C6926", (short) 25);
        colorToIntMap.put("#FFB470", (short) 26);
        colorToIntMap.put("#000000", (short) 27);
        colorToIntMap.put("#515252", (short) 28);
        colorToIntMap.put("#898D90", (short) 29);
        colorToIntMap.put("#D4D7D9", (short) 30);
        colorToIntMap.put("#FFFFFF", (short) 31);
    }

    public short colorToInt(String colorString) {
        return colorToIntMap.get(colorString);
    }

    public static Color intToColor(int color) {
        return intToColorArr[color];
    }

}
