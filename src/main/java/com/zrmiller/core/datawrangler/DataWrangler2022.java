package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2022;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.ColorConverter2022;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.TileEdit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class DataWrangler2022 extends DataWrangler {

    private static final String downloadURLTemplate = "https://placedata.reddit.com/data/canvas-history/2022_place_canvas_history-INDEX.csv.gzip";

    private final ArrayList<IStatusTracker2022> statusTrackers = new ArrayList<>();

    private int expectedFiles;
    private int filesDownloaded;

    public void downloadAndProcessFullDataset() {
        expectedFiles = PlaceInfo.FILE_COUNT_2022 - DataValidator.getFileCount2022();
        for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
            File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + FileName.BINARY_2022.getIndexedName(i));
            if (file.exists())
                continue;
            downloadUnzipAndCompress(i);
        }
    }

    public void downloadUnzipAndCompress(int index) {
//        int fileCount = index + 1;
//        System.out.println("Downloading file #" + fileCount + "...");
        // NOTE : File order is fixed right here
        downloadFile(FileName.ZIPPED_2022.getIndexedName(index), Dataset.PLACE_2022.YEAR_STRING, getUrlString(index));
        for (IStatusTracker2022 tracker : statusTrackers)
            tracker.onFileDownloadComplete();
//        System.out.println("Unzipping file #" + fileCount + "...");
        unzip(FileName.ZIPPED_2022.getIndexedName(index), FileName.ORIGINAL_2022.getIndexedName(index));
        for (IStatusTracker2022 tracker : statusTrackers)
            tracker.onUnZipComplete();
//        System.out.println("Compressing file #" + fileCount + "...");
        compressFile(FileName.ORIGINAL_2022.getIndexedName(index), FileName.BINARY_2022.getIndexedName(index));
        filesDownloaded++;
        for (IStatusTracker2022 tracker : statusTrackers)
            tracker.onCompressComplete();
    }

    private String getUrlString(int index) {
        StringBuilder builder = new StringBuilder("0000000000");
        if (index < 10) builder.append("0");
        builder.append(index);
        return downloadURLTemplate.replaceFirst("INDEX", builder.toString());
    }

    public boolean unzip(String source, String dest) {
        return unzip(source, dest, true);
    }

    public boolean unzip(String source, String dest, boolean deleteSource) {
        try {
            GZIPInputStream inputStream = new GZIPInputStream(new FileInputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + source));
            OutputStream outputStream = new FileOutputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + dest);
            final int BUFFER_SIZE = 1024;
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            bytesProcessed = 0;
            fileSize = (int) Files.size(Paths.get(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + source));
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                bytesProcessed += 512;
            }
            inputStream.close();
            outputStream.close();
            if (deleteSource) {
                File sourceFile = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + source);
                boolean success = sourceFile.delete();
                if (!success) {
                    System.out.println("Failed to delete:" + SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + source);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean compressFile(String source, String dest) {
        return compressFile(source, dest, true);
    }

    public boolean compressFile(String source, String dest, boolean deleteSource) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + source));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + dest));
            ColorConverter2022 colorConverter = new ColorConverter2022();
            writeYear(outputStream, Dataset.PLACE_2022.YEAR);
            writeMetaInt(outputStream);
            File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + source);
            fileSize = (int)file.length();
            bytesProcessed = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                bytesProcessed += line.length();
                ArrayList<String> tokens = tokenizeLine(line);
                if (tokens == null) continue; // Skip Empty Lines
                if (!lineStartsWithNumber(tokens.get(0))) continue; // Skip lines that don't start with a timestamp
                TileEdit edit;
                if (tokens.size() == 7) {
                    edit = new TileEdit(getTimestamp(tokens.get(0), PlaceInfo.INITIAL_TIME_2022), colorConverter.colorToInt(tokens.get(2)),
                            Short.parseShort(tokens.get(3).substring(1)), Short.parseShort(tokens.get(4).substring(0, tokens.get(4).length() - 1)),
                            Short.parseShort(tokens.get(5)), Short.parseShort(tokens.get(6).substring(0, tokens.get(6).length() - 1)));
                } else {
                    edit = new TileEdit(getTimestamp(tokens.get(0), PlaceInfo.INITIAL_TIME_2022), colorConverter.colorToInt(tokens.get(2)),
                            Short.parseShort(tokens.get(3).substring(1)), Short.parseShort(tokens.get(4).substring(0, tokens.get(4).length() - 1)));
                }
                outputStream.write(edit.toByteArray());
            }
            reader.close();
            outputStream.close();
            if (deleteSource) {
                file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2022.getYearPath() + source);
                return file.delete();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getExpectedFiles() {
        return expectedFiles;
    }

    public int getFilesDownloaded() {
        return filesDownloaded;
    }

    public void addStatusTracker(IStatusTracker2022 tracker) {
        statusTrackers.add(tracker);
    }

    public void removeStatusTracker(IStatusTracker2022 tracker) {
        statusTrackers.remove(tracker);
    }

    public void removeAllStatusTrackers() {
        statusTrackers.clear();
    }

}
