package com.apporiented.device2uddf.model;

/**
 * Created by lars on 20.11.15.
 */
public class Progress {

    private String currentStatus;

    private Integer percent;

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
