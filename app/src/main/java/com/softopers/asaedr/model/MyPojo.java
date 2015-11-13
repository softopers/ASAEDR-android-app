package com.softopers.asaedr.model;

public class MyPojo {
    private String Status;

    private String Result;

    private String Version;

    private String SentMessages;

    private String MessageMaster;

    @Override
    public String toString() {
        return "MyPojo{" +
                "Status='" + Status + '\'' +
                ", Result='" + Result + '\'' +
                ", Version='" + Version + '\'' +
                ", SentMessages='" + SentMessages + '\'' +
                ", MessageMaster='" + MessageMaster + '\'' +
                '}';
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getSentMessages() {
        return SentMessages;
    }

    public void setSentMessages(String sentMessages) {
        SentMessages = sentMessages;
    }

    public String getMessageMaster() {
        return MessageMaster;
    }

    public void setMessageMaster(String messageMaster) {
        MessageMaster = messageMaster;
    }
}