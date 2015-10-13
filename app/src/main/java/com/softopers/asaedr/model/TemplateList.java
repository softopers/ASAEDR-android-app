package com.softopers.asaedr.model;

import java.util.ArrayList;

public class TemplateList {
    private String Status;

    private ArrayList<TemplateDetail> TemplateDetail;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public ArrayList<TemplateDetail> getTemplateDetail() {
        return TemplateDetail;
    }

    public void setTemplateDetail(ArrayList<TemplateDetail> TemplateDetail) {
        this.TemplateDetail = TemplateDetail;
    }

    @Override
    public String toString() {
        return "ClassPojo [Status = " + Status + ", TemplateDetail = " + TemplateDetail + "]";
    }
}