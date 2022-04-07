package com.zrmiller.core;

import com.zrmiller.core.enums.Dataset;

import java.util.ArrayList;

public class DatasetManager {

    private static Dataset dataset = Dataset.PLACE_2017;
    private static final ArrayList<IDatasetListener> datasetListeners = new ArrayList<>();

    public static void changeDataset(Dataset dataset) {
        DatasetManager.dataset = dataset;
        for (IDatasetListener listener : datasetListeners) {
            listener.onDatasetChanged(dataset);
        }
    }

    public static Dataset getDataset() {
        return dataset;
    }

    public static void addListener(IDatasetListener listener) {
        datasetListeners.add(listener);
    }

    public static void removeListener(IDatasetListener listener) {
        datasetListeners.remove(listener);
    }

    public static void removeAllListeners() {
        datasetListeners.clear();
    }

}
