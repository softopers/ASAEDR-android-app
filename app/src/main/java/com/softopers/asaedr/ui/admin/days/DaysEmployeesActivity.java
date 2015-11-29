package com.softopers.asaedr.ui.admin.days;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.BaseActivity;


public class DaysEmployeesActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            DaysEmployeesFragment daysEmployeesFragment = new DaysEmployeesFragment();
            Bundle bundle = new Bundle();
            if (getIntent().getStringExtra("unlock") != null) {
                bundle.putString("unlock", "unlock");
            } else {
                bundle.putString("lock", "lock");
            }
            bundle.putString("date", getIntent().getStringExtra("date"));
            daysEmployeesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, daysEmployeesFragment)
                    .commit();
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
                toolbar.setTitle(getIntent().getStringExtra("date"));
            }
        });
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_INVALID;
    }
}
