package com.softopers.asaedr.model;

public class MessageRequest {
    private SendMessage SendMessage;

    public SendMessage getSendMessage() {
        return SendMessage;
    }

    public void setSendMessage(SendMessage SendMessage) {
        this.SendMessage = SendMessage;
    }

    @Override
    public String toString() {
        return "ClassPojo [SendMessage = " + SendMessage + "]";
    }
}

	