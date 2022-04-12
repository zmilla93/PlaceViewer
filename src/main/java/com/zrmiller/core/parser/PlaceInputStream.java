package com.zrmiller.core.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlaceInputStream extends BufferedInputStream {


    public PlaceInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public void openStream() {
        byte[] meta = new byte[6];
        try {
            read(meta);
//            ByteBuffer buffer = ByteBuffer.wrap(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
