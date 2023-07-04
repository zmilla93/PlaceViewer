package com.zrmiller.core.parser;

import com.zrmiller.core.utility.TileEdit;

import java.io.IOException;

public abstract class AbstractPlaceParser {

    abstract boolean openStream();

    abstract boolean closeStream();

    abstract boolean ready() throws IOException;

    abstract TileEdit readNextLine() throws IOException;

}
