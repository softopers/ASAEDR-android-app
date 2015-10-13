package com.softopers.asaedr.model;

public class DateList {
    private String DayStatusId;

    private String Date;

    private Boolean IsLock;

    private Integer UnReadCounter;

    private String IsComment;

    public String getDayStatusId() {
        return DayStatusId;
    }

    public void setDayStatusId(String DayStatusId) {
        this.DayStatusId = DayStatusId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public Boolean getIsLock() {
        return IsLock;
    }

    public void setIsLock(Boolean IsLock) {
        this.IsLock = IsLock;
    }

    public Integer getUnReadCounter() {
        return UnReadCounter;
    }

    public void setUnReadCounter(Integer UnReadCounter) {
        this.UnReadCounter = UnReadCounter;
    }

    public String getIsComment() {
        return IsComment;
    }

    public void setIsComment(String IsComment) {
        this.IsComment = IsComment;
    }

    @Override
    public String toString() {
        return "ClassPojo [DayStatusId = " + DayStatusId + ", Date = " + Date + ", IsLock = " + IsLock + ", UnReadCounter = " + UnReadCounter + ", IsComment = " + IsComment + "]";
    }
}