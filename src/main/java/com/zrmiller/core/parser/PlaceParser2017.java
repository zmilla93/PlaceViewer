package com.zrmiller.core.parser;

import com.zrmiller.core.FileName;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;

import java.io.*;

public class PlaceParser2017 implements IPlaceParser {

    private BufferedInputStream reader;
    //    private final String directory;
    private TileEdit currentTile;

    public PlaceParser2017() {
//        this.directory = directory;
    }

    @Override
    public boolean openStream() {
        try {
            reader = new BufferedInputStream(new FileInputStream(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean closeStream() {
        if (reader != null) {
            try {
                reader.close();
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
        return tryReadLine();
    }

    @Override
    public TileEdit readNextLine() {
        return currentTile;
    }

    private boolean tryReadLine() throws IOException {
        byte[] line = new byte[TileEdit.BYTE_COUNT];
        int numBytesRead = reader.read(line);
        if (numBytesRead == -1)
            return false;
        currentTile = new TileEdit(line);
        return true;
    }

}
