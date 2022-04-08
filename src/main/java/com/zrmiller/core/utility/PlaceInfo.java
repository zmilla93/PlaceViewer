package com.zrmiller.core.utility;

import java.awt.*;
import java.text.NumberFormat;

public class PlaceInfo {

    public static final int ORIGINAL_LINE_COUNT = 16567568;
    public static final int CLEAN_LINE_COUNT = 16567564;

    public static final String CLEAN_LINE_COUNT_FORMATTED = NumberFormat.getInstance().format(CLEAN_LINE_COUNT);

    public static int CANVAS_2017_X = 1001;
    public static int CANVAS_2017_Y = 1001;

    public static int CANVAS_2022_X = 2000;
    public static int CANVAS_2022_Y = 2000;

    public static int CANVAS_SIZE_X = 2000;
    public static int CANVAS_SIZE_Y = 2000;

//    public static int CANVAS_SIZE_X = 1001;

    // 2022 Stats
    public static final long INITIAL_TIME_2017 = 1490136935548L;
    public static final long FINAL_TIME_2017 = 1491256735739L;
    public static final int TIME_CORRECTION_2017 = (int) (FINAL_TIME_2017 - INITIAL_TIME_2017);
    public static final long INITIAL_TIME_2022 = 1648835050315L;
    public static final long FINAL_TIME_2022 = 1649135640207L;
    public static final int TIME_CORRECTION_2022 = (int) (FINAL_TIME_2022 - INITIAL_TIME_2022);

    // Colors
    public static final Color[] canvasColors = new Color[]{
            new Color(255, 255, 255),
            new Color(201, 201, 201),
            new Color(79, 79, 79),
            new Color(0, 0, 0),
            new Color(222, 156, 227),
            new Color(243, 8, 8),
            new Color(245, 107, 36),
            new Color(122, 62, 18),
            new Color(232, 196, 5),
            new Color(164, 220, 128),
            new Color(17, 47, 7),
            new Color(40, 181, 194),
            new Color(72, 132, 217),
            new Color(13, 56, 227),
            new Color(181, 60, 222),
            new Color(104, 16, 171),
    };

    public static int[] fileOrder = new int[]{1, 2, 3, 5, 6, 10, 11, 8, 13, 4, 9, 15, 12, 18, 14, 16, 20, 17, 23, 19, 21, 28, 7, 29, 30, 31, 32, 33, 25, 35, 36, 27, 22, 0, 40, 41, 24, 34, 44, 37, 38, 39, 48, 43, 26, 45, 46, 47, 42, 49, 50, 55, 52, 57, 58, 54, 61, 56, 63, 53, 59, 60, 62, 51, 70, 64, 65, 66, 72, 73, 74, 75, 76, 77, 67, 69, 68, 71};

}
