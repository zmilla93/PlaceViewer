package com.zrmiller.modules.strings;

import com.zrmiller.core.managers.SaveManager;

import java.io.File;

public class References {

    public static final String APP_NAME = "PlaceViewer";
    public static final String AUTHOR_NAME = "zmilla93";
    public static final String GITHUB_URL = "https://github.com/zmilla93/PlaceViewer";
    public static final String GITHUB_ISSUES_URL = "https://github.com/zmilla93/PlaceViewer/issues";

    public static final String EXPORT_FOLDER_NAME = "Exports";

    public static final int MENU_BAR_ICON_SIZE = 16;

    public static String getDataFolder() {
        String dir = SaveManager.settings.data.dataDirectory;
        if (dir == null) return null;
        File file = new File(dir);
        if (!file.isDirectory()) return null;
        return SaveManager.settings.data.dataDirectory;
    }

    public static String getExportFolder() {
        String dir = SaveManager.settings.data.dataDirectory;
        if (dir == null) return null;
        File file = new File(dir);
        if (!file.isDirectory()) return null;
        return SaveManager.settings.data.dataDirectory + References.EXPORT_FOLDER_NAME + File.separator;
    }

}
