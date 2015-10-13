package com.softopers.asaedr.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Krunal on 18-08-2015.
 */
public class User implements Serializable {

    private String ZoneName;

    private Integer PrivilegeCategoryId;

    private String Mob;

    private ArrayList<Privilage> Privilage;

    private String EmpId;

    private String Password;

    private String UserName;

    private Boolean IsAdmin;

    private Boolean IsOwner;

    private String IsActive;

    private String Address;

    private String EmpName;

    private String Image;

    private String IsDeleted;

    private String DeviceToken;

    private String DepartmentName;

    private String Designation;

    private String Description;

    private Integer ZoneId;

    private Integer DepartmentId;

    private User user;

    public User(User user) {
        this.user = user;
    }

    public User() {

    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public Integer getPrivilegeCategoryId() {
        return PrivilegeCategoryId;
    }

    public void setPrivilegeCategoryId(Integer PrivilegeCategoryId) {
        this.PrivilegeCategoryId = PrivilegeCategoryId;
    }

    public String getMob() {
        return Mob;
    }

    public void setMob(String Mob) {
        this.Mob = Mob;
    }

    public ArrayList<Privilage> getPrivilage() {
        return Privilage;
    }

    public void setPrivilage(ArrayList<Privilage> Privilage) {
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

    public Boolean getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(Boolean IsAdmin) {
        this.IsAdmin = IsAdmin;
    }

    public Boolean getIsOwner() {
        return IsOwner;
    }

    public void setIsOwner(Boolean IsOwner) {
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getZoneId() {
        return ZoneId;
    }

    public void setZoneId(Integer zoneId) {
        ZoneId = zoneId;
    }

    public Integer getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        DepartmentId = departmentId;
    }

    @Override
    public String toString() {
        return "ClassPojo [ZoneName = " + ZoneName + ", PrivilegeCategoryId = " + PrivilegeCategoryId + ", Mob = " + Mob + ", Privilage = " + Privilage + ", EmpId = " + EmpId + ", Password = " + Password + ", UserName = " + UserName + ", IsAdmin = " + IsAdmin + ", IsOwner = " + IsOwner + ", IsActive = " + IsActive + ", Address = " + Address + ", EmpName = " + EmpName + ", Image = " + Image + ", IsDeleted = " + IsDeleted + ", DeviceToken = " + DeviceToken + ", DepartmentName = " + DepartmentName + ", Designation = " + Designation + "]";
    }
}