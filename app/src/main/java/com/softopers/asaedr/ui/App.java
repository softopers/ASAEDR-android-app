package com.softopers.asaedr.ui;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import de.greenrobot.event.EventBus;

public class App extends Application {

    public static final EventBus eventBus = EventBus.getDefault();
    /**
     * API Fields *
     */
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static final String REQUEST_BY_EMP_EMAIL_ID = "REQUEST_BY_EMP_EMAIL_ID";
    public static final String EMPLOYEE_REPORTING_LIST = "EMPLOYEE_REPORTING_LIST";
    public static final String INSERT_REPORT = "INSERT_REPORT";
    public static final String TEMPLATE_LIST = "TEMPLATE_LIST";
    public static final String DELETE_TEMPLATE = "DELETE_TEMPLATE";
    public static final String ADMIN_EMPLOYEE_TAB1 = "ADMIN_EMPLOYEE_TAB1";
    public static final String ADMIN_EMPLOYEE_DATEWISE_REPORT = "ADMIN_EMPLOYEE_DATEWISE_REPORT";
    public static final String DETAIL_REPORTS_BY_EMPID_DATE = "DETAIL_REPORTS_BY_EMPID_DATE";
    public static final String INSERT_TEMPLATE = "INSERT_TEMPLATE";
    public static final String ADMIN_DATEWISE_REPORT_BY_ADMIN_ID = "ADMIN_DATEWISE_REPORT_BY_ADMIN_ID";
    public static final String EMPLOYEE_REPORTS_BY_DATE = "EMPLOYEE_REPORTS_BY_DATE";
    public static final String INSERT_COMMENT = "INSERT_COMMENT";
    public static final String ADMIN_EMPLOYEE_DATA_BY_ADMIN_ID  = "ADMIN_EMPLOYEE_DATA_BY_ADMIN_ID";
    public static final String CHANGE_USER_PASSWORD  = "CHANGE_USER_PASSWORD";
    public static final String MESSAGE_REQUEST  = "MESSAGE_REQUEST";
    public static final String MESSAGE_LIST_BY_EMPID  = "MESSAGE_LIST_BY_EMPID";
    public static final String SENT_MESSAGE_DETAIL  = "SENT_MESSAGE_DETAIL";
    public static final String REQUEST_BY_EMP_EMAIL_ID_ADMIN = "REQUEST_BY_EMP_EMAIL_ID_ADMIN";
    public static final String LOGOUT = "LOGOUT";

    private static final int DELAYED_MS = 750;
    public static Context context;

    public static void postEvent(Object event) {
        eventBus.post(event);
    }

    public static void postEventDelayed(final Object event) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                eventBus.post(event);
            }
        }, DELAYED_MS);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
