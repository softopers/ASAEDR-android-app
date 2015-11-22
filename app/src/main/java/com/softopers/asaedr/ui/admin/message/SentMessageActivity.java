package com.softopers.asaedr.ui.admin.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.BaseActivity;


public class SentMessageActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);
        SentMessageFragment sentMessageFragment = new SentMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message_id", getIntent().getStringExtra("message_id"));
        sentMessageFragment.setArguments(bundle);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, sentMessageFragment)
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
                toolbar.setTitle("SENT MESSAGE");
            }
        });
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_INVALID;
    }
}
