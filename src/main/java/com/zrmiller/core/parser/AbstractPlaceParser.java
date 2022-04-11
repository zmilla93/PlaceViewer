package com.zrmiller.core.parser;

import com.zrmiller.core.utility.TileEdit;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractPlaceParser {

    // FIXME : Move this to somewhere more logical
    private static final int META_BYTE_COUNT = 6;

    /**
     * Opens a file and reads the first 6 bytes.
     *
     * @param fullPath
     * @return
     * @throws IOException
     */
    protected BufferedInputStream openInputStream(String fullPath) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fullPath));
        byte[] meta = new byte[META_BYTE_COUNT];
        int bytesRead = inputStream.read(meta);
        // TODO : Validate metadata and throw exception if invalid
//        if(bytesRead != META_BYTE_COUNT)
        return inputStream;
    }

    abstract boolean openStream();

    abstract boolean closeStream();

    abstract boolean ready() throws IOException;

    abstract TileEdit readNextLine() throws IOException;

}
