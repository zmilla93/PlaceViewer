package com.zrmiller.core.utility;

import java.awt.*;

public class ZUtil {

    public static GridBagConstraints getGC() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        return gc;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

}
