package com.zrmiller.core.parser;

import com.zrmiller.gui.CanvasPanel;

import java.io.*;

public class PlaceParser {

    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean running;

    private int lineCount;

    private int[] colorBuffer = new int[CanvasPanel.CANVAS_SIZE_X * CanvasPanel.CANVAS_SIZE_Y];

    public void openInputStream(String inputPath) {
        try {
            reader = new BufferedReader(new FileReader(inputPath));
            lineCount = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean ready() throws IOException {
        return reader.ready();
    }

    /**
     * Reads the next line from the file and returns the result.
     *
     * @return An array of 3 integers. [0] = x, [1] = y, [2] = color
     * @throws IOException
     */
    public int[] getNextLine() throws IOException {
        String[] line = reader.readLine().split(",");
        int[] ints = new int[]{Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2])};
        int index = ints[0] + ints[1] * CanvasPanel.CANVAS_SIZE_X;
        colorBuffer[index] = ints[2];
        lineCount++;
        return ints;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int[] getColorBuffer() {
        return colorBuffer;
    }

    public void close() {
        if (reader != null) {
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
