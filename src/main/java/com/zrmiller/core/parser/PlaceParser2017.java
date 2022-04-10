package com.zrmiller.core.parser;

import com.zrmiller.core.FileName;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
            byte[] meta = new byte[6];
            int numBytesRead = reader.read(meta);
            if (numBytesRead != 6)
                return false;
            // TODO : Validate file
//            ByteBuffer buffer = ByteBuffer.wrap(meta);
//            short year = buffer.getShort();
//            int m = buffer.getInt();
//            System.out.println("YEAR : " + year);
//            System.out.println("META : " + m);
            return true;
        } catch (IOException e) {
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
        // FIXME : This can throw a null pointer exception when switching datasets
        byte[] line = new byte[TileEdit.BYTE_COUNT];
        int numBytesRead = reader.read(line);
        if (numBytesRead == -1)
            return false;
        currentTile = new TileEdit(line);
        return true;
    }

}
