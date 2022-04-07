package com.zrmiller.core.parser;

import com.zrmiller.core.utility.PlaceInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PlacePlayer {

    PlaceParser parser = new PlaceParser();
    private int updatesPerSecond = 1000000;
    private int TEMP_FPS = 60;
    private Timer timer = new Timer();
    int frameCount = 0;

    private final int[] colorBuffer = new int[PlaceInfo.CANVAS_SIZE_X * PlaceInfo.CANVAS_SIZE_Y];
    private final String inputPath;

    private boolean playing;

    public PlacePlayer(String inputPath) {
        this.inputPath = inputPath;
        parser.openInputStream(inputPath);
    }

    public void play() {
        if(playing) return;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < updatesPerSecond / TEMP_FPS; i++) {
                        applyNextFrame();
                    }
                } catch (IOException e) {
                    pause();
                }
            }
        }, 0, 1000 / TEMP_FPS);
        playing = true;
    }

    public void pause() {
        timer.cancel();
        timer.purge();
        playing = false;
    }

    public void setSpeed(int speed) {
        updatesPerSecond = speed;
    }

    public void reset() {
        pause();
        parser.close();
        frameCount = 0;
        Arrays.fill(colorBuffer, 0);
        parser.openInputStream(inputPath);
    }

    public void jumpToFrame(int frame) {

    }

    private void applyNextFrame() throws IOException {
        if (parser.ready()) {
            int[] tokens = parser.getNextLine();
            colorBuffer[tokens[0] + tokens[1] * PlaceInfo.CANVAS_SIZE_X] = tokens[2];
            frameCount++;
        } else {
            pause();
        }
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int[] getColorBuffer() {
        return colorBuffer;
    }

}
