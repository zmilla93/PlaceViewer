package com.zrmiller.core.parser;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.TileEdit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlaceParser2022 extends AbstractPlaceParser {

    private PlaceInputStream currentStream;
    private PlaceInputStream nextStream;
    private int fileIndex;
    private int fileLineCount;
    private TileEdit currentLine;

    @Override
    public boolean openStream() {
        try {
            currentStream = new PlaceInputStream(Files.newInputStream(Paths.get(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(0))));
            nextStream = new PlaceInputStream(Files.newInputStream(Paths.get(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(1))));
            fileIndex = 2;
            return true;
        } catch (IOException e) {
            // FIXME:
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
        if (!currentStream.ready()) {
            if (nextStream == null)
                return false;
            cycleFiles();
            return currentStream.ready();
        }
        currentLine = currentStream.getNextTile();
        fileLineCount++;
        return true;
    }

    @Override
    public TileEdit readNextLine() throws IOException {
        return currentLine;
    }

    private boolean cycleFiles() {
        currentStream = nextStream;
        if (fileIndex < PlaceInfo.FILE_COUNT_2022) {
            try {
                nextStream = new PlaceInputStream(Files.newInputStream(Paths.get(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(fileIndex))));
                fileIndex++;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            nextStream = null;
        }
        return true;
    }

    private String getIndexedName(String input, int index) {
        return input.replaceFirst("INDEX", Integer.toString(index));
    }

}
