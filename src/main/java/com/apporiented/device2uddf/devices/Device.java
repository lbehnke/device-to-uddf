package com.apporiented.device2uddf.devices;

import com.apporiented.device2uddf.SerialConnection;
import com.apporiented.device2uddf.model.DiveData;
import com.apporiented.device2uddf.model.GlobalData;
import com.apporiented.device2uddf.model.Progress;

import java.util.List;
import java.util.function.Consumer;

public interface Device {

    void init (SerialConnection connection);

    GlobalData retrieveGlobalData();

    List<DiveData> retrieveDiveData(Consumer<Progress> progressCallback);

}
