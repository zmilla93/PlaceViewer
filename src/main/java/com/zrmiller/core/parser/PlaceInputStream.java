package com.zrmiller.core.parser;

import com.zrmiller.core.utility.TileEdit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PlaceInputStream extends BufferedInputStream {

    public PlaceInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public boolean openStream() {
        byte[] meta = new byte[TileEdit.META_COUNT];
        try {
            int numByteRead = read(meta);
            if (numByteRead != TileEdit.META_COUNT) {
                return false;
            }
            ByteBuffer buffer = ByteBuffer.wrap(meta);
            int year = buffer.getShort();
            int format = buffer.getInt();
            System.out.println("Year : " + year);
            System.out.println("Format : " + format);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public TileEdit getNextTile() throws IOException {
        byte[] data = new byte[TileEdit.BYTE_COUNT];
        int numBytesRead = read(data);
        assert numBytesRead == TileEdit.BYTE_COUNT;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int timeStamp = buffer.getInt();
        short color = buffer.getShort();
        short x = buffer.getShort();
        short y = buffer.getShort();
        boolean isRect = false;
        byte[] rectData;
        if (color == -1) {
            rectData = new byte[TileEdit.BYTE_INCREASE];
            numBytesRead = read(rectData);
            assert numBytesRead == TileEdit.BYTE_COUNT_INCREASED;
            buffer = ByteBuffer.wrap(rectData);
            short x2 = buffer.getShort();
            short y2 = buffer.getShort();
            color = buffer.getShort();
            return new TileEdit(timeStamp, color, x, y, x2, y2);
        } else {
            return new TileEdit(timeStamp, color, x, y);
        }
    }

}
