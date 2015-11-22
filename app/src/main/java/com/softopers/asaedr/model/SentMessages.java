package com.softopers.asaedr.model;

import java.util.ArrayList;

public class SentMessages {
    ArrayList<SentMessages> sentMessages;
    private String SentDateTime;
    private String MessageId;
    private String IsSent;
    private String ReadDateTime;
    private String EmpId;
    private String EmpName;
    private String IsRead;

    public ArrayList<SentMessages> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(ArrayList<SentMessages> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public String getSentDateTime() {
        return SentDateTime;
    }

    public void setSentDateTime(String SentDateTime) {
        this.SentDateTime = SentDateTime;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String MessageId) {
        this.MessageId = MessageId;
    }

    public String getIsSent() {
        return IsSent;
    }

    public void setIsSent(String IsSent) {
        this.IsSent = IsSent;
    }

    public String getReadDateTime() {
        return ReadDateTime;
    }

    public void setReadDateTime(String ReadDateTime) {
        this.ReadDateTime = ReadDateTime;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String EmpId) {
        this.EmpId = EmpId;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String IsRead) {
        this.IsRead = IsRead;
    }

    @Override
    public String toString() {
        return "SentMessages{" +
                "SentDateTime='" + SentDateTime + '\'' +
                ", MessageId='" + MessageId + '\'' +
                ", IsSent='" + IsSent + '\'' +
                ", ReadDateTime='" + ReadDateTime + '\'' +
                ", EmpId='" + EmpId + '\'' +
                ", EmpName='" + EmpName + '\'' +
                ", IsRead='" + IsRead + '\'' +
                ", sentMessages=" + sentMessages +
                '}';
    }
}