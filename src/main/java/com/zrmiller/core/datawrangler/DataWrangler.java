package com.zrmiller.core.datawrangler;

import com.zrmiller.core.managers.SaveManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * IMPORTANT : When writing binary files, first make a call to writeYear, then writeMetaInt.
 */
public abstract class DataWrangler {

    protected int fileSize;
    protected int bytesProcessed;
    private static final int BYTE_BUFFER_SIZE = 1024 * 4;

    protected boolean downloadFile(String fileName, String yearString, String urlString) {
        if (!validateDirectory(yearString))
            return false;
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(urlString).openConnection());
            fileSize = httpConnection.getContentLength();
            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(SaveManager.settings.data.dataDirectory + yearString + File.separator + fileName));
            byte[] data = new byte[BYTE_BUFFER_SIZE];
            bytesProcessed = 0;
            int numBytesRead;
            while ((numBytesRead = inputStream.read(data, 0, BYTE_BUFFER_SIZE)) >= 0) {
                bytesProcessed += numBytesRead;
                outputStream.write(data, 0, numBytesRead);
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

    protected ArrayList<String> tokenizeLine(String input) {
        ArrayList<String> tokens = new ArrayList<>(5);
//        String[] tokens = new String[tokenCount];
        StringBuilder builder = new StringBuilder(200);
//        int tokenIndex = 0;
        for (int i = 0; i < input.length(); i++) {
//            if (tokenIndex >= tokenCount) {
//                return null;
//            }
            if (input.charAt(i) == ',') {
                tokens.add(builder.toString());
                builder.setLength(0);
//                tokenIndex++;
            } else {
                builder.append(input.charAt(i));
            }
        }
        tokens.add(builder.toString());
//        tokens[tokenIndex] = builder.toString();
        return tokens;
    }


    public boolean validateDirectory(String yearString) {
        File file = new File(SaveManager.settings.data.dataDirectory + yearString);
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

    public int getBytesProcessed() {
        return bytesProcessed;
    }

    public int getFileSizeInBytes() {
        return fileSize;
    }

    protected void writeYear(BufferedOutputStream outputStream, int year) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short) year);
        outputStream.write(buffer.array());
    }

    protected void writeMetaInt(BufferedOutputStream outputStream) throws IOException {
        writeMetaInt(outputStream, 0);
    }

    protected void writeMetaInt(BufferedOutputStream outputStream, int value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        outputStream.write(buffer.array());
    }

    /**
     * Returns 0-100
     *
     * @return
     */
    public int getProgress() {
        return (int) Math.ceil(bytesProcessed / (float) fileSize * 100);
    }

}
