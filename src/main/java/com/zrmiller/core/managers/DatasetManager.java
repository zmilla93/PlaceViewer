package com.zrmiller.core.managers;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.modules.listening.ListenManager;

/**
 * Controls which dataset is loaded. Notifies all listeners when dataset is changed.
 */
public class DatasetManager extends ListenManager<IDatasetListener> {

    private Dataset dataset = Dataset.PLACE_2017;

    public void changeDataset(Dataset dataset) {
        this.dataset = dataset;
        for (IDatasetListener listener : listeners) {
            listener.onDatasetChanged(dataset);
        }
    }

    public Dataset currentDataset() {
        return dataset;
    }

}
