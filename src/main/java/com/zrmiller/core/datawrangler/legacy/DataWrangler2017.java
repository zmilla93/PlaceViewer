package com.zrmiller.core.datawrangler.legacy;

import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2017;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.TileEdit;
import com.zrmiller.modules.stopwatch.Stopwatch;
import com.zrmiller.modules.strings.FileName;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

@Deprecated
public class DataWrangler2017 extends DataWrangler {

    private String downloadURL = "https://storage.googleapis.com/place_data_share/place_tiles.csv";
    private final ArrayList<IStatusTracker2017> statusTrackers = new ArrayList<>();

    public void downloadAndProcessDataset() {
        if (!downloadDataset())
            return;
        sortAndCompress();
    }

    private boolean downloadDataset() {
        if (!validateDirectory(Dataset.PLACE_2017.YEAR_STRING))
            return false;
        boolean success = downloadFile(FileName.ORIGINAL_2017.toString(), Dataset.PLACE_2017.YEAR_STRING, downloadURL);
        if (success)
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onFileDownloadComplete();
        return success;
    }

    private boolean sortAndCompress() {
        return sortAndCompress(true);
    }

    private boolean sortAndCompress(boolean deleteSource) {
        try {
            System.out.flush();
            BufferedReader reader = new BufferedReader(new FileReader(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.ORIGINAL_2017));
            TileEdit[] tileEdits = new TileEdit[PlaceInfo.CLEAN_LINE_COUNT_2017];
            int lineCount = 0;
            bytesProcessed = 0;
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
                bytesProcessed += line.length();
                lineCount++;
                if (Thread.currentThread().isInterrupted()) {
                    reader.close();
                    cancelDownload();
                    return false;
                }
            }
            reader.close();
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onFileReadComplete();
            // Sort
            TileEdit.sortCount = 0;
            Arrays.sort(tileEdits);
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onFileSortComplete();
            // Write Output
            bytesProcessed = 0;
            fileSize = tileEdits.length * TileEdit.BYTE_COUNT;
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017));
            writeYear(outputStream, Dataset.PLACE_2017.YEAR);
            writeMetaInt(outputStream);
            for (TileEdit tile : tileEdits) {
                outputStream.write(tile.toByteArray());
                bytesProcessed += TileEdit.BYTE_COUNT;
                if (Thread.currentThread().isInterrupted()) {
                    outputStream.close();
                    cancelDownload();
                    return false;
                }
            }
            outputStream.close();
            for (IStatusTracker2017 tracker : statusTrackers)
                tracker.onCompressComplete();
            if (deleteSource) {
                File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.ORIGINAL_2017);
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
        File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017);
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

    @Override
    protected void cancelDownload() {
        File original = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.getYearPath() + FileName.ORIGINAL_2017);
        File compressed = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.getYearPath() + FileName.ORIGINAL_2017);
        if (original.exists() && original.isFile())
            original.delete();
        if (compressed.exists() && compressed.isFile())
            compressed.delete();
        for (IStatusTracker2017 tracker : statusTrackers) {
            tracker.onCancel();
        }
    }
}
