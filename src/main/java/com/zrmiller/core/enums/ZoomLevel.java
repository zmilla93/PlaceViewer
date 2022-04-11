package com.zrmiller.core.enums;

public enum ZoomLevel {

    Zoom_Out_4(4, true),
    Zoom_Out_3(3, true),
    Zoom_Out_2(2, true),
    Zoom_1(1),
    Zoom_2(2),
    Zoom_3(3),
    Zoom_4(4),
    Zoom_5(5),
    Zoom_6(6),
    Zoom_7(7),
    Zoom_8(8),
    Zoom_9(9),
    Zoom_10(10),
    Zoom_12(12),
    Zoom_14(14),
    Zoom_16(16),
    Zoom_18(18),
    Zoom_2000(20),
    ;

    public final String name;
    public final int modifier;
    public final boolean zoomOut;

    ZoomLevel(int modifier) {
        this(modifier, false);
    }

    ZoomLevel(int modifier, boolean zoomOut) {
        this.name = getName(modifier, zoomOut);
        this.modifier = modifier;
        this.zoomOut = zoomOut;
    }

    private String getName(int zoom, boolean zoomOut) {
        if (zoomOut)
            return 100 / zoom + "%";
        return 100 * zoom + "%";
    }

    @Override
    public String toString() {
        return name;
    }

}
