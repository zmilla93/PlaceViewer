package com.zrmiller.gui;

import java.awt.*;

public interface ICanvasListener {

    void onZoom(int zoomLevel);
    void onPan(Point center);

}
