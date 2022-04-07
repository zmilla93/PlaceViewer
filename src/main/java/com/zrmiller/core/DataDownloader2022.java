package com.zrmiller.core;

import com.zrmiller.core.utility.PlaceInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

// Dataset Details - https://www.reddit.com/r/place/comments/txvk2d/rplace_datasets_april_fools_2022/

public class DataDownloader2022 implements IDownloadTracker {

    private static final String downloadURLTemplate = "https://placedata.reddit.com/data/canvas-history/2022_place_canvas_history-INDEX.csv.gzip";

    private String directory = "D:/Place/";

    private String prefix = "Place_2022_INDEX";
    private String binaryExtension = ".placetiles";

    private String zipFileName = prefix + "_Archive_INDEX.gzip";
    private String originalFileName = prefix + "_Original_INDEX.txt";
    private String binaryFileName = prefix + binaryExtension;

    private final HashSet<Integer> filesToIgnore = new HashSet<>();
    private static final int BYTE_BUFFER_SIZE = 1024 * 4;

    public DataDownloader2022() {

    }

    public void downloadAndUnzipFullDataset() {
        for (int i = 0; i < 78; i++) {
            System.out.println("Downloading file #" + i + "...");
            downloadFile(indexedName(zipFileName, i), getUrlString(i));
            System.out.println("Unzipping file #" + i + "...");
            unzip(indexedName(zipFileName, i), indexedName(originalFileName, i));
        }
    }

    public void downloadUnzipAndMinify(int index) {
        downloadFile(indexedName(zipFileName, index), getUrlString(index));
        unzip(indexedName(zipFileName, index), indexedName(originalFileName, index), false);
        minifyFileBinary(indexedName(originalFileName, index), indexedName(binaryFileName, index));
    }

    private String indexedName(String input, int index) {
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

    public boolean downloadFile(String fileName, String urlString) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(urlString).openConnection());
            long fileSize = httpConnection.getContentLength();
            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(directory + fileName));
            byte[] data = new byte[BYTE_BUFFER_SIZE];
            int bytesDownloaded = 0;
            int numBytesRead;
            while ((numBytesRead = inputStream.read(data, 0, BYTE_BUFFER_SIZE)) >= 0) {
                bytesDownloaded += numBytesRead;
                float currentProgress = (bytesDownloaded / (float) fileSize);
                outputStream.write(data, 0, numBytesRead);
                System.out.println("CURP:" + currentProgress);
            }
            inputStream.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unzip(String source, String dest) {
        return unzip(source, dest, true);
    }

    public boolean unzip(String source, String dest, boolean deleteZip) {
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
            if (deleteZip) {
                File file = new File(directory + source);
                boolean success = file.delete();  // Fixme : check delete status
                if (!success) {
                    System.out.println("Failed to delete:" + directory + source);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    int miniLineCount = 0;

    public boolean minifyFile(String source, String dest) {
        miniLineCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directory + source));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + dest)));
            while (reader.ready()) {
                String formattedLine = getFormattedLine(reader.readLine());
                if (formattedLine == null) continue;
                writer.write(formattedLine);
            }
            reader.close();
            writer.close();
            System.out.println("LINE COUT : " + miniLineCount);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean minifyFileBinary(String source, String dest) {
        miniLineCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directory + source));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(directory + dest));
            ColorConverter colorConverter = new ColorConverter();
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = tokenizeLine(line, 5);
                if (tokens == null) continue; // Skip Empty Lines
                if (!lineStartsWithNumber(tokens[0])) continue; // Skip lines that don't start with a timestamp
                try {
                    TileEdit edit = new TileEdit(getTimestamp(tokens[0]), colorConverter.colorToInt(tokens[2]), Short.parseShort(tokens[3].substring(1)), Short.parseShort(tokens[4].substring(0, tokens[4].length() - 1)));
                    outputStream.write(edit.toByteArray());
                } catch (IllegalArgumentException e) {
                    System.out.println("BADNUM:" + line);
                    e.printStackTrace();
                }
            }
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getFormattedLine(String input) {
        // TODO
        if (!lineStartsWithNumber(input))
            return null;
        String[] tokens = tokenizeLine(input, 5);
        if (tokens == null) return null; // TODO : TEMP
        long timeT = getTimestamp(tokens[0]);
        miniLineCount++;
        return timeT + "," + tokens[2] + "," + tokens[3] + "," + tokens[4] + "\n";


    }

    private boolean lineStartsWithNumber(String line) {
        int c = line.charAt(0);
        return c >= 48 && c <= 57;
    }

    private int getTimestamp(String timeToken) throws IllegalArgumentException {
        String timeString = timeToken.substring(0, timeToken.length() - 4);
        Timestamp timestamp = Timestamp.valueOf(timeString);
        return (int) (timestamp.getTime() - (long) PlaceInfo.TIME_CORRECTION_2022);
    }

    private String[] tokenizeLine(String input, int tokenCount) {
        String[] tokens = new String[tokenCount];
        StringBuilder builder = new StringBuilder();
        int tokenIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            if (tokenIndex >= tokenCount) {
                System.out.println("BADNESS:" + input);
                return null;
            }
            if (input.charAt(i) == ',') {
                tokens[tokenIndex] = builder.toString();
                builder.setLength(0);
                tokenIndex++;
            } else {
                builder.append(input.charAt(i));
            }
        }
        tokens[tokenIndex] = builder.toString();
        return tokens;
    }




    @Override
    public void downloadPercentCallback(int progress) {
//        System.out.println("PROGRESS : " + progress);
    }

    @Override
    public void textCallback(String message) {

    }
}
