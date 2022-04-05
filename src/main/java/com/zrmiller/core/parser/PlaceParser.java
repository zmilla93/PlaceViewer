package com.zrmiller.core.parser;

import com.zrmiller.gui.CanvasPanel;

import java.io.*;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class PlaceParser {

    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean running;
    private IParserCallback callback;

    private int lineCount;

    private int[] colorBuffer = new int[CanvasPanel.CANVAS_SIZE_X * CanvasPanel.CANVAS_SIZE_Y];

    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            callback.statusUpdate("");
        }
    };

    public void firstClean(String inputPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputPath + "_partial")));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = line.split(",");
                if (tokens.length != 5 || tokens[0].length() < 5) continue;
                String time = tokens[0].substring(0, tokens[0].length() - 4);
                Timestamp timestamp = Timestamp.valueOf(time);
                String timeString = Long.toString(timestamp.getTime());
                writer.write(timeString + "," + tokens[2] + "," + tokens[3] + "," + tokens[4] + "\n");
            }
            reader.close();
            writer.close();
            System.out.println("Clean end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void secondClean(String inputPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath + "_sorted"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputPath + "_final")));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = line.split(",");
                writer.write(tokens[1] + "," + tokens[2] + "," + tokens[3] + "\n");
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public int[] getNextTokens() throws IOException {
        String[] line = reader.readLine().split(",");
        int[] ints = new int[]{Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2])};
        int index = ints[0] + ints[1] * CanvasPanel.CANVAS_SIZE_X;
        colorBuffer[index] = ints[2];
        lineCount++;
        return ints;
    }

    public int getLineCount(){
        return lineCount;
    }

    public int[] getColorBuffer(){
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
