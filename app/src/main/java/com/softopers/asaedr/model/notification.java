package com.softopers.asaedr.model;

public class notification {

    private String AdminName;

    private String Date;

    private String NotificationType;

    private String DayStatusId;

    private String MessageContent;

    public String getDayStatusId() {
        return DayStatusId;
    }

    public void setDayStatusId(String dayStatusId) {
        DayStatusId = dayStatusId;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String AdminName) {
        this.AdminName = AdminName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String NotificationType) {
        this.NotificationType = NotificationType;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    @Override
    public String toString() {
        return "notification{" +
                "AdminName='" + AdminName + '\'' +
                ", Date='" + Date + '\'' +
                ", NotificationType='" + NotificationType + '\'' +
                ", DayStatusId='" + DayStatusId + '\'' +
                ", MessageContent='" + MessageContent + '\'' +
                '}';
    }
}