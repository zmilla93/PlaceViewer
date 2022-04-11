package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2017;
import com.zrmiller.gui.FrameManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadManager {

    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static DataWrangler2017 runDownload2017() {
        DataWrangler2017 wrangler = new DataWrangler2017();
        // FIXME : this should be a made a direct call
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloadComplete() {
                executor.execute(() -> wrangler.sortAndCompress(true));
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
        };
        wrangler.addStatusTracker(tracker);
        executor.execute(wrangler::downloadDataset);
        return wrangler;
    }

    public static DataWrangler2022 runDownload2022() {
        DataWrangler2022 wrangler = new DataWrangler2022();
        executor.execute(wrangler::downloadAndProcessFullDataset);
        return wrangler;
    }

}
