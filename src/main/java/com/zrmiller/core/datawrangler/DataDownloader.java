package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IFileDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IMultipleFileDownloadTracker;
import com.zrmiller.core.managers.SaveManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles file downloads while tracking progress.
 */
public abstract class DataDownloader {

    protected int fileSize;
    protected int bytesProcessed;
    private static final int BYTE_BUFFER_SIZE = 1024 * 4;
    private IFileDownloadTracker fileTracker;
    protected IMultipleFileDownloadTracker multipleFileTracker;
    private boolean cancel = false;
    public static DataDownloader activeDownloader;
    protected String yearString;

    public DataDownloader(String yearString) {
        this.yearString = yearString;
    }

    protected boolean downloadFile(String fileName, String urlString) {
        if (!validateDirectory(yearString))
            return false;
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(urlString).openConnection());
            fileSize = httpConnection.getContentLength();
            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
            String outputFile = SaveManager.settings.data.dataDirectory + yearString + File.separator + fileName;
            BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(outputFile)));
            byte[] data = new byte[BYTE_BUFFER_SIZE];
            bytesProcessed = 0;
            int numBytesRead;
            while ((numBytesRead = inputStream.read(data, 0, BYTE_BUFFER_SIZE)) >= 0) {
                bytesProcessed += numBytesRead;
                outputStream.write(data, 0, numBytesRead);
                if (fileTracker != null) fileTracker.updateProgress();
                if (Thread.currentThread().isInterrupted() || cancel) {
                    inputStream.close();
                    outputStream.close();
                    deleteFile(outputFile);
                    if (fileTracker != null) fileTracker.onDownloadComplete();
                    if (multipleFileTracker != null) multipleFileTracker.onDownloadComplete();
                    return false;
                }
            }
            inputStream.close();
            outputStream.close();
            if (fileTracker != null) fileTracker.onDownloadComplete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateDirectory(String yearString) {
        File file = new File(SaveManager.settings.data.dataDirectory + yearString);
        if (file.exists())
            return file.isDirectory();
        return file.mkdirs();
    }

    public void setFileTracker(IFileDownloadTracker tracker) {
        this.fileTracker = tracker;
    }

    public IFileDownloadTracker getFileTracker() {
        return fileTracker;
    }

    public void setMultipleFileTracker(IMultipleFileDownloadTracker tracker) {
        this.multipleFileTracker = tracker;
    }

    public IMultipleFileDownloadTracker getMultipleFileTracker() {
        return multipleFileTracker;
    }

    public int getFileSizeInBytes() {
        return fileSize;
    }

    public int getBytesProcessed() {
        return bytesProcessed;
    }

    public void cancelDownload() {
        cancel = true;
    }

    public boolean isCanceled() {
        return cancel;
    }

    public int getProgress() {
        return (int) Math.ceil(bytesProcessed / (float) fileSize * 100);
    }

    protected void deleteFile(String outputFile) {
        File file = new File(outputFile);
        boolean success = file.delete();
        if (!success) System.err.println("Failed to delete file '" + outputFile + "'.");
    }

}
