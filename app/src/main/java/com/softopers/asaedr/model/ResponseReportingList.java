package com.softopers.asaedr.model;

import java.util.ArrayList;

public class ResponseReportingList {
    private String Status;

    private ArrayList<ReportList> ReportList;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public ArrayList<ReportList> getReportList() {
        return ReportList;
    }

    public void setReportList(ArrayList<ReportList> ReportList) {
        this.ReportList = ReportList;
    }

    @Override
    public String toString() {
        return "ResponseReportingList [Status = " + Status + ", ReportList = " + ReportList + "]";
    }
}