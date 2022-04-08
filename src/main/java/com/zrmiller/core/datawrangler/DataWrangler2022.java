package com.zrmiller.core.datawrangler;

import com.zrmiller.core.ColorConverter;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.*;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

// Dataset Details - https://www.reddit.com/r/place/comments/txvk2d/rplace_datasets_april_fools_2022/

public class DataWrangler2022 extends DataWrangler {

    private static final String downloadURLTemplate = "https://placedata.reddit.com/data/canvas-history/2022_place_canvas_history-INDEX.csv.gzip";

    private String directory = "D:/Place/2022-Binary/";

    private String prefix = "Place_2022_INDEX";
    private String binaryExtension = ".placetiles";

    private String zipFileName = prefix + "_Archive.gzip";
    private String originalFileName = prefix + "_Original.txt";
    private String binaryFileName = prefix + binaryExtension;

    private final HashSet<Integer> filesToIgnore = new HashSet<>();


    public DataWrangler2022() {

    }

    public void downloadAndProcessFullDataset() {
        for (int i = 0; i < 78; i++) {
            downloadUnzipAndMinify(i);
        }
    }

    public void downloadUnzipAndMinify(int index) {
        int fileCount = index + 1;
        System.out.println("Downloading file #" + fileCount + "...");
        downloadFile(getIndexedName(zipFileName, index), getUrlString(index));
        System.out.println("Unzipping file #" + fileCount + "...");
        unzip(getIndexedName(zipFileName, index), getIndexedName(originalFileName, index));
        System.out.println("Minifying file #" + fileCount + "...");
        minifyFile(getIndexedName(originalFileName, index), getIndexedName(binaryFileName, index));
    }

    private String getIndexedName(String input, int index) {
        return input.replaceFirst("INDEX", Integer.toString(index));
    }

    public void addFileToIgnore(int index) {
        if (!filesToIgnore.contains(index))
            filesToIgnore.add(index);
    }

    private String getUrlString(int index) {
        StringBuilder builder = new StringBuilder("0000000000");
        if (index < 10) builder.append("0");
        builder.append(index);
        return downloadURLTemplate.replaceFirst("INDEX", builder.toString());
    }

//    public boolean downloadFile(String fileName, String urlString) {
//        try {
//            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(urlString).openConnection());
//            long fileSize = httpConnection.getContentLength();
//            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
//            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(directory + fileName));
//            byte[] data = new byte[BYTE_BUFFER_SIZE];
//            int bytesDownloaded = 0;
//            int numBytesRead;
//            while ((numBytesRead = inputStream.read(data, 0, BYTE_BUFFER_SIZE)) >= 0) {
//                bytesDownloaded += numBytesRead;
//                float currentProgress = (bytesDownloaded / (float) fileSize);
//                outputStream.write(data, 0, numBytesRead);
////                System.out.println("CURP:" + currentProgress);
//            }
//            inputStream.close();
//            outputStream.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean unzip(String source, String dest) {
        return unzip(source, dest, true);
    }

    public boolean unzip(String source, String dest, boolean deleteSource) {
        try {
            GZIPInputStream inputStream = new GZIPInputStream(new FileInputStream(directory + source));
            OutputStream outputStream = new FileOutputStream(directory + dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            if (deleteSource) {
                File file = new File(directory + source);
                boolean success = file.delete();
                if (!success) {
                    System.out.println("Failed to delete:" + directory + source);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean minifyFile(String source, String dest) {
        return minifyFile(source, dest, true);
    }

    public boolean minifyFile(String source, String dest, boolean deleteSource) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directory + source));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(directory + dest));
            ColorConverter colorConverter = new ColorConverter();
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = tokenizeLine(line, 5);
                if (tokens == null) continue; // Skip Empty Lines
                if (!lineStartsWithNumber(tokens[0])) continue; // Skip lines that don't start with a timestamp
                TileEdit edit = new TileEdit(getTimestamp(tokens[0], PlaceInfo.TIME_CORRECTION_2022), colorConverter.colorToInt(tokens[2]), Short.parseShort(tokens[3].substring(1)), Short.parseShort(tokens[4].substring(0, tokens[4].length() - 1)));
                outputStream.write(edit.toByteArray());
            }
            reader.close();
            outputStream.close();
            if (deleteSource) {
                File file = new File(directory + source);
                return file.delete();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    private boolean lineStartsWithNumber(String line) {
//        int c = line.charAt(0);
//        return c >= 48 && c <= 57;
//    }

//    private int getTimestamp(String timeToken) throws IllegalArgumentException {
//        String timeString = timeToken.substring(0, timeToken.length() - 4);
//        Timestamp timestamp = Timestamp.valueOf(timeString);
//        return (int) (timestamp.getTime() - (long) PlaceInfo.TIME_CORRECTION_2022);
//    }

}
