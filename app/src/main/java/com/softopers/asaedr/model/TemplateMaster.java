package com.softopers.asaedr.model;

import java.io.Serializable;

public class TemplateMaster implements Serializable {

    private String TemplateContent;

    private String TemplateName;

    private String IsPersonal;

    private String EmpId;

    private String TemplateId;

    private TemplateMaster TemplateMaster;

    public TemplateMaster() {

    }

    public TemplateMaster(TemplateMaster templateMaster) {
        TemplateMaster = templateMaster;
    }

    public TemplateMaster(String templateId, String empId) {
        TemplateId = templateId;
        EmpId = empId;
    }

    public TemplateMaster(String templateName, String templateContent, String empId) {
        TemplateName = templateName;
        TemplateContent = templateContent;
        EmpId = empId;
    }

    public String getTemplateContent() {
        return TemplateContent;
    }

    public void setTemplateContent(String TemplateContent) {
        this.TemplateContent = TemplateContent;
    }

    public String getTemplateName() {
        return TemplateName;
    }

    public void setTemplateName(String TemplateName) {
        this.TemplateName = TemplateName;
    }

    public String getIsPersonal() {
        return IsPersonal;
    }

    public void setIsPersonal(String IsPersonal) {
        this.IsPersonal = IsPersonal;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String EmpId) {
        this.EmpId = EmpId;
    }

    public String getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(String TemplateId) {
        this.TemplateId = TemplateId;
    }

    @Override
    public String toString() {
        return "TemplateDetail [TemplateContent = " + TemplateContent + ", TemplateName = " + TemplateName + ", IsPersonal = " + IsPersonal + ", EmpId = " + EmpId + ", TemplateId = " + TemplateId + "]";
    }
}
