package com.zrmiller.gui.mainframe.listeners;

public interface IPlayerControllerListener {

    void onPlay();

    void onPause();

    void onStop();

    void onTogglePlayPause();

    void onSpeedChange(int tilesPerSecond);

}
