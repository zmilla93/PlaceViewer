package com.zrmiller.core.parser;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.TileEdit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PlaceParser2017 extends AbstractPlaceParser {

    private PlaceInputStream inputStream;

    @Override
    public boolean openStream() {
        try {
            inputStream = new PlaceInputStream(new FileInputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017));
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
