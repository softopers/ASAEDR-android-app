package com.softopers.asaedr.model;

public class ReportList {
    private String Description;

    private Boolean IsComment;

    private String postDateTime;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public Boolean getIsComment() {
        return IsComment;
    }

    public void setIsComment(Boolean IsComment) {
        this.IsComment = IsComment;
    }

    public String getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(String postDateTime) {
        this.postDateTime = postDateTime;
    }

    @Override
    public String toString() {
        return "ClassPojo [Description = " + Description + ", IsComment = " + IsComment + ", postDateTime = " + postDateTime + "]";
    }
}