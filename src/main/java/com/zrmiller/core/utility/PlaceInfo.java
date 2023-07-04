package com.zrmiller.core.utility;

import java.awt.*;
import java.text.NumberFormat;

public class PlaceInfo {

    public static final int ORIGINAL_LINE_COUNT_2017 = 16567568;
    public static final int CLEAN_LINE_COUNT_2017 = 16567564;
    public static final int CLEAN_LINE_COUNT_2022 = 160353085;

    public static final String CLEAN_LINE_COUNT_2017_FORMATTED = NumberFormat.getInstance().format(CLEAN_LINE_COUNT_2017);

    public static final int FILE_COUNT_2022 = 79;

    // 2022 Stats
    public static final long INITIAL_TIME_2017 = 1490136935548L;
    public static final long FINAL_TIME_2017 = 1491256735739L;
    //    public static final int TIME_CORRECTION_2017 = (int) (FINAL_TIME_2017 - INITIAL_TIME_2017);
    public static final long INITIAL_TIME_2022 = 1648835050315L;
    public static final long FINAL_TIME_2022 = 1649135640207L;
//    public static final int TIME_CORRECTION_2022 = (int) (FINAL_TIME_2022 - INITIAL_TIME_2022);

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

    // File order of original dataset. Preprocessed dataset has been ordered correctly.
    public static int[] fileOrder = new int[]{10, 6, 9, 14, 15, 17, 13, 11, 7, 16, 12, 18, 24, 20, 26, 19, 21, 22, 23, 25, 27, 28, 35, 30, 31, 32, 33, 34, 39, 42, 36, 37, 38, 43, 44, 45, 46, 48, 29, 51, 47, 54, 3, 2, 41, 0, 1, 55, 56, 4, 50, 57, 59, 53, 40, 49, 62, 52, 65, 61, 69, 8, 63, 64, 60, 66, 67, 5, 74, 68, 75, 76, 77, 70, 71, 72, 73, 58, 78};

}
