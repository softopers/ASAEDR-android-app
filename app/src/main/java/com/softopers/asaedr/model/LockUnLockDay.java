package com.softopers.asaedr.model;

import java.io.Serializable;
import java.util.ArrayList;

public class LockUnLockDay  implements Serializable {
    private ArrayList<EmpIDList> EmpIDList;

    private String AdminId;

    private String Date;

    public ArrayList<EmpIDList> getEmpIDList() {
        return EmpIDList;
    }

    public void setEmpIDList(ArrayList<EmpIDList> EmpIDList) {
        this.EmpIDList = EmpIDList;
    }

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String AdminId) {
        this.AdminId = AdminId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    @Override
    public String toString() {
        return "LockUnLockDay{" +
                "EmpIDList=" + EmpIDList +
                ", AdminId='" + AdminId + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}