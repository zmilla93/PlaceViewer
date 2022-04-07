package com.zrmiller.core;

import com.zrmiller.core.enums.Dataset;

public interface IDatasetListener {

    void onDatasetChanged(Dataset dataset);

}
