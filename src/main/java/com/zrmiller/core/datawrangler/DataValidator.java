package com.zrmiller.core.datawrangler;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.File;

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

}
