package com.zrmiller.core.parser;

public interface IParserCallback {

    void statusUpdate(String report);

    void complete(String report);

}
