package com.zrmiller.core.datawrangler;

import com.zrmiller.core.FileName;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.modules.stopwatch.Stopwatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataWrangler2017 extends DataWrangler {

    private String downloadURL = "https://storage.googleapis.com/place_data_share/place_tiles.csv";
    private final ArrayList<IStatusTracker2017> statusTrackers = new ArrayList<>();

    public void downloadDataset() {
        if (!validateDirectory(Dataset.PLACE_2017.YEAR_STRING))
            return;
        downloadFile(FileName.ORIGINAL_2017.toString(), Dataset.PLACE_2017.YEAR_STRING, downloadURL);
        for (IStatusTracker2017 tracker : statusTrackers)
            tracker.onFileDownloadComplete();
    }

    public boolean sortAndMinify(boolean deleteSource) {
        try {
            System.out.flush();
            BufferedReader reader = new BufferedReader(new FileReader(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.ORIGINAL_2017));
            TileEdit[] tileEdits = new TileEdit[PlaceInfo.CLEAN_LINE_COUNT];
            int lineCount = 0;

            bytesDownloaded = 0;
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
                bytesDownloaded += line.length();
                lineCount++;
            }
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onFileReadComplete();
            // Sort
            TileEdit.sortCount = 0;
            Arrays.sort(tileEdits);
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onFileSortComplete();
            // Write Output
            tilesWritten = 0;
            bytesWritten = 0;
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017));
            writeYear(outputStream, Dataset.PLACE_2017.YEAR);
            writeMetaInt(outputStream);
            for (TileEdit tile : tileEdits) {
                outputStream.write(tile.toByteArray());
                bytesWritten = TileEdit.BYTE_COUNT;
                tilesWritten++;
            }
            outputStream.close();
            reader.close();
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onCompressComplete();
            if (deleteSource) {
                File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.ORIGINAL_2017);
                return file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            System.gc();
        }
        return true;
    }

    // FIXME : deleting data will fail if PlacePlayer has an open stream
    public boolean deleteData() {
        File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017);
        if (file.exists()) {
            if (!file.isFile())
                return false;
            Stopwatch.start();
            return file.delete();
        }
        return true;
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
