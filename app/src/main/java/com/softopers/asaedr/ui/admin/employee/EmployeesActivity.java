package com.softopers.asaedr.ui.admin.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.Privilage;
import com.softopers.asaedr.ui.BaseActivity;

import java.util.ArrayList;


public class EmployeesActivity extends BaseActivity {

    ArrayList<Privilage> privilages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_main);

        privilages = (ArrayList<Privilage>) getIntent().getSerializableExtra("employees");

        if (null == savedInstanceState) {
            EmployeesFragment employeesFragment = new EmployeesFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("employees", privilages);
            employeesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, employeesFragment)
                    .commit();
        }
    }

    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        return NAVDRAWER_ITEM_EMPLOYEES;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        for (int i = 0; i < privilages.size(); i++) {
            if (privilages.get(i).getName().contains("Access to Add Employee") && privilages.get(i).getValue()) {
                getMenuInflater().inflate(R.menu.menu_add, menu);
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
            Intent employeeRegisterIntent = new Intent(this, EmployeeRegisterActivity.class);
            employeeRegisterIntent.putExtra("employees", privilages);
            startActivity(employeeRegisterIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
