package com.zrmiller.core.parser;

import com.zrmiller.core.TileEdit;

import java.io.IOException;

public interface IPlaceParser {

    boolean openStream();

    boolean closeStream();

    boolean ready() throws IOException;

    TileEdit readNextLine() throws IOException;

}
