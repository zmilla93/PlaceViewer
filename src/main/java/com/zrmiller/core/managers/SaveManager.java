package com.zrmiller.core.managers;

import com.zrmiller.core.saving.savefiles.SettingsSaveFile;
import com.zrmiller.modules.saving.SaveFile;

import java.io.File;

public class SaveManager {

    // Save Files
    public static SaveFile<SettingsSaveFile> settingsSaveFile = new SaveFile<>(getSaveDirectory() + "settings.json", SettingsSaveFile.class);

    // Save Directory
    private static String saveDirectory;
    private static final String folderWin = "PlaceViewer";
    private static final String folderOther = ".placeviewer";

    public static String getSaveDirectory() {
        if (saveDirectory == null) {
            String os = (System.getProperty("os.name")).toUpperCase();
            if (os.contains("WIN")) {
                saveDirectory = System.getenv("LocalAppData") + File.separator + folderWin + File.separator;
            } else {
                saveDirectory = System.getProperty("user.home") + File.separator + folderOther + File.separator;
            }
        }
        if (!verify(saveDirectory))
            return null;
        return saveDirectory;
    }

    private static boolean verify(String saveDirectory) {
        File file = new File(saveDirectory);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.err.println("[SaveManager] Error while creating save directory!");
                return false;
            }
        }
        return true;
    }

}
