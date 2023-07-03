package com.zrmiller.core.managers;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.listeners.IColorModeListener;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.utility.ColorMode;
import com.zrmiller.modules.listening.ListenManager;

import java.util.ArrayList;

/**
 * Controls which dataset is loaded. Notifies all listeners when dataset is changed.
 */
// FIXME : Should probably just convert this to a static class
public class DatasetManager extends ListenManager<IDatasetListener> {

    private Dataset dataset = null;
    private ColorMode colorMode = ColorMode.NORMAL;
    private final ArrayList<IColorModeListener> colorModeListeners = new ArrayList<>();

    public void changeDataset(Dataset dataset) {
        this.dataset = dataset;
        for (IDatasetListener listener : listeners) {
            listener.onDatasetChanged(dataset);
        }
    }

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
        for (IColorModeListener listener : colorModeListeners) {
            listener.onColorModeChange(colorMode);
        }
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    public Dataset currentDataset() {
        return dataset;
    }

    public void addColorModeListener(IColorModeListener listener) {
        colorModeListeners.add(listener);
    }

}
