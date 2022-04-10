package com.zrmiller.core.managers.listeners;

import com.zrmiller.core.enums.Dataset;

public interface IDatasetListener {

    void onDatasetChanged(Dataset dataset);

}
