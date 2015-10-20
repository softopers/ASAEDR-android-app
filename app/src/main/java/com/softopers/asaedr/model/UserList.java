package com.softopers.asaedr.model;

import java.io.Serializable;

public class UserList implements Serializable {
    private String ZoneName;

    private String PrivilegeCategoryId;

    private String Mob;

    private String Description;

    private String Privilage;

    private String EmpId;

    private String Password;

    private String UserName;

    private String ZoneId;

    private Integer UnReadCounter;

    private Boolean IsAdmin;

    private String IsOwner;

    private String IsActive;

    private String Address;

    private String EmpName;

    private String Image;

    private String IsDeleted;

    private String DeviceToken;

    private String DepartmentId;

    private String DepartmentName;

    private String Designation;

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public String getPrivilegeCategoryId() {
        return PrivilegeCategoryId;
    }

    public void setPrivilegeCategoryId(String PrivilegeCategoryId) {
        this.PrivilegeCategoryId = PrivilegeCategoryId;
    }

    public String getMob() {
        return Mob;
    }

    public void setMob(String Mob) {
        this.Mob = Mob;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getPrivilage() {
        return Privilage;
    }

    public void setPrivilage(String Privilage) {
        this.Privilage = Privilage;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String EmpId) {
        this.EmpId = EmpId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getZoneId() {
        return ZoneId;
    }

    public void setZoneId(String ZoneId) {
        this.ZoneId = ZoneId;
    }

    public Integer getUnReadCounter() {
        return UnReadCounter;
    }

    public void setUnReadCounter(Integer UnReadCounter) {
        this.UnReadCounter = UnReadCounter;
    }

    public Boolean getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(Boolean IsAdmin) {
        this.IsAdmin = IsAdmin;
    }

    public String getIsOwner() {
        return IsOwner;
    }

    public void setIsOwner(String IsOwner) {
        this.IsOwner = IsOwner;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String IsActive) {
        this.IsActive = IsActive;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String DeviceToken) {
        this.DeviceToken = DeviceToken;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String DepartmentId) {
        this.DepartmentId = DepartmentId;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    @Override
    public String toString() {
        return "ClassPojo [ZoneName = " + ZoneName + ", PrivilegeCategoryId = " + PrivilegeCategoryId + ", Mob = " + Mob + ", Description = " + Description + ", Privilage = " + Privilage + ", EmpId = " + EmpId + ", Password = " + Password + ", UserName = " + UserName + ", ZoneId = " + ZoneId + ", UnReadCounter = " + UnReadCounter + ", IsAdmin = " + IsAdmin + ", IsOwner = " + IsOwner + ", IsActive = " + IsActive + ", Address = " + Address + ", EmpName = " + EmpName + ", Image = " + Image + ", IsDeleted = " + IsDeleted + ", DeviceToken = " + DeviceToken + ", DepartmentId = " + DepartmentId + ", DepartmentName = " + DepartmentName + ", Designation = " + Designation + "]";
    }
}
