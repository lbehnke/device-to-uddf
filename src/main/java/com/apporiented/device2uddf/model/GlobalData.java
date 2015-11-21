package com.apporiented.device2uddf.model;

/**
 * Created by lars on 20.11.15.
 */
public class GlobalData {

    private String serialNumber;
    private String ownerName;
    private Integer timeAlarm;
    private Double depthAlarm;
    private Integer maxFreeDiveTime;
    private Double maxFreeDiveDepth;


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getTimeAlarm() {
        return timeAlarm;
    }

    public void setTimeAlarm(Integer timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public Double getDepthAlarm() {
        return depthAlarm;
    }

    public void setDepthAlarm(Double depthAlarm) {
        this.depthAlarm = depthAlarm;
    }

    public Integer getMaxFreeDiveTime() {
        return maxFreeDiveTime;
    }

    public void setMaxFreeDiveTime(Integer maxFreeDiveTime) {
        this.maxFreeDiveTime = maxFreeDiveTime;
    }

    public Double getMaxFreeDiveDepth() {
        return maxFreeDiveDepth;
    }

    public void setMaxFreeDiveDepth(Double maxFreeDiveDepth) {
        this.maxFreeDiveDepth = maxFreeDiveDepth;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Serial number     : " + getSerialNumber() + "\n");
        sb.append("Owner             : " + getOwnerName() + "\n");
        sb.append("Max time (free)   : " + getMaxFreeDiveTime() + "\n");
        sb.append("Max depth (free)  : " + getMaxFreeDiveDepth() + "\n");
        sb.append("Depth alarm       : " + getDepthAlarm() + "\n");
        sb.append("Time  alarm       : " + getTimeAlarm() + "\n");
        return sb.toString();
    }
}
