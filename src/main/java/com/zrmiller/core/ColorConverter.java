package com.zrmiller.core;

import java.util.HashMap;

public class ColorConverter {

    private HashMap<String, Short> colorToIntMap;

    public ColorConverter() {
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

}
