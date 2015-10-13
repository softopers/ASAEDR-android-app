package com.softopers.asaedr.ui.admin.reporting;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.BaseActivity;

/**
 * Created by Krunal on 9/6/2015.
 */
public class DetailReportsEmpDateActivity extends BaseActivity {

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
                DetailReportsEmpDateFragment detailReportsEmpDateFragment = new DetailReportsEmpDateFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AdminId", getIntent().getStringExtra("AdminId"));
                bundle.putString("EmpId", getIntent().getStringExtra("EmpId"));
                bundle.putString("Date", getIntent().getStringExtra("Date"));
                detailReportsEmpDateFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, detailReportsEmpDateFragment)
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
                if (getIntent().getStringExtra("date-emp") != null) {
                    toolbar.setTitle(getIntent().getStringExtra("Date") + " - " + getIntent().getStringExtra("EmpName"));
                } else {
                    toolbar.setTitle(getIntent().getStringExtra("EmpName") + " - " + getIntent().getStringExtra("Date"));
                }
            }
        });
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_INVALID;
    }

}
