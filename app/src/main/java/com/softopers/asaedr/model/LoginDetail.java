package com.softopers.asaedr.model;

/**
 * Created by Krunal on 18-08-2015.
 */
public class LoginDetail {

    private User User;

    private String Status;

    public User getUser() {
        return User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    @Override
    public String toString() {
        return "ClassPojo [User = " + User + ", Status = " + Status + "]";
    }
}
