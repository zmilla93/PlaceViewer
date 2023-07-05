package com.zrmiller.core.parser;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.TileEdit;
import com.zrmiller.modules.strings.FileName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlaceParser2022 extends AbstractPlaceParser {

    private PlaceInputStream currentStream;
    private PlaceInputStream nextStream;
    private int fileIndex;
    private TileEdit currentLine;

    @Override
    public boolean openStream() {
        try {
            String firstFilePath = SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(0);
            String secondFilePath = SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(1);
            if (!validateFile(firstFilePath)) return false;
            if (!validateFile(secondFilePath)) return false;
            currentStream = new PlaceInputStream(Files.newInputStream(Paths.get(firstFilePath)));
            nextStream = new PlaceInputStream(Files.newInputStream(Paths.get(secondFilePath)));
            fileIndex = 2;
            return true;
        } catch (IOException e) {
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
            if (!cycleFiles()) return false;
            return currentStream.ready();
        }
        currentLine = currentStream.getNextTile();
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
                String filePath = SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(fileIndex);
                if (!validateFile(filePath)) return false;
                nextStream = new PlaceInputStream(Files.newInputStream(Paths.get(filePath)));
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

    private boolean validateFile(String filePath) {
        File file = new File(filePath);
        boolean valid = file.exists();
        if (!valid) System.err.println("File not found: " + filePath);
        return valid;
    }

}
