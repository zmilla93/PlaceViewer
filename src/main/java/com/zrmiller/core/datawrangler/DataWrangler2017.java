package com.zrmiller.core.datawrangler;

import com.zrmiller.core.ColorConverter;
import com.zrmiller.core.FileNames;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.*;
import java.util.Arrays;

public class DataWrangler2017 extends DataWrangler {

    private String downloadURL = "https://storage.googleapis.com/place_data_share/place_tiles.csv";
    private Thread thread;

    public void downloadFile() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                downloadFile(FileNames.original2017, downloadURL);
            }
        });
        thread.start();
//        executor.execute(() -> downloadFile(FileNames.original2017, downloadURL));
    }

    public boolean sortAndMinify(String source, String dest, boolean deleteSource) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SaveManager.settingsSaveFile.data.dataDirectory + source));
            ColorConverter colorConverter = new ColorConverter();
            TileEdit[] tileEdits = new TileEdit[PlaceInfo.CLEAN_LINE_COUNT];
            int lineCount = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = tokenizeLine(line, 5);
                if (tokens == null) continue; // Skip Empty Lines
                if (!lineStartsWithNumber(tokens[0])) continue; // Skip lines that don't start with a timestamp
                if (tokens[2] == null || tokens[2].length() == 0) continue; // Skip corrupt lines
                TileEdit tile = new TileEdit(getTimestamp(tokens[0], PlaceInfo.INITIAL_TIME_2017),
                        Short.parseShort(tokens[4]),
                        Short.parseShort(tokens[2]),
                        Short.parseShort(tokens[3]));
                tileEdits[lineCount] = tile;
                lineCount++;
            }
            // Sort
            // TODO : report sort progress
            Arrays.sort(tileEdits);
            // Write Output
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(SaveManager.settingsSaveFile.data.dataDirectory + dest));
            for (TileEdit tile : tileEdits) {
                outputStream.write(tile.toByteArray());
            }
            outputStream.close();

            reader.close();
//            outputStream.close();
            if (deleteSource) {
                File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + source);
                return file.delete();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
