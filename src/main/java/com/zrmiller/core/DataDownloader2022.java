package com.zrmiller.core;

import com.zrmiller.core.utility.PlaceInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

public class DataDownloader2022 implements IDownloadTracker {


    // Dataset Details - https://www.reddit.com/r/place/comments/txvk2d/rplace_datasets_april_fools_2022/
    private static final String downloadURL = "https://placedata.reddit.com/data/canvas-history/2022_place_canvas_history-INDEX.csv.gzip";


    private String directory = "D:/Place/";
    //    private String outputPath = "D:/Place/Place_Tiles_2022_INDEX.gzip";
    private String zipFileName = "Place_Tiles__2022_Archive_INDEX.gzip";
    private String originalFileName = "Place_Tiles__2022_Original_INDEX.txt";
    private String miniFileName = "Place_Tiles__2022_Mini_INDEX.txt";
    private String binaryFileName = "Place_Tiles__2022_Binary_INDEX.placetiles";
    private String sortFileName = "SORT_NAMES.txt";

    private final HashSet<Integer> filesToIgnore = new HashSet<>();

    public DataDownloader2022() {

    }

    public void downloadAndUnzipFullDataset() {
        for (int i = 0; i < 78; i++) {
            System.out.println("Downloading file #" + i + "...");
            downloadFile(indexedName(zipFileName, i), getUrlString(i), this);
            System.out.println("Unzipping file #" + i + "...");
            unzip(indexedName(zipFileName, i), indexedName(originalFileName, i));
        }
    }

    public void runOrder(int index) {
        downloadFile(indexedName(zipFileName, index), getUrlString(index), this);
        unzip(indexedName(zipFileName, index), indexedName(originalFileName, index));
        minifyFile(indexedName(originalFileName, index), indexedName(miniFileName, index));
    }

    public void runDownload() {
//        downloadFile(0);
        System.out.println("URL:" + getUrlString(0));

//        downloadFile(indexedName(zipFileName, 0), getUrlString(0), this);
//        downloadFile(indexedName(zipFileName, 77), getUrlString(77), this);

//        unzip(indexedName(zipFileName, 0), indexedName(originalFileName, 0));
//        unzip(indexedName(zipFileName, 77), indexedName(originalFileName, 77));


        minifyFile(indexedName(originalFileName, 0), indexedName(miniFileName, 0));
        minifyFile(indexedName(originalFileName, 77), indexedName(miniFileName, 77));
    }

    private String indexedName(String input, int index) {
        return input.replaceFirst("INDEX", Integer.toString(index));
    }

    public void addFileToIgnore(int index) {
        if (!filesToIgnore.contains(index))
            filesToIgnore.add(index);
    }

    public void binTest() {
        minifyFileBinary("Place_Tiles__2022_Original_0.txt", "Place_1_Micro.txt");
    }

    private String getUrlString(int index) {
        StringBuilder builder = new StringBuilder("0000000000");
        if (index < 10) builder.append("0");
        builder.append(index);
        return downloadURL.replaceFirst("INDEX", builder.toString());
    }

    private int MAX_ATTEMPTS = 5;

    public boolean downloadFile(String fileName, String urlString, IDownloadTracker tracker) {
        int attempts = 1;
        while (attempts <= MAX_ATTEMPTS) {
            try {
                System.out.println("Downloading '" + fileName + "' from '" + urlString + "'...");
                URL url = new URL(urlString);
                HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                long fileSize = httpConnection.getContentLength();
                BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
//                FileOutputStream fos = new FileOutputStream(App.saveManager.INSTALL_DIRECTORY + File.separator + fileName);
                FileOutputStream fos = new FileOutputStream(directory + fileName);
                BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                byte[] data = new byte[1024];
                long downloadedFileSize = 0;
                int i;
                while ((i = in.read(data, 0, 1024)) >= 0) {
                    downloadedFileSize += i;
                    final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) fileSize)) * 100000d);
                    tracker.downloadPercentCallback(currentProgress);
                    bout.write(data, 0, i);
                }
                bout.close();
                in.close();
                System.out.println("Download complete.");
                return true;
            } catch (IOException e) {
                if (attempts == MAX_ATTEMPTS) {
                    System.out.println("Error downloading file from '" + urlString + "'");
                    return false;
                } else {
                    tracker.textCallback("Download failed, retrying...");
                    System.out.println("Download failed, retrying... (" + attempts + ")");
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    attempts++;
                }
            }
        }
        return false;
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
            FileOutputStream outputStream = new FileOutputStream(directory + dest);
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
//        int c = tokens[0].charAt(0);
//        if (c < 48 || c > 57) return null;  // Ignore lines that don't start with a number
//        String time = tokens[0].substring(0, tokens[0].length() - 4);
//        Timestamp timestamp = Timestamp.valueOf(time);
        long timeT = getTimestamp(tokens[0]);
        miniLineCount++;
        return timeT + "," + tokens[2] + "," + tokens[3] + "," + tokens[4] + "\n";


    }

    private boolean lineStartsWithNumber(String line) {
        int c = line.charAt(0);
        return c >= 48 && c <= 57;
    }

//    private long getTime(String timeToken) {
//        String time = timeToken.substring(0, timeToken.length() - 4);
//        Timestamp timestamp = Timestamp.valueOf(time);
//        return timestamp.getTime();
//    }

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

    public void dumpTimestamps(String outputDirectory) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + sortFileName)));
            for (int i = 0; i < 78; i++) {
                BufferedReader reader = new BufferedReader(new FileReader(directory + indexedName(originalFileName, i)));
                if (reader.ready()) {
                    reader.readLine();
                    String[] tokens = tokenizeLine(reader.readLine(), 5);
                    if (tokens == null) continue;
                    writer.write(tokens[0] + "," + i + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {

        }
    }

    public void cleanTimestamps(String inputPath, String outputPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directory + inputPath));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + outputPath)));
            while (reader.ready()) {
                String[] tokens = tokenizeLine(reader.readLine(), 2);
                if (tokens == null) continue;
                writer.write(tokens[1]);
                if (reader.ready())
                    writer.write(",");
            }
            reader.close();
            writer.close();
        } catch (IOException e) {

        }
    }

    public void scanForColors(String source) {
        try {
            HashSet<String> colors = new HashSet<>();
            BufferedReader reader = new BufferedReader(new FileReader(directory + source));
            while (reader.ready()) {
                String[] tokens = tokenizeLine(reader.readLine(), 5);
                if (tokens == null) continue;
                colors.add(tokens[2]);
            }
            for (String s : colors) {
                System.out.println("COLOR :: " + s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadPercentCallback(int progress) {
//        System.out.println("PROGRESS : " + progress);
    }

    @Override
    public void textCallback(String message) {

    }
}
