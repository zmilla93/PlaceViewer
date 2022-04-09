package com.zrmiller.core.datawrangler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadManager {

    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static DataWrangler2017 downloadAndMinify2017() {
        DataWrangler2017 wrangler = new DataWrangler2017();
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloaded() {
                System.out.println("here we go...");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wrangler.sortAndMinify(false);
                    }
                });
                thread.start();
//                executor.execute(() -> wrangler.sortAndMinify(false));
            }

            @Override
            public void onFileRead() {

            }

            @Override
            public void onFileSorted() {

            }

            @Override
            public void onMinifiyComplete() {

            }
        };
        wrangler.addStatusTracker(tracker);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                wrangler.downloadFile();
            }
        });
        thread.start();

//        executor.execute(wrangler::downloadFile);
//        executor.execute(() -> wrangler.sortAndMinify(false));
        return wrangler;
    }

    public static void downloadAndMinify2022() {

    }

}
