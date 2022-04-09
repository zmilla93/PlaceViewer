package com.zrmiller.core.datawrangler;

import com.zrmiller.core.managers.SaveManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

public abstract class DataWrangler {

    private static final int BYTE_BUFFER_SIZE = 1024 * 4;
//    protected String directory;

    protected int fileSize;
    protected int bytesDownloaded;
    protected int bytesWritten;
    protected int tilesWritten;

    //    protected static Executor executor = Executors.newSingleThreadExecutor();
    private final ArrayList<IStatusListener> statusListeners = new ArrayList<>();

    protected boolean downloadFile(String fileName, String yearString, String urlString) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(urlString).openConnection());
            fileSize = httpConnection.getContentLength();
            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(SaveManager.settingsSaveFile.data.dataDirectory + yearString + File.separator + fileName));
            byte[] data = new byte[BYTE_BUFFER_SIZE];
            bytesDownloaded = 0;
            int numBytesRead;
            while ((numBytesRead = inputStream.read(data, 0, BYTE_BUFFER_SIZE)) >= 0) {
                bytesDownloaded += numBytesRead;
                float currentProgress = (bytesDownloaded / (float) fileSize);
                outputStream.write(data, 0, numBytesRead);
//                System.out.println("CURP:" + currentProgress);
            }
            inputStream.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected String[] tokenizeLine(String input, int tokenCount) {
        String[] tokens = new String[tokenCount];
        StringBuilder builder = new StringBuilder();
        int tokenIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            if (tokenIndex >= tokenCount) {
                // TODO : FIXME
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

    public boolean validateDirectory(String yearString) {
        File file = new File(SaveManager.settingsSaveFile.data.dataDirectory + yearString);
        if (file.exists())
            return file.isDirectory();
        return file.mkdirs();
    }

    protected boolean lineStartsWithNumber(String line) {
        int c = line.charAt(0);
        return c >= 48 && c <= 57;
    }

    protected int getTimestamp(String timeToken, long timeCorrection) throws IllegalArgumentException {
        String timeString = timeToken.substring(0, timeToken.length() - 4);
        Timestamp timestamp = Timestamp.valueOf(timeString);
        return (int) (timestamp.getTime() - timeCorrection);
    }

    protected long getLongTimestamp(String timeToken, int timeCorrection) throws IllegalArgumentException {
        String timeString = timeToken.substring(0, timeToken.length() - 4);
        Timestamp timestamp = Timestamp.valueOf(timeString);
        return timestamp.getTime();
    }

    public int getBytesDownloaded() {
        return bytesDownloaded;
    }

    public int getFileSizeInBytes() {
        return fileSize;
    }

    public int getTilesWritten() {
        return tilesWritten;
    }

    public int getBytesWritten() {
        return bytesWritten;
    }

    public void addListener(IStatusListener listener) {
        statusListeners.add(listener);
    }

    public void removeListener(IStatusListener listener) {
        statusListeners.add(listener);
    }

    public void removeAllListeners() {
        statusListeners.clear();
    }

    /**
     * Returns 0-100
     *
     * @return
     */
    public int getProgress() {
        return (int) Math.ceil(bytesDownloaded / (float) fileSize * 100);
    }

}
