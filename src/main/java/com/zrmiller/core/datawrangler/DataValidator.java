package com.zrmiller.core.datawrangler;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.parser.PlaceInputStream;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.TileEdit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class DataValidator {

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

    // 2022
    public static boolean checkFileCount2022() {
        return getFileCount2022() == PlaceInfo.FILE_COUNT_2022;
    }

    public static int getFileCount2022() {
        int fileCount = 0;
        for (int i = 0; i < PlaceInfo.fileOrder.length; i++) {
            File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i));
            if (file.exists() && file.isFile())
                fileCount++;
        }
        return fileCount;
    }

    public static long getTotalFileSize2022() {
        long fileSize = 0;
        for (int i = 0; i < PlaceInfo.fileOrder.length; i++) {
            File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i));
            if (file.exists() && file.isFile())
                fileSize += file.length();
        }
        return fileSize;
    }

    public static int[] getFileOrder() {
        try {
            HashMap<Integer, Integer> orderMap = new HashMap<>();
            int[] timestampSorter = new int[PlaceInfo.FILE_COUNT_2022];
            int[] sortedKeys = new int[PlaceInfo.FILE_COUNT_2022];
            for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
                System.out.println("I:" + SaveManager.getSaveDirectory() + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i));
                PlaceInputStream inputStream = new PlaceInputStream(new FileInputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i)));
                inputStream.openStream();
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

}
