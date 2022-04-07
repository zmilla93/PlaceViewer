package com.zrmiller.core.parser;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlaceParser2022 implements IPlaceParser{

    private String directory;
    private String fileNameTemplate;

    private BufferedInputStream currentStream;
    private BufferedInputStream nextStream;
    private boolean moreFiles;
    int fileIndex;
    private int fileLineCount;
    private TileEdit currentLine;

    public PlaceParser2022(String directory, String fileNameTemplate) {
        this.directory = directory;
        this.fileNameTemplate = fileNameTemplate;
    }

    @Override
    public boolean openStream() {
//        goToFile(99);
        try {
            System.out.println("F:" + directory + getIndexedName(fileNameTemplate, PlaceInfo.fileOrder[0]));
            currentStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(fileNameTemplate, PlaceInfo.fileOrder[0])));
            nextStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(fileNameTemplate, PlaceInfo.fileOrder[1])));
            fileIndex = 2;
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean closeStream() {
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

    @Override
    public boolean ready() throws IOException {
        return tryReadNextTile();
    }

    @Override
    public TileEdit readNextLine() throws IOException {
        return currentLine;
    }

    private boolean tryReadNextTile() throws IOException {
        byte[] line = new byte[TileEdit.BYTE_COUNT];
        int numBytesRead = currentStream.read(line);
        if (numBytesRead == -1) {
            if (nextStream != null) {
                cycleFiles();
                numBytesRead = currentStream.read(line);
                fileLineCount = 0;
            }
        }
        if (numBytesRead == -1){
            currentLine = null;
            return false;
        }
//        TileEdit edit = new TileEdit(line);
//        if(fileLineCount == 0)
//            System.out.println("T" + edit.timestamp);
        currentLine = new TileEdit(line);
        fileLineCount++;
        return true;
    }

    private boolean cycleFiles() {
//
        currentStream = nextStream;
        if (fileIndex < PlaceInfo.fileOrder.length) {
            try {
                System.out.println("Next:" + PlaceInfo.fileOrder[fileIndex]);
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
