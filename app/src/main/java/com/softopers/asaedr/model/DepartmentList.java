package com.softopers.asaedr.model;

public class DepartmentList {

    private Integer DepartmentId;

    private String DepartmentName;

    public Integer getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(Integer DepartmentId) {
        this.DepartmentId = DepartmentId;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }

    @Override
    public String toString() {
        return "ClassPojo [DepartmentId = " + DepartmentId + ", DepartmentName = " + DepartmentName + "]";
    }
}