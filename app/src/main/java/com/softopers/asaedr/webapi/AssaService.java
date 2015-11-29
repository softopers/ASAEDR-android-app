package com.softopers.asaedr.webapi;

import com.softopers.asaedr.model.AdminEmployeeList;
import com.softopers.asaedr.model.ChangePasswordRequset;
import com.softopers.asaedr.model.EmployeeRegistrationDetail;
import com.softopers.asaedr.model.LockUnlock;
import com.softopers.asaedr.model.LoginDetail;
import com.softopers.asaedr.model.MessageListResponse;
import com.softopers.asaedr.model.MessageRequest;
import com.softopers.asaedr.model.Report;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.model.ResponseLockUnlock;
import com.softopers.asaedr.model.ResponseLogout;
import com.softopers.asaedr.model.ResponseMessage;
import com.softopers.asaedr.model.ResponseReportingList;
import com.softopers.asaedr.model.ResponseResult;
import com.softopers.asaedr.model.ResponseSentMessageDetail;
import com.softopers.asaedr.model.TemplateList;
import com.softopers.asaedr.model.TemplateMaster;
import com.softopers.asaedr.model.User;
import com.softopers.asaedr.model.UserDateWiseReport;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface AssaService {

    @POST("/Login")
    LoginDetail loginUser(@Body User user);

    @GET("/GetDepartmentZoneList")
    EmployeeRegistrationDetail getEmployeeRegistrationDetails();

    @POST("/AddRegistration")
    ResponseResult employeeRegistration(@Body User user);

    @POST("/UserDateWiseReportsByEmpId")
    UserDateWiseReport userDateWiseReport(@Body RequestByIds requestByIds);

    @POST("/DetailReportsByDailyStatus")
    ResponseReportingList reportingList(@Body RequestByIds requestByIds);

    @POST("/InsertReports")
    ResponseResult insertReport(@Body Report report);

    @POST("/TemplateListByEmpId")
    TemplateList templateList(@Body RequestByIds requestByIds);

    @POST("/DeleteTemplate")
    ResponseResult deleteTemplate(@Body TemplateMaster templateMaster);

    @POST("/AdminEmployeeListById")
    AdminEmployeeList adminEmployeeListTab1(@Body RequestByIds requestByIds);

    @POST("/AdminEmployeeDateWiseReport")
    UserDateWiseReport adminEmployeeDateWiseReport(@Body RequestByIds requestByIds);

    @POST("/DetailReportsByEmpIdDate")
    ResponseReportingList detailReportsByEmpIdDate(@Body RequestByIds requestByIds);

    @POST("/InsertTemplate")
    ResponseResult insertTemplate(@Body TemplateMaster templateMaster);

    @POST("/AdminDateWiseReportsByEmpId")
    UserDateWiseReport adminDateWiseReportsByAdminId(@Body RequestByIds requestByIds);

    @POST("/EmployeeReportsByDate")
    AdminEmployeeList employeeReportsByDate(@Body RequestByIds requestByIds);

    @POST("/InsertComment")
    ResponseResult insertComment(@Body RequestByIds requestByIds);

    @POST("/AdminEmployeeDataByAdminId")
    AdminEmployeeList adminEmployeeDataByAdminId(@Body RequestByIds requestByIds);

    @POST("/ChangeUserPassword")
    ResponseResult changeUserPassword(@Body ChangePasswordRequset changePasswordRequset);

    @POST("/UpdateRegistration")
    ResponseResult updateRegistration(@Body User user);

    @POST("/SendMessage")
    ResponseMessage sendMessage(@Body MessageRequest messageRequest);

    @POST("/MessageListByEmpId")
    MessageListResponse messageListByEmpId(@Body RequestByIds requestByIds);

    @POST("/SentMessageDetail")
    ResponseSentMessageDetail sentMessageDetail(@Body RequestByIds requestByIds);

    @POST("/AdminEmployeeDataByAdminIdForMessage")
    AdminEmployeeList adminEmployeeDataByAdminIdForMessage(@Body RequestByIds requestByIds);

    @POST("/Logout")
    ResponseLogout logout(@Body RequestByIds requestByIds);

    @POST("/AdminUnLockDaysList")
    UserDateWiseReport adminUnLockDaysList(@Body RequestByIds requestByIds);

    @POST("/AdminLockDaysList")
    UserDateWiseReport adminLockDaysList(@Body RequestByIds requestByIds);

    @POST("/AdminUnLockDaysEmployeeList")
    AdminEmployeeList adminUnLockDaysEmployeeList(@Body RequestByIds requestByIds);

    @POST("/AdminLockDaysEmployeeList")
    AdminEmployeeList adminLockDaysEmployeeList(@Body RequestByIds requestByIds);

    @POST("/AdminUnLockDay")
    ResponseLockUnlock adminUnLockDay(@Body LockUnlock lockUnlock);

    @POST("/AdminLockDay")
    ResponseLockUnlock adminLockDay(@Body LockUnlock lockUnlock);
}
