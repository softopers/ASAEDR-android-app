package com.softopers.asaedr.webapi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.softopers.asaedr.R;
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
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.util.PrefUtils;

import java.net.UnknownServiceException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class RestAPIClientService extends WakefulIntentService {

    private static final String LOGTAG = "RestAPIClientService";

    private static final String GCM_TOKEN = "678586725717";

    private RestAdapter restAdapter = null;
    private AssaService service;

    public RestAPIClientService() {
        super(LOGTAG);
    }


    @Override
    protected void doWakefulWork(Intent intent) {
        Operation op = (Operation) intent.getSerializableExtra(Operation.class.getName());
        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(getResources().getString(R.string.base_url))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestInterceptor.RequestFacade request) {
                            try {
                                request.addHeader("version-name", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .build();
        }

        if (service == null) {
            service = restAdapter.create(AssaService.class);
        }

        switch (op) {
            case LOGIN_USER:
                try {
                    loginUser(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EMPLOYEE_REGISTRATION_DETAILS:
                try {
                    getEmployeeRegistrationDetails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EMPLOYEE_REGISRATION:
                try {
                    employeeRegistration(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EMPLOYEE_DATE_WISE_REPORTS_BY_EMAIL_ID:
                try {
                    userDateWiseReport(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EMPLOYEE_REPORTING_LIST:
                try {
                    reportingList(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case INSERT_REPORT:
                try {
                    insertReport(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TEMPLATE_LIST:
                try {
                    templateList(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case DELETE_TEMPLATE:
                try {
                    deleteTemplate(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ADMIN_EMPLOYEE_TAB1:
                try {
                    adminEmployeeListTab1(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ADMIN_EMPLOYEE_DATEWISE_REPORT:
                try {
                    adminEmployeeDateWiseReport(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case DETAIL_REPORTS_BY_EMPID_DATE:
                try {
                    detailReportsByEmpIdDate(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case INSERT_TEMPLATE:
                try {
                    insertTemplate(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ADMIN_DATEWISE_REPORT_BY_ADMIN_ID:
                try {
                    adminDateWiseReportsByAdminId(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EMPLOYEE_REPORTS_BY_DATE:
                try {
                    employeeReportsByDate(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case INSERT_COMMENT:
                try {
                    insertComment(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ADMIN_EMPLOYEE_DATA_BY_ADMIN_ID:
                try {
                    adminEmployeeDataByAdminId(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CHANGE_USER_PASSWORD:
                try {
                    changeUserPassword(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case UPDATE_REGISRATION:
                try {
                    updateRegistration(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MESSAGE_REQUEST:
                try {
                    sendMessage(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MESSAGE_LIST_BY_EMPID:
                try {
                    messageListByEmpId(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SENT_MESSAGE_DETAIL:
                try {
                    sentMessageDetail(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_BY_EMP_EMAIL_ID_ADMIN:
                try {
                    adminEmployeeDataByAdminIdForMessage(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LOGOUT:
                try {
                    logout(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case DAYS_UNLOCK:
                try {
                    adminUnLockDaysList(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case DAYS_LOCK:
                try {
                    adminLockDaysList(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case DAYS_UNLOCK_EMPLOYEE:
                try {
                    adminUnLockDaysEmployeeList(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case DAYS_LOCK_EMPLOYEE:
                try {
                    adminLockDaysEmployeeList(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case UNLOCK_DAY:
                try {
                    adminUnLockDay(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LOCK_DAY:
                try {
                    adminLockDay(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }

    private void loginUser(Intent intent) throws UnknownServiceException {
        try {
            User user = (User) intent.getSerializableExtra(App.USER);
            String token = PrefUtils.getDeviceToken(RestAPIClientService.this);
            if (token.equals("")) {
                String msg;
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(RestAPIClientService.this);
                InstanceID instanceID = InstanceID.getInstance(RestAPIClientService.this);
                token = instanceID.getToken(GCM_TOKEN, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                msg = "Device registered, registration id=" + token;
                PrefUtils.setDeviceToken(RestAPIClientService.this, token);
                Log.d("Tag", msg);
            }
            user.setDeviceToken(token);
            Log.v(LOGTAG, new Gson().toJson(user));
            LoginDetail loginDetail = service.loginUser(user);
            App.eventBus.post(loginDetail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void employeeRegistration(Intent intent) throws UnknownServiceException {
        try {
            User user = (User) intent.getSerializableExtra(App.USER);
            ResponseResult responseResult = service.employeeRegistration(user);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    public void getEmployeeRegistrationDetails() throws UnknownServiceException {
        try {
            EmployeeRegistrationDetail employeeRegistrationDetail = service.getEmployeeRegistrationDetails();
            App.eventBus.post(employeeRegistrationDetail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void userDateWiseReport(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.REQUEST_BY_EMP_EMAIL_ID);
            UserDateWiseReport userDateWiseReport = service.userDateWiseReport(requestByIds);
            App.eventBus.post(userDateWiseReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void reportingList(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.EMPLOYEE_REPORTING_LIST);
            ResponseReportingList responseReportingList = service.reportingList(requestByIds);
            App.eventBus.post(responseReportingList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void insertReport(Intent intent) throws UnknownServiceException {
        try {
            Report report = (Report) intent.getSerializableExtra(App.INSERT_REPORT);
            ResponseResult responseResult = service.insertReport(report);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void templateList(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.TEMPLATE_LIST);
            TemplateList templateList = service.templateList(requestByIds);
            App.eventBus.post(templateList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void deleteTemplate(Intent intent) throws UnknownServiceException {
        try {
            TemplateMaster templateMaster = (TemplateMaster) intent.getSerializableExtra(App.DELETE_TEMPLATE);
            ResponseResult responseResult = service.deleteTemplate(templateMaster);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminEmployeeListTab1(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.ADMIN_EMPLOYEE_TAB1);
            AdminEmployeeList adminEmployeeList = service.adminEmployeeListTab1(requestByIds);
            App.eventBus.post(adminEmployeeList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminEmployeeDateWiseReport(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.ADMIN_EMPLOYEE_DATEWISE_REPORT);
            UserDateWiseReport userDateWiseReport = service.adminEmployeeDateWiseReport(requestByIds);
            App.eventBus.post(userDateWiseReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void detailReportsByEmpIdDate(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.DETAIL_REPORTS_BY_EMPID_DATE);
            ResponseReportingList responseReportingList = service.detailReportsByEmpIdDate(requestByIds);
            App.eventBus.post(responseReportingList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }


    private void insertTemplate(Intent intent) throws UnknownServiceException {
        try {
            TemplateMaster templateMaster = (TemplateMaster) intent.getSerializableExtra(App.INSERT_TEMPLATE);
            ResponseResult responseResult = service.insertTemplate(templateMaster);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminDateWiseReportsByAdminId(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.ADMIN_DATEWISE_REPORT_BY_ADMIN_ID);
            UserDateWiseReport userDateWiseReport = service.adminDateWiseReportsByAdminId(requestByIds);
            App.eventBus.post(userDateWiseReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void employeeReportsByDate(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.EMPLOYEE_REPORTS_BY_DATE);
            AdminEmployeeList adminEmployeeList = service.employeeReportsByDate(requestByIds);
            App.eventBus.post(adminEmployeeList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void insertComment(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.INSERT_COMMENT);
            ResponseResult responseResult = service.insertComment(requestByIds);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminEmployeeDataByAdminId (Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.ADMIN_EMPLOYEE_DATA_BY_ADMIN_ID);
            AdminEmployeeList adminEmployeeList = service.adminEmployeeDataByAdminId(requestByIds);
            App.eventBus.post(adminEmployeeList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void changeUserPassword (Intent intent) throws UnknownServiceException {
        try {
            ChangePasswordRequset changePasswordRequset = (ChangePasswordRequset) intent.getSerializableExtra(App.CHANGE_USER_PASSWORD);
            ResponseResult responseResult= service.changeUserPassword(changePasswordRequset);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void updateRegistration(Intent intent) throws UnknownServiceException {
        try {
            User user = (User) intent.getSerializableExtra(App.USER);
            ResponseResult responseResult = service.updateRegistration(user);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void sendMessage(Intent intent) throws UnknownServiceException {
        try {
            MessageRequest messageRequest = (MessageRequest) intent.getSerializableExtra(App.MESSAGE_REQUEST);
            ResponseMessage responseMessage = service.sendMessage(messageRequest);
            App.eventBus.post(responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void messageListByEmpId(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.MESSAGE_LIST_BY_EMPID);
            MessageListResponse messageListResponse = service.messageListByEmpId(requestByIds);
            App.eventBus.post(messageListResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void sentMessageDetail(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds= (RequestByIds) intent.getSerializableExtra(App.SENT_MESSAGE_DETAIL);
            ResponseSentMessageDetail responseSentMessageDetail= service.sentMessageDetail(requestByIds);
            App.eventBus.post(responseSentMessageDetail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminEmployeeDataByAdminIdForMessage(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.REQUEST_BY_EMP_EMAIL_ID_ADMIN);
            AdminEmployeeList userDateWiseReport = service.adminEmployeeDataByAdminIdForMessage(requestByIds);
            App.eventBus.post(userDateWiseReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void logout(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.LOGOUT);
            ResponseLogout responseLogout = service.logout(requestByIds);
            App.eventBus.post(responseLogout);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminUnLockDaysList(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.DAYS_UNLOCK);
            UserDateWiseReport userDateWiseReport = service.adminUnLockDaysList(requestByIds);
            App.eventBus.post(userDateWiseReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminLockDaysList(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.DAYS_LOCK);
            UserDateWiseReport userDateWiseReport = service.adminLockDaysList(requestByIds);
            App.eventBus.post(userDateWiseReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminUnLockDaysEmployeeList(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.DAYS_UNLOCK_EMPLOYEE);
            AdminEmployeeList adminEmployeeList = service.adminUnLockDaysEmployeeList(requestByIds);
            App.eventBus.post(adminEmployeeList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminLockDaysEmployeeList(Intent intent) throws UnknownServiceException {
        try {
            RequestByIds requestByIds = (RequestByIds) intent.getSerializableExtra(App.DAYS_LOCK_EMPLOYEE);
            AdminEmployeeList adminEmployeeList = service.adminLockDaysEmployeeList(requestByIds);
            App.eventBus.post(adminEmployeeList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminUnLockDay(Intent intent) throws UnknownServiceException {
        try {
            LockUnlock lockUnlock = (LockUnlock) intent.getSerializableExtra(App.UNLOCK_DAY);
            ResponseLockUnlock responseResult = service.adminUnLockDay(lockUnlock);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private void adminLockDay(Intent intent) throws UnknownServiceException {
        try {
            LockUnlock lockUnlock = (LockUnlock) intent.getSerializableExtra(App.LOCK_DAY);
            ResponseLockUnlock responseResult = service.adminLockDay(lockUnlock);
            App.eventBus.post(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    public enum Operation {
        LOGIN_USER,
        EMPLOYEE_REGISTRATION_DETAILS,
        EMPLOYEE_REGISRATION,
        EMPLOYEE_DATE_WISE_REPORTS_BY_EMAIL_ID,
        EMPLOYEE_REPORTING_LIST,
        INSERT_REPORT,
        TEMPLATE_LIST,
        DELETE_TEMPLATE,
        ADMIN_EMPLOYEE_TAB1,
        ADMIN_EMPLOYEE_DATEWISE_REPORT,
        DETAIL_REPORTS_BY_EMPID_DATE,
        INSERT_TEMPLATE,
        ADMIN_DATEWISE_REPORT_BY_ADMIN_ID,
        EMPLOYEE_REPORTS_BY_DATE,
        INSERT_COMMENT,
        ADMIN_EMPLOYEE_DATA_BY_ADMIN_ID,
        CHANGE_USER_PASSWORD,
        UPDATE_REGISRATION,
        MESSAGE_REQUEST,
        MESSAGE_LIST_BY_EMPID,
        SENT_MESSAGE_DETAIL,
        REQUEST_BY_EMP_EMAIL_ID_ADMIN,
        LOGOUT,
        DAYS_UNLOCK,
        DAYS_LOCK,
        DAYS_UNLOCK_EMPLOYEE,
        DAYS_LOCK_EMPLOYEE,
        UNLOCK_DAY,
        LOCK_DAY
    }
}
