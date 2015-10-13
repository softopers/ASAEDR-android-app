package com.softopers.asaedr.model;

public class TemplateDetail {
    private String TemplateContent;

    private String TemplateName;

    private Boolean IsPersonal;

    private String EmpId;

    private String TemplateId;

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

    public Boolean getIsPersonal() {
        return IsPersonal;
    }

    public void setIsPersonal(Boolean IsPersonal) {
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
