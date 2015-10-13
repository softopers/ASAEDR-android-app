package com.softopers.asaedr.model;

import java.io.Serializable;

public class Report implements Serializable {

    private Integer DayStatusId;

    private String Content;

    private Report Report;

    public Report(Report Report) {
        this.Report = Report;
    }

    public Report() {

    }

    public Integer getDayStatusId() {
        return DayStatusId;
    }

    public void setDayStatusId(Integer DayStatusId) {
        this.DayStatusId = DayStatusId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    @Override
    public String toString() {
        return "Report [DayStatusId = " + DayStatusId + ", Content = " + Content + "]";
    }
}