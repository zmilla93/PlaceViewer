package com.zrmiller.core.datawrangler;

import com.zrmiller.core.FileNames;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataWrangler2017 extends DataWrangler {

    private String downloadURL = "https://storage.googleapis.com/place_data_share/place_tiles.csv";
    private Thread thread;
    private final ArrayList<IStatusTracker2017> statusTrackers = new ArrayList<>();

    public void downloadFile() {
        if (!validateDirectory(Dataset.PLACE_2017.YEAR_STRING))
            return;
        downloadFile(FileNames.original2017, Dataset.PLACE_2017.YEAR_STRING, downloadURL);
        System.out.println("FILE DOWNLOAD FINISHED");
        for (IStatusTracker2017 tracker : statusTrackers)
            tracker.onFileDownloaded();
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        thread.start();
//        executor.execute(() -> downloadFile(FileNames.original2017, downloadURL));
    }

    public boolean sortAndMinify(boolean deleteSource) {
        try {
            System.out.println("sort and minify!");
            System.out.flush();
            BufferedReader reader = new BufferedReader(new FileReader(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileNames.original2017));
            TileEdit[] tileEdits = new TileEdit[PlaceInfo.CLEAN_LINE_COUNT];
            int lineCount = 0;

            bytesDownloaded = 0;
            while (reader.ready()) {
//                System.out.println("B:" + bytesDownloaded);
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
                bytesDownloaded += line.length();
                lineCount++;
            }
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onFileRead();
            // Sort
            TileEdit.sortCount = 0;
            Arrays.sort(tileEdits);
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onFileSorted();
            // Write Output
            tilesWritten = 0;
            bytesWritten = 0;
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + FileNames.minified2017));
            for (TileEdit tile : tileEdits) {
                outputStream.write(tile.toByteArray());
                bytesWritten = TileEdit.BYTE_COUNT;
                tilesWritten++;
            }

            outputStream.close();

            reader.close();
//            outputStream.close();
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onMinifiyComplete();
            if (deleteSource) {
                File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + FileNames.original2017);
                return file.delete();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addStatusTracker(IStatusTracker2017 tracker) {
        statusTrackers.add(tracker);
    }

    public void removeStatusTracker(IStatusTracker2017 tracker) {
        statusTrackers.remove(tracker);
    }

    public void removeAllStatusTrackers() {
        statusTrackers.clear();
    }

}
