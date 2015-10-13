package com.softopers.asaedr.model;

public class ZoneList {

    private String ZoneName;

    private Integer ZoneId;

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public Integer getZoneId() {
        return ZoneId;
    }

    public void setZoneId(Integer ZoneId) {
        this.ZoneId = ZoneId;
    }

    @Override
    public String toString() {
        return "ClassPojo [ZoneName = " + ZoneName + ", ZoneId = " + ZoneId + "]";
    }
}