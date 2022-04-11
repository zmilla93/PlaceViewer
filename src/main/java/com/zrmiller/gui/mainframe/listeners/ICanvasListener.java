package com.zrmiller.gui.mainframe.listeners;

import com.zrmiller.core.enums.ZoomLevel;

import java.awt.*;

public interface ICanvasListener {

    void onZoom(ZoomLevel zoomLevel);

    void onPan(Point center);

    void onDraw(int frameCount);

}
