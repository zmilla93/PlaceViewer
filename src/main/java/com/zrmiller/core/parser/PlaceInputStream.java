package com.zrmiller.core.parser;

import com.zrmiller.core.utility.TileEdit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Provides easy reading of a .placetiles binary file.
 */
public class PlaceInputStream extends BufferedInputStream {

    private TileEdit nextTile;

    public PlaceInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public boolean ready() throws IOException {
        return tryGetNextTile();
    }

    public TileEdit getNextTile() {
        return nextTile;
    }

    /**
     * Reads the next 10 bytes.
     * If color is -1, the entry is a rect and 6 more bytes are read.
     *
     * @return
     * @throws IOException
     */
    private boolean tryGetNextTile() throws IOException {
        byte[] data = new byte[TileEdit.BYTE_COUNT];
        int numBytesRead = read(data);
        if (numBytesRead != TileEdit.BYTE_COUNT)
            return false;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int timeStamp = buffer.getInt();
        short color = buffer.getShort();
        short x = buffer.getShort();
        short y = buffer.getShort();
        if (color == -1) {
            byte[] rectData = new byte[TileEdit.BYTE_INCREASE];
            numBytesRead = read(rectData);
            if (numBytesRead != TileEdit.BYTE_COUNT_INCREASED)
                return false;
            buffer = ByteBuffer.wrap(rectData);
            short x2 = buffer.getShort();
            short y2 = buffer.getShort();
            color = buffer.getShort();
            nextTile = new TileEdit(timeStamp, color, x, y, x2, y2);
        } else {
            nextTile = new TileEdit(timeStamp, color, x, y);
        }
        return true;
    }

}
