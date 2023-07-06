package com.zrmiller.core.data;

public enum FileName {

    ORIGINAL_2017("Place_2017_Original.txt"),
    BINARY_2017("Place_2017.placetiles"),
    ZIPPED_2022("Place_2022_Archive_INDEX.gzip"),
    ORIGINAL_2022("Place_2022_Original_INDEX.gzip"),
    BINARY_2022("Place_2022_INDEX.placetiles"),
    ;

    private final String fileName;

    FileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIndexedName(int index) {
        return fileName.replaceFirst("INDEX", Integer.toString(index));
    }

    @Override
    public java.lang.String toString() {
        return fileName;
    }

}
