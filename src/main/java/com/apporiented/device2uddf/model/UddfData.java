package com.apporiented.device2uddf.model;

import java.util.List;

/**
 * Created by lars on 20.11.15.
 */
public class UddfData {

    private GlobalData globalData;

    private List<DiveData> diveDataList;

    public GlobalData getGlobalData() {
        return globalData;
    }

    public void setGlobalData(GlobalData globalData) {
        this.globalData = globalData;
    }

    public List<DiveData> getDiveDataList() {
        return diveDataList;
    }

    public void setDiveDataList(List<DiveData> diveDataList) {
        this.diveDataList = diveDataList;
    }
}
