package com.softopers.asaedr.model;

import java.util.ArrayList;

public class ResponseSentMessageDetail {
    private String Status;

    private String Result;

    private String Version;

    private ArrayList<SentMessages> SentMessages;

    private String MessageMaster;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public ArrayList<SentMessages> getSentMessages() {
        return SentMessages;
    }

    public void setSentMessages(ArrayList<SentMessages> SentMessages) {
        this.SentMessages = SentMessages;
    }

    public String getMessageMaster() {
        return MessageMaster;
    }

    public void setMessageMaster(String MessageMaster) {
        this.MessageMaster = MessageMaster;
    }

    @Override
    public String toString() {
        return "ResponseSentMessageDetail{" +
                "Status=" + Status +
                ", Result='" + Result + '\'' +
                ", Version='" + Version + '\'' +
                ", SentMessages=" + SentMessages +
                ", MessageMaster='" + MessageMaster + '\'' +
                '}';
    }
}