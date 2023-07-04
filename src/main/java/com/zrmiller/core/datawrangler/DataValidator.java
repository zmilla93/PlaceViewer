package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IValidationListener2017;
import com.zrmiller.core.datawrangler.callbacks.IValidationListener2022;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.parser.PlaceInputStream;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.TileEdit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataValidator {

    private static final ArrayList<IValidationListener2017> listeners2017 = new ArrayList<>();
    private static final ArrayList<IValidationListener2022> listeners2022 = new ArrayList<>();

    // 2017
    public static boolean checkExists2017() {
        return getFileSize2017() > 0;
    }

    public static long getFileSize2017() {
        File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017 + File.separator + FileName.BINARY_2017);
        if (file.exists() && file.isFile())
            return file.length();
        return -1;
    }

    public static void runValidation2017() {
        long fileSize = getFileSize2017();
        boolean valid = fileSize > 0;
        for (IValidationListener2017 listener : listeners2017)
            listener.onValidation2017(valid, fileSize);
    }

    // 2022
    public static boolean checkFileCount2022() {
        return getFileCount2022() == PlaceInfo.FILE_COUNT_2022;
    }

    public static int getFileCount2022() {
        int fileCount = 0;
        for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
            File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i));
            if (file.exists() && file.isFile())
                fileCount++;
        }
        return fileCount;
    }

    public static long getTotalFileSize2022() {
        long fileSize = 0;
        for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
            File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i));
            if (file.exists() && file.isFile())
                fileSize += file.length();
        }
        return fileSize;
    }

    public static void runValidation2022() {
        int fileCount = getFileCount2022();
        boolean valid = fileCount == PlaceInfo.FILE_COUNT_2022;
        long installSize = getTotalFileSize2022();
        for (IValidationListener2022 listener : listeners2022) {
            listener.onValidation2022(valid, fileCount, installSize);
        }
    }

    public static int[] getFileOrder() {
        try {
            HashMap<Integer, Integer> orderMap = new HashMap<>();
            int[] timestampSorter = new int[PlaceInfo.FILE_COUNT_2022];
            int[] sortedKeys = new int[PlaceInfo.FILE_COUNT_2022];
            for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
                System.out.println("I:" + SaveManager.getSaveDirectory() + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i));
                PlaceInputStream inputStream = new PlaceInputStream(Files.newInputStream(Paths.get(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i))));
                inputStream.ready();
                TileEdit tile = inputStream.getNextTile();
                orderMap.put(tile.timestamp, i);
                timestampSorter[i] = tile.timestamp;
            }
            Arrays.sort(timestampSorter);
            for (int i = 0; i < sortedKeys.length; i++) {
                sortedKeys[i] = orderMap.get(timestampSorter[i]);
            }
            return sortedKeys;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addValidationListener2017(IValidationListener2017 listener) {
        listeners2017.add(listener);
    }

    public static void addValidationListener2022(IValidationListener2022 listener) {
        listeners2022.add(listener);
    }

}
