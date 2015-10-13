package com.softopers.asaedr.model;

import java.util.ArrayList;

public class EmployeeRegistrationDetail {

    private ArrayList<PrivilegeCategoryList> PrivilegeCategoryList;

    private ArrayList<DepartmentList> DepartmentList;

    private ArrayList<ZoneList> ZoneList;

    public ArrayList<PrivilegeCategoryList> getPrivilegeCategoryList() {
        return PrivilegeCategoryList;
    }

    public void setPrivilegeCategoryList(ArrayList<PrivilegeCategoryList> PrivilegeCategoryList) {
        this.PrivilegeCategoryList = PrivilegeCategoryList;
    }

    public ArrayList<DepartmentList> getDepartmentList() {
        return DepartmentList;
    }

    public void setDepartmentList(ArrayList<DepartmentList> DepartmentList) {
        this.DepartmentList = DepartmentList;
    }

    public ArrayList<ZoneList> getZoneList() {
        return ZoneList;
    }

    public void setZoneList(ArrayList<ZoneList> ZoneList) {
        this.ZoneList = ZoneList;
    }

    @Override
    public String toString() {
        return "ClassPojo [PrivilegeCategoryList = " + PrivilegeCategoryList + ", DepartmentList = " + DepartmentList + ", ZoneList = " + ZoneList + "]";
    }
}