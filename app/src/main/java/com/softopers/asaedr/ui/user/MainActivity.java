package com.softopers.asaedr.ui.user;

import android.content.Intent;
import android.os.Bundle;

import com.softopers.asaedr.R;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.ui.BaseActivity;
import com.softopers.asaedr.ui.admin.reporting.ReportsActivity;
import com.softopers.asaedr.util.PrefUtils;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);

        if (PrefUtils.isLoggedIn(this)) {
            if (PrefUtils.getUserRole(this).equals(App.ADMIN)) {
                Intent intent = new Intent(this, ReportsActivity.class);
                startActivity(intent);
                finish();
            }
        }

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commit();
        }
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_REPORTING;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
