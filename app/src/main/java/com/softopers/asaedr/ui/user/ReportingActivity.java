package com.softopers.asaedr.ui.user;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.BaseActivity;

/**
 * Created by Krunal on 09-08-2015.
 */
public class ReportingActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean("comment")) {
            } else {
                if (getIntent().getStringExtra("DayStatusId") != null) {
                    ReportingFragment reportingFragment = new ReportingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("DayStatusId", getIntent().getStringExtra("DayStatusId"));
                    bundle.putBoolean("isLock", getIntent().getBooleanExtra("isLock", false));
                    reportingFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, reportingFragment)
                            .commit();
                }

            }
        }

        final Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent().getExtras().getString("date") != null) {
                    toolbar.setTitle(getIntent().getExtras().getString("date"));
                } else {
                    toolbar.setTitle(getIntent().getStringExtra("date"));
                }

            }
        });
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_INVALID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean("comment")) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(1);
            String id = getIntent().getExtras().getString("DayStatusId");
            Log.v("id", String.valueOf(id));
            ReportingFragment reportingFragment = new ReportingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("DayStatusId", id);
            bundle.putString("noti", "noti");
            reportingFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, reportingFragment)
                    .commit();
            getIntent().removeExtra("comment");
        }
    }

}
