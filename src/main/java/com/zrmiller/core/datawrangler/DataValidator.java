package com.zrmiller.core.datawrangler;

import com.zrmiller.core.FileNames;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;

import java.io.File;

public class DataValidator {

    public static long validate2017() {
        File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileNames.minified2017);
        if (file.exists() && file.isFile())
            return file.length();
        return -1;
    }

}
