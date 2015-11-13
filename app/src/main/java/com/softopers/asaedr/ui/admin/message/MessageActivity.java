package com.softopers.asaedr.ui.admin.message;

import android.os.Bundle;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.BaseActivity;


public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MessageFragment.newInstance())
                    .commit();
        }
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_ADMIN_MESSAGES;
    }
}
