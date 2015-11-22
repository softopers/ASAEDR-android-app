package com.softopers.asaedr.model;

public class MessageMaster {
    private String SendType;

    private String CreateDateTime;

    private String AdminName;

    private String AdminId;

    private String MessageId;

    private boolean IsSent;

    private String EmpMessageList;

    private String MessageContent;

    private String IsDeleted;

    public String getSendType() {
        return SendType;
    }

    public void setSendType(String SendType) {
        this.SendType = SendType;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String CreateDateTime) {
        this.CreateDateTime = CreateDateTime;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String AdminName) {
        this.AdminName = AdminName;
    }

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String AdminId) {
        this.AdminId = AdminId;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String MessageId) {
        this.MessageId = MessageId;
    }

    public boolean getIsSent() {
        return IsSent;
    }

    public void setIsSent(boolean IsSent) {
        this.IsSent = IsSent;
    }

    public String getEmpMessageList() {
        return EmpMessageList;
    }

    public void setEmpMessageList(String EmpMessageList) {
        this.EmpMessageList = EmpMessageList;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String MessageContent) {
        this.MessageContent = MessageContent;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    @Override
    public String toString() {
        return "ClassPojo [SendType = " + SendType + ", CreateDateTime = " + CreateDateTime + ", AdminName = " + AdminName + ", AdminId = " + AdminId + ", MessageId = " + MessageId + ", IsSent = " + IsSent + ", EmpMessageList = " + EmpMessageList + ", MessageContent = " + MessageContent + ", IsDeleted = " + IsDeleted + "]";
    }
}