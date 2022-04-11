package com.zrmiller.core.enums;

public enum ZoomLevel {

    Zoom_25("25%", 4, true),
    Zoom_50("50%", 2, true),
    Zoom_100("100%", 1),
    Zoom_200("200%", 2),
    Zoom_400("300%", 3),
    Zoom_800("400%", 4),
    ;

    public final String name;
    public final int modifier;
    public final boolean zoomOut;

    ZoomLevel(String name, int modifier) {
        this(name, modifier, false);
    }

    ZoomLevel(String name, int modifier, boolean zoomOut) {
        this.name = name;
        this.modifier = modifier;
        this.zoomOut = zoomOut;
    }

    @Override
    public String toString() {
        return name;
    }

}
