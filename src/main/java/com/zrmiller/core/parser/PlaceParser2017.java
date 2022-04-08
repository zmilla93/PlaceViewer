package com.zrmiller.core.parser;

import com.zrmiller.core.FileNames;
import com.zrmiller.core.TileEdit;

import java.io.*;

public class PlaceParser2017 implements IPlaceParser {

    private BufferedInputStream reader;
    private final String directory;
    private TileEdit currentTile;

    public PlaceParser2017(String directory) {
        this.directory = directory;
    }

    @Override
    public boolean openStream() {
        try {
            reader = new BufferedInputStream(new FileInputStream(directory + FileNames.minified2017));
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
