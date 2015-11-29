package com.softopers.asaedr.ui.admin.days;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.BaseActivity;

/**
 * Created by krunal on 29/11/15.
 */
public class DaysActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            DaysFragment daysFragment = new DaysFragment();
            Bundle bundle = new Bundle();
            if (getIntent().getStringExtra("unlock") != null) {
                bundle.putString("unlock", "unlock");
            } else {
                bundle.putString("lock", "lock");
            }
            daysFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, daysFragment)
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
                toolbar.setTitle(getIntent().getStringExtra("unlock") != null ? "UNLOCK DAYS" : "LOCK DAYS");
            }
        });
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return getIntent().getStringExtra("unlock") != null ? NAVDRAWER_ITEM_UNLOCK_DAYS : NAVDRAWER_ITEM_LOCK_DAYS;
    }
}
