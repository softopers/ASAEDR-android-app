package com.softopers.asaedr.model;

import java.io.Serializable;

public class EmpMessageList implements Serializable {
    private String EmpId;

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String EmpId) {
        this.EmpId = EmpId;
    }

    @Override
    public String toString() {
        return "ClassPojo [EmpId = " + EmpId + "]";
    }
}
