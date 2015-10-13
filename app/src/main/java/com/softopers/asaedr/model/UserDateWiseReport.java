package com.softopers.asaedr.model;

import java.util.ArrayList;

public class UserDateWiseReport {
    private String Status;

    private ArrayList<DateList> DateList;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public ArrayList<DateList> getDateList() {
        return DateList;
    }

    public void setDateList(ArrayList<DateList> DateList) {
        this.DateList = DateList;
    }

    @Override
    public String toString() {
        return "ClassPojo [Status = " + Status + ", DateList = " + DateList + "]";
    }
}