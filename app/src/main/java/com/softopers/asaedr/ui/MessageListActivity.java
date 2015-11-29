package com.softopers.asaedr.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.Privilage;
import com.softopers.asaedr.ui.admin.message.MessageActivity;

import java.util.ArrayList;

/**
 * Created by krunal on 17/11/15.
 */
public class MessageListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean("message")) {

        } else {
            MessageListFragment messageListFragment = new MessageListFragment();
            Bundle bundle = new Bundle();
            messageListFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, messageListFragment)
                    .commit();
        }
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_MESSAGES;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getStringExtra("admin") != null) {
            ArrayList<Privilage> privilages = (ArrayList<Privilage>) getIntent().getSerializableExtra("privilages");
            for (int i = 0; i < privilages.size(); i++) {
                if (privilages.get(i).getName().contains("Access to Send Message") && privilages.get(i).getValue()) {
                    getMenuInflater().inflate(R.menu.menu_add, menu);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new) {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean("message")) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(0);
            MessageListFragment messageListFragment = new MessageListFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, messageListFragment)
                    .commit();
            getIntent().removeExtra("message");
        }
    }
}
