package com.softopers.asaedr.model;

import java.io.Serializable;

public class ChangePasswordRequset implements Serializable
{
    private String NewPassword;

    private String EmpId;

    private String CurrentPassword;

    public String getNewPassword ()
    {
        return NewPassword;
    }

    public void setNewPassword (String NewPassword)
    {
        this.NewPassword = NewPassword;
    }

    public String getEmpId ()
    {
        return EmpId;
    }

    public void setEmpId (String EmpId)
    {
        this.EmpId = EmpId;
    }

    public String getCurrentPassword ()
    {
        return CurrentPassword;
    }

    public void setCurrentPassword (String CurrentPassword)
    {
        this.CurrentPassword = CurrentPassword;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [NewPassword = "+NewPassword+", EmpId = "+EmpId+", CurrentPassword = "+CurrentPassword+"]";
    }
}