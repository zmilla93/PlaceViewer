package com.zrmiller.core.colors;

import com.zrmiller.core.utility.ZUtil;

public enum ColorMode {

    NORMAL, HEATMAP_GRAYSCALE("Heatmap (Grayscale)"), HEATMAP_COLOR("Heatmap (Color)");

    private final String displayName;

    ColorMode() {
        displayName = ZUtil.enumToString(name());
    }

    ColorMode(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
