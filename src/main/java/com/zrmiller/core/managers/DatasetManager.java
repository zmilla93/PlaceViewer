package com.zrmiller.core.managers;

import com.zrmiller.core.colors.ColorMode;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.listeners.IColorModeListener;
import com.zrmiller.core.managers.listeners.IDatasetListener;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Controls which dataset is loaded. Notifies all listeners when dataset is changed.
 */
public final class DatasetManager {

    private static Dataset dataset = null;
    private static ColorMode colorMode = ColorMode.NORMAL;
    private static final ArrayList<IColorModeListener> colorModeListeners = new ArrayList<>();
    private static final ArrayList<IDatasetListener> datasetListeners = new ArrayList<>();

    private DatasetManager() {

    }

    public static void setDataset(Dataset dataset) {
        DatasetManager.dataset = dataset;
        if (SwingUtilities.isEventDispatchThread()) alertDatasetListeners(dataset);
        else SwingUtilities.invokeLater(() -> alertDatasetListeners(dataset));
    }

    private static void alertDatasetListeners(Dataset dataset) {
        for (IDatasetListener listener : datasetListeners)
            listener.onDatasetChanged(dataset);
    }

    public static void setColorMode(ColorMode colorMode) {
        DatasetManager.colorMode = colorMode;
        for (IColorModeListener listener : colorModeListeners) {
            listener.onColorModeChange(colorMode);
        }
    }

    public static ColorMode getColorMode() {
        return colorMode;
    }

    public static Dataset getDataset() {
        return dataset;
    }

    public static void addColorModeListener(IColorModeListener listener) {
        colorModeListeners.add(listener);
    }

    public static void addDatasetListener(IDatasetListener listener) {
        datasetListeners.add(listener);
    }

}
