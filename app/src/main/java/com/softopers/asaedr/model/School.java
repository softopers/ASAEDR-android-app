package com.softopers.asaedr.model;

public class School
{
    private String SchoolName;

    private int SchoolId;

    public String getSchoolName ()
    {
        return SchoolName;
    }

    public void setSchoolName (String SchoolName)
    {
        this.SchoolName = SchoolName;
    }

    public int getSchoolId ()
    {
        return SchoolId;
    }

    public void setSchoolId (int SchoolId)
    {
        this.SchoolId = SchoolId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [SchoolName = "+SchoolName+", SchoolId = "+SchoolId+"]";
    }
}
