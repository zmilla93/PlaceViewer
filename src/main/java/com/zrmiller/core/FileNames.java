package com.zrmiller.core;

public class FileNames {

    public static final String original2017 = "Place_2017_Original.txt";
    public static final String minified2017 = "Place_2017.placetiles";
    public static final String zip2022 = "Place_2022_Archive_INDEX.gzip";
    public static final String original2022 = "Place_2022_Original_INDEX.gzip";
    public static final String minified2022 = "Place_2022_INDEX.placetiles";

    private String getIndexedName(String input, int index) {
        return input.replaceFirst("INDEX", Integer.toString(index));
    }

}
