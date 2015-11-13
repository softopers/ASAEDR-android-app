package com.softopers.asaedr.model;

import java.util.ArrayList;

public class SendMessage
{
    private int SendType;

    private String AdminId;

    private ArrayList<EmpMessageList> EmpMessageList;

    private String MessageContent;

    public int getSendType ()
    {
        return SendType;
    }

    public void setSendType (int SendType)
    {
        this.SendType = SendType;
    }

    public String getAdminId ()
    {
        return AdminId;
    }

    public void setAdminId (String AdminId)
    {
        this.AdminId = AdminId;
    }

    public ArrayList<EmpMessageList> getEmpMessageList ()
    {
        return EmpMessageList;
    }

    public void setEmpMessageList (ArrayList<EmpMessageList> EmpMessageList)
    {
        this.EmpMessageList = EmpMessageList;
    }

    public String getMessageContent ()
    {
        return MessageContent;
    }

    public void setMessageContent (String MessageContent)
    {
        this.MessageContent = MessageContent;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "SendType='" + SendType + '\'' +
                ", AdminId='" + AdminId + '\'' +
                ", EmpMessageList=" + EmpMessageList +
                ", MessageContent='" + MessageContent + '\'' +
                '}';
    }
}
