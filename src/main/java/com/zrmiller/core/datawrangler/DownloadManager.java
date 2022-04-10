package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2017;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadManager {

    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static DataWrangler2017 downloadAndMinify2017() {
        DataWrangler2017 wrangler = new DataWrangler2017();
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloadComplete() {
                executor.execute(() -> wrangler.sortAndMinify(true));
            }

            @Override
            public void onFileReadComplete() {

            }

            @Override
            public void onFileSortComplete() {

            }

            @Override
            public void onCompressComplete() {

            }
        };
        wrangler.addStatusTracker(tracker);
        executor.execute(wrangler::downloadDataset);
        return wrangler;
    }

    public static void runDownload2022(){

    }

}
