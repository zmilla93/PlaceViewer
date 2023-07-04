package com.zrmiller.gui.mainframe.listeners;

public interface IPlayerControllerListener {

    void onStop();

    void onPlay();

    void onPause();

    void onSpeedChange(int tilesPerSecond);

}
