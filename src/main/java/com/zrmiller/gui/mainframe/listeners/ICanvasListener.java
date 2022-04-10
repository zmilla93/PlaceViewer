package com.zrmiller.gui.mainframe.listeners;

import java.awt.*;

public interface ICanvasListener {

    void onZoom(int zoomLevel);

    void onPan(Point center);

    void onDraw(int frameCount);

}
