package com.softopers.asaedr.model;

import java.io.Serializable;

public class Report implements Serializable {

    private Integer DayStatusId;

    private Integer SchoolId;

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

    public Integer getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(Integer SchoolId) {
        this.SchoolId = SchoolId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    @Override
    public String toString() {
        return "ClassPojo [DayStatusId = " + DayStatusId + ", SchoolId = " + SchoolId + ", Content = " + Content + "]";
    }
}
