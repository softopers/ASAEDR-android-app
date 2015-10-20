package com.softopers.asaedr.model;

public class ReportList {
    private String AdminName;

    private String Description;

    private Boolean IsRead;

    private Boolean IsComment;

    private String postDateTime;

    public String getAdminName ()
    {
        return AdminName;
    }

    public void setAdminName (String AdminName)
    {
        this.AdminName = AdminName;
    }

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public Boolean getIsRead ()
    {
        return IsRead;
    }

    public void setIsRead (Boolean IsRead)
    {
        this.IsRead = IsRead;
    }

    public Boolean getIsComment ()
    {
        return IsComment;
    }

    public void setIsComment (Boolean IsComment)
    {
        this.IsComment = IsComment;
    }

    public String getPostDateTime ()
    {
        return postDateTime;
    }

    public void setPostDateTime (String postDateTime)
    {
        this.postDateTime = postDateTime;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [AdminName = "+AdminName+", Description = "+Description+", IsRead = "+IsRead+", IsComment = "+IsComment+", postDateTime = "+postDateTime+"]";
    }
}
