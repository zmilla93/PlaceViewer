package com.zrmiller.core.parser;

import com.zrmiller.core.data.Dataset;
import com.zrmiller.core.data.FileName;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.TileEdit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlaceParser2017 extends AbstractPlaceParser {

    private PlaceInputStream inputStream;

    @Override
    public boolean openStream() {
        try {
            String path = SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017;
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("File not found: " + path);
                return false;
            }
            inputStream = new PlaceInputStream(Files.newInputStream(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean closeStream() {
        if (inputStream != null) {
            try {
                inputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean ready() throws IOException {
        return inputStream.ready();
    }

    @Override
    public TileEdit readNextLine() {
        return inputStream.getNextTile();
    }

}
