package com.softopers.asaedr.model;

import java.util.ArrayList;

public class MessageListResponse {
    private String Status;

    private String Result;

    private String Version;

    private String SentMessages;

    private ArrayList<MessageMaster> MessageMaster;

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

    public String MyPojo() {
        return SentMessages;
    }

    public void setSentMessages(String SentMessages) {
        this.SentMessages = SentMessages;
    }

    public ArrayList<MessageMaster> getMessageMaster() {
        return MessageMaster;
    }

    public void setMessageMaster(ArrayList<MessageMaster> MessageMaster) {
        this.MessageMaster = MessageMaster;
    }

    @Override
    public String toString() {
        return "ClassPojo [Status = " + Status + ", Result = " + Result + ", Version = " + Version + ", SentMessages = " + SentMessages + ", MessageMaster = " + MessageMaster + "]";
    }
}