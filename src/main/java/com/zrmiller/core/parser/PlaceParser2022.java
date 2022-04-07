package com.zrmiller.core.parser;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlaceParser2022 {

    private String directory;
    private String fileNameTemplate;

    private BufferedInputStream currentStream;
    private BufferedInputStream nextStream;
    private boolean moreFiles;
    int fileIndex = 0;

    public PlaceParser2022(String directory, String fileNameTemplate) {
        this.directory = directory;
        this.fileNameTemplate = fileNameTemplate;
    }

    public boolean openStream() {
//        goToFile(99);
        try {
            System.out.println("F:" + directory + getIndexedName(fileNameTemplate, PlaceInfo.fileOrder[0]));
            currentStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(fileNameTemplate, PlaceInfo.fileOrder[0])));
            nextStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(fileNameTemplate, PlaceInfo.fileOrder[1])));
            fileIndex = 2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean closeStreams() {
        try {
            if (currentStream != null) {
                currentStream.close();
                currentStream = null;
            }
            if (nextStream != null) {
                nextStream.close();
                nextStream = null;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean ready() {
        return false;
    }

    public TileEdit readNextLine() throws IOException {
        byte[] line = new byte[TileEdit.BYTE_COUNT];
//        System.out.println("reading...");
        int numBytesRead = currentStream.read(line);
//        System.out.println("READ:" + numBytesRead);
        if (numBytesRead == -1) {
//            System.out.println("UMM");
            if (nextStream != null) {
                cycleFiles();
                numBytesRead = currentStream.read(line);
            }
        }
        if (numBytesRead == -1)
            return null;
        TileEdit edit = new TileEdit(line);;
//        System.out.println("x" + edit.x);
        return new TileEdit(line);
    }

    private boolean cycleFiles() {
        System.out.println("Cycling:" + fileIndex);
        currentStream = nextStream;
        if (fileIndex < PlaceInfo.fileOrder.length) {
            try {
                nextStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(fileNameTemplate, PlaceInfo.fileOrder[fileIndex])));
                fileIndex++;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getIndexedName(String input, int index) {
        return input.replaceFirst("INDEX", Integer.toString(index));
    }

}
