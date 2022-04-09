package com.zrmiller.core.enums;

import com.zrmiller.core.ColorConverter;
import com.zrmiller.core.utility.PlaceInfo;

import java.awt.*;
import java.io.File;

public enum Dataset {

    PLACE_2017("2017", 1001, 1001, PlaceInfo.canvasColors, 0),
    PLACE_2022("2012", 2000, 2000, ColorConverter.intToColorArr, 31),
    ;

    public final String YEAR_STRING;
    public final int CANVAS_SIZE_X;
    public final int CANVAS_SIZE_Y;
    public final Color[] COLOR_ARRAY;
    public final int WHITE_INDEX;

    Dataset(String yearString, int x, int y, Color[] colorArray, int whiteIndex) {
        YEAR_STRING = yearString;
        CANVAS_SIZE_X = x;
        CANVAS_SIZE_Y = y;
        COLOR_ARRAY = colorArray;
        WHITE_INDEX = whiteIndex;
    }

    @Override
    public String toString() {
        return YEAR_STRING;
    }

    public String getYearPath(){
        return YEAR_STRING + File.separator;
    }

}
