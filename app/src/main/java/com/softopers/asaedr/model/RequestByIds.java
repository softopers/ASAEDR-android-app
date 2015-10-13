package com.softopers.asaedr.model;

import java.io.Serializable;

/**
 * Created by Krunal on 24-08-2015.
 */
public class RequestByIds implements Serializable {

    private String EmpId;

    private String AdminId;

    private Integer PageNumber;

    private Integer DayStatusId;

    private String Date;

    private String Comment;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
    }

    public Integer getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        PageNumber = pageNumber;
    }

    public Integer getDayStatusId() {
        return DayStatusId;
    }

    public void setDayStatusId(Integer dayStatusId) {
        DayStatusId = dayStatusId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
