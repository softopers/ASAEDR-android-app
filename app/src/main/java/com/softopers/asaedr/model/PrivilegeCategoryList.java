package com.softopers.asaedr.model;

public class PrivilegeCategoryList {

    private Integer PrivilegeCategoryId;

    private String PrivilegeCategoryName;

    public Integer getPrivilegeCategoryId() {
        return PrivilegeCategoryId;
    }

    public void setPrivilegeCategoryId(Integer PrivilegeCategoryId) {
        this.PrivilegeCategoryId = PrivilegeCategoryId;
    }

    public String getPrivilegeCategoryName() {
        return PrivilegeCategoryName;
    }

    public void setPrivilegeCategoryName(String PrivilegeCategoryName) {
        this.PrivilegeCategoryName = PrivilegeCategoryName;
    }

    @Override
    public String toString() {
        return "ClassPojo [PrivilegeCategoryId = " + PrivilegeCategoryId + ", PrivilegeCategoryName = " + PrivilegeCategoryName + "]";
    }
}