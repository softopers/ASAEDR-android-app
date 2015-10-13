package com.softopers.asaedr.model;

import java.util.ArrayList;

public class AdminEmployeeList {
    private String Status;

    private ArrayList<UserList> UserList;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public ArrayList<UserList> getUserList() {
        return UserList;
    }

    public void setUserList(ArrayList<UserList> UserList) {
        this.UserList = UserList;
    }

    @Override
    public String toString() {
        return "ClassPojo [Status = " + Status + ", UserList = " + UserList + "]";
    }
}
