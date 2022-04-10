package com.zrmiller.core.parser;

import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.TileEdit;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.BufferedInputStream;
import java.io.IOException;

public class PlaceParser2022 extends AbstractPlaceParser {

    private BufferedInputStream currentStream;
    private BufferedInputStream nextStream;
    private boolean moreFiles;
    int fileIndex;
    private int fileLineCount;
    private TileEdit currentLine;

    @Override
    public boolean openStream() {
        try {
            currentStream = openInputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(PlaceInfo.fileOrder[0]));
            nextStream = openInputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(PlaceInfo.fileOrder[1]));
            fileIndex = 2;
            return true;
        } catch (IOException e) {
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
                nextStream = openInputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(PlaceInfo.fileOrder[fileIndex]));
                fileIndex++;
            } catch (IOException e) {
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
