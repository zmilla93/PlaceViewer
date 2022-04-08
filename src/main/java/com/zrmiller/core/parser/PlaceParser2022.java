package com.zrmiller.core.parser;

import com.zrmiller.core.FileNames;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlaceParser2022 implements IPlaceParser {

    private String directory;

    private BufferedInputStream currentStream;
    private BufferedInputStream nextStream;
    private boolean moreFiles;
    int fileIndex;
    private int fileLineCount;
    private TileEdit currentLine;

    public PlaceParser2022(String directory) {
        this.directory = directory;
    }

    @Override
    public boolean openStream() {
        try {
            currentStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(FileNames.minified2022, PlaceInfo.fileOrder[0])));
            nextStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(FileNames.minified2022, PlaceInfo.fileOrder[1])));
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
        if (numBytesRead == -1) {
            currentLine = null;
            return false;
        }
        currentLine = new TileEdit(line);
        fileLineCount++;
        return true;
    }

    private boolean cycleFiles() {
//
        currentStream = nextStream;
        if (fileIndex < PlaceInfo.fileOrder.length) {
            try {
                nextStream = new BufferedInputStream(new FileInputStream(directory + getIndexedName(FileNames.minified2022, PlaceInfo.fileOrder[fileIndex])));
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
