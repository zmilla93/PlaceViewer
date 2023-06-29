package com.zrmiller.core.enums;

import java.util.Locale;

public enum PlaybackSpeed {

    SLOW(1, 1000),
    MEDIUM(100, 100000),
    FAST(1000, 1000000),
    FASTER(100, 10000000),
    ;

    private String name;
    public final int MINIMUM_SPEED;
    public final int MAXIMUM_SPEED;

    PlaybackSpeed(int min, int max) {
        MINIMUM_SPEED = min;
        MAXIMUM_SPEED = max;
    }

    @Override
    public String toString() {
        if (name == null) {
            name = name();
            name = name.charAt(0) + name.substring(1, name().length()).toLowerCase(Locale.ROOT);
        }
        return name;
    }
}
