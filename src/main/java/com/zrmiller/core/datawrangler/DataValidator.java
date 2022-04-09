package com.zrmiller.core.datawrangler;

import com.zrmiller.core.FileNames;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.File;

public class DataValidator {

    /**
     * Checks if 2017 binary file exists
     * @return File size when valid, -1 if not.
     */
    public static long validate2017() {
        File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017 + File.separator + FileNames.minified2017);
        if (file.exists() && file.isFile())
            return file.length();
        return -1;
    }

    public static boolean validate2022(){
        for(int i = 0;i< PlaceInfo.fileOrder.length;i++){
//            File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileNames )
        }
        return false;
    }

}
