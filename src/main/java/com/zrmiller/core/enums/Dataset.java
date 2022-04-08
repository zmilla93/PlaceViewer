package com.zrmiller.core.enums;

import com.zrmiller.core.ColorConverter;
import com.zrmiller.core.utility.PlaceInfo;

import java.awt.*;

public enum Dataset {

    PLACE_2017(1001, 1001, PlaceInfo.canvasColors, 0),
    PLACE_2022(2000, 2000, ColorConverter.intToColorArr, 31),
    ;

    public final int CANVAS_SIZE_X;
    public final int CANVAS_SIZE_Y;
    public final Color[] colorArray;
    public final int whiteIndex;

    Dataset(int x, int y, Color[] colorArray, int whiteIndex) {
        CANVAS_SIZE_X = x;
        CANVAS_SIZE_Y = y;
        this.colorArray = colorArray;
        this.whiteIndex = whiteIndex;
    }

}
