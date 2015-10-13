package com.softopers.asaedr.ui.admin.reporting.report_employee_date;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.BaseActivity;

/**
 * Created by Krunal on 9/6/2015.
 */
public class AfterReportsActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            if (getIntent().getStringExtra("EmpId") != null) {
                AfterReportsFragment afterReportsFragment = new AfterReportsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AdminId", getIntent().getStringExtra("AdminId"));
                bundle.putString("EmpId", getIntent().getStringExtra("EmpId"));
                bundle.putString("EmpName", getIntent().getStringExtra("EmpName"));
                afterReportsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, afterReportsFragment)
                        .commit();
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
                toolbar.setTitle(getIntent().getStringExtra("EmpName"));
            }
        });
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_INVALID;
    }

}
