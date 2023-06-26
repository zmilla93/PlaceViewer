package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IFileDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IMultipleFileDownloadTracker;
import com.zrmiller.core.managers.SaveManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class DataDownloader {

    protected int fileSize;
    protected int bytesProcessed;
    private static final int BYTE_BUFFER_SIZE = 1024 * 4;
    private IDownloadTracker tracker;
    private IFileDownloadTracker fileTracker;
    protected IMultipleFileDownloadTracker multipleFileTracker;
    private boolean cancel = false;

    protected boolean downloadFile(String fileName, String yearString, String urlString) {
        if (!validateDirectory(yearString))
            return false;
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(urlString).openConnection());
            fileSize = httpConnection.getContentLength();
            BufferedInputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
            String outputFile = SaveManager.settings.data.dataDirectory + yearString + File.separator + fileName;
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
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
                    tracker.onDownloadComplete();
                    return false;
                }
            }
            inputStream.close();
            outputStream.close();
            tracker.onDownloadComplete();
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

    public void setTracker(IDownloadTracker tracker) {
        this.tracker = tracker;
    }

    public void setMultipleFileTracker(IMultipleFileDownloadTracker tracker) {
        this.multipleFileTracker = tracker;
    }

    public void setFileTracker(IFileDownloadTracker tracker) {
        this.fileTracker = tracker;
    }

    public int getFileSizeInBytes() {
        return fileSize;
    }

    public int getBytesProcessed() {
        return bytesProcessed;
    }

    public void cancelDownload(){
        cancel = true;
    }

    public int getProgress() {
        return (int) Math.ceil(bytesProcessed / (float) fileSize * 100);
    }

    protected void deleteFile(String outputFile){
        File file = new File(outputFile);
        boolean success = file.delete();
        if(!success) System.err.println("Failed to delete file '" + outputFile + "'.");
    }

}
