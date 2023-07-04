package com.zrmiller.core.enums;

import com.zrmiller.core.utility.ColorConverter2022;
import com.zrmiller.core.utility.PlaceInfo;

import java.awt.*;
import java.io.File;
import java.text.NumberFormat;

public enum Dataset {

    PLACE_2017(2017, 1001, 1001, 16567564, PlaceInfo.canvasColors, 0),
    PLACE_2022(2022, 2000, 2000, 153383211, ColorConverter2022.intToColorArr, 31),
    ;

    public final int YEAR;
    public final String YEAR_STRING;
    public final int CANVAS_SIZE_X;
    public final int CANVAS_SIZE_Y;
    public final int FRAME_COUNT;
    public final String FORMATTED_FRAME_COUNT;
    public final Color[] COLOR_ARRAY;
    public final int WHITE_INDEX;

    Dataset(int year, int x, int y, int frameCount, Color[] colorArray, int whiteIndex) {
        YEAR = year;
        YEAR_STRING = Integer.toString(year);
        CANVAS_SIZE_X = x;
        CANVAS_SIZE_Y = y;
        FRAME_COUNT = frameCount;
        FORMATTED_FRAME_COUNT = NumberFormat.getInstance().format(frameCount);
        COLOR_ARRAY = colorArray;
        WHITE_INDEX = whiteIndex;
    }

    @Override
    public String toString() {
        return YEAR_STRING;
    }

    public String getYearPath() {
        return YEAR_STRING + File.separator;
    }

}
