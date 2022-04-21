package com.zrmiller.gui.mainframe.listeners;

public interface IPlayerControllerListener {

    void onReset();

    void onPlay();

    void onPause();

    void onSpeedChange(int tilesPerSecond);

}
