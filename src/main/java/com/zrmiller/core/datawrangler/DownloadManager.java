package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2017;
import com.zrmiller.gui.FrameManager;

import java.util.concurrent.Future;

public class DownloadManager {

    private static Thread thread;
//    private static final Executor executor = Executors.newSingleThreadExecutor();
//    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static DataWrangler2017 runDownload2017() {
        DataWrangler2017 wrangler = new DataWrangler2017();
        // FIXME : this should be a made a direct call
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloadComplete() {
//                executor.execute(() -> wrangler.sortAndCompress(true));
//                executor.execute(() -> wrangler.sortAndCompress(true));
            }

            @Override
            public void onFileReadComplete() {

            }

            @Override
            public void onFileSortComplete() {

            }

            @Override
            public void onCompressComplete() {
                FrameManager.mainFrame.validateDatasetMenu();
            }

            @Override
            public void onCancel() {
                FrameManager.mainFrame.validateDatasetMenu();
                // FIXME:
            }
        };
        wrangler.addStatusTracker(tracker);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                wrangler.downloadAndProcessDataset();
            }
        });
        thread.start();
        return wrangler;
    }

    public static DataWrangler2022 runDownload2022() {
        DataWrangler2022 wrangler = new DataWrangler2022();
        thread = new Thread(wrangler::downloadAndProcessFullDataset);
        thread.start();
        return wrangler;
    }

    public static void cancel() {
        if (thread != null)
            thread.interrupt();
        thread = null;
    }

}
