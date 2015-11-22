package com.softopers.asaedr.ui.admin.employee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.model.DepartmentList;
import com.softopers.asaedr.model.EmployeeRegistrationDetail;
import com.softopers.asaedr.model.PrivilegeCategoryList;
import com.softopers.asaedr.model.ResponseResult;
import com.softopers.asaedr.model.User;
import com.softopers.asaedr.model.UserList;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.ui.BaseActivity;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 19-08-2015.
 */
public class EmployeeRegisterActivity extends BaseActivity {

    private static final String TAG = EmployeeRegisterActivity.class.getSimpleName();

    public static String picturePath;

    View focusView = null;
    String image;
    UserList userList;
    private Handler mHandler = new Handler();
    private EditText employee_register_first_name, employee_register_mobile_number, employee_register_address, employee_register_designation, employee_register_username, employee_register_password, employee_register_description;
    private ImageView employee_register_image;
    private CheckBox employee_register_is_admin;
    private Uri contentUri;
    private Spinner employee_register_privilage, employee_register_department;
    private View mProgressView;
    private View mEmployeeRegisterFormView;
    private Integer departmentId, privilegeCategoryId;
    private LinearLayout header1, header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);

        employee_register_first_name = (EditText) findViewById(R.id.employee_register_first_name);
        employee_register_mobile_number = (EditText) findViewById(R.id.employee_register_mobile_number);
        employee_register_address = (EditText) findViewById(R.id.employee_register_address);
        employee_register_designation = (EditText) findViewById(R.id.employee_register_designation);
        employee_register_username = (EditText) findViewById(R.id.employee_register_username);
        employee_register_password = (EditText) findViewById(R.id.employee_register_password);
        employee_register_description = (EditText) findViewById(R.id.employee_register_description);

        employee_register_image = (ImageView) findViewById(R.id.employee_register_image);

        employee_register_is_admin = (CheckBox) findViewById(R.id.employee_register_is_admin);

        employee_register_privilage = (Spinner) findViewById(R.id.employee_register_privilage);
        employee_register_department = (Spinner) findViewById(R.id.employee_register_department);
        employee_register_privilage.setPrompt("Select a Privilage");
        employee_register_department.setPrompt("Select a Department");

        header1 = (LinearLayout) findViewById(R.id.header1);
        header = (LinearLayout) findViewById(R.id.header);

        mEmployeeRegisterFormView = findViewById(R.id.employee_register_form);

        mProgressView = findViewById(R.id.employee_register_progress);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        if (getIntent().getSerializableExtra("userList") != null) {
            employee_register_username.setEnabled(false);
            employee_register_password.setEnabled(false);
            header1.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            userList = (UserList) getIntent().getSerializableExtra("userList");
            employee_register_first_name.setText(userList.getEmpName());
            employee_register_mobile_number.setText(userList.getMob());
            employee_register_address.setText(userList.getAddress());
            employee_register_designation.setText(userList.getDesignation());
            employee_register_description.setText(userList.getDescription());
            employee_register_username.setText(userList.getUserName());
            employee_register_password.setText(userList.getPassword());
            if (!TextUtils.isEmpty(userList.getImage()))
                Picasso.with(getApplicationContext()).load("http://app.allsportsacademy.in/Image/" + userList.getImage()).placeholder(R.drawable.person_image_empty).error(R.drawable.person_image_empty).into(employee_register_image);
            else
                employee_register_image.setImageResource(R.drawable.person_image_empty);
        } else {
            showProgress(true);

            Intent intent = new Intent(this, RestAPIClientService.class);
            intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.EMPLOYEE_REGISTRATION_DETAILS);
            WakefulIntentService.sendWakefulWork(this, intent);

            employee_register_is_admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        employee_register_privilage.setVisibility(View.VISIBLE);
                    } else {
                        employee_register_privilage.setVisibility(View.GONE);
                    }
                }
            });

            if (!PrefUtils.getUser(getApplicationContext()).getIsOwner()) {
                header1.setVisibility(View.GONE);
            } else {
                header1.setVisibility(View.VISIBLE);
            }

            employee_register_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image = "";
                    selectImage(); //select image from camera or gallery.
                }
            });
        }

        FrameLayout submit_button = (FrameLayout) findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptEmployeeRegistration();
            }
        });

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
                if (getIntent().getSerializableExtra("userList") != null) {
                    toolbar.setTitle("UPDATE EMPLOYEE DETAILS");
                } else {
                    toolbar.setTitle("EMPLOYEE REGISTRATION");
                }

            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            if (photoFile != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(photoFile));
                                startActivityForResult(intent, 1);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(picturePath);
                    contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(picturePath);
                    bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 200, 200, true);
                    employee_register_image.setImageBitmap(bitmapImage);
                    image = ConfigUtils.encodeTobase64(bitmapImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                try {
                    contentUri = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(contentUri, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap bitmapImage = BitmapFactory.decodeFile(picturePath);
                    bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 200, 200, true);
                    employee_register_image.setImageBitmap(bitmapImage);
                    image = ConfigUtils.encodeTobase64(bitmapImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onEvent(final EmployeeRegistrationDetail employeeRegistrationDetail) {
        this.runOnUiThread(new Runnable() {
            public void run() {

                showProgress(false);
                Log.v("RegistrationDetail", employeeRegistrationDetail.getDepartmentList().toString());

                fillSpinners(employeeRegistrationDetail);
            }
        });
    }

    private void fillSpinners(EmployeeRegistrationDetail employeeRegistrationDetail) {

        if (employee_register_department != null) {

            DeptListAdapter adapter = new DeptListAdapter(getApplicationContext(), R.layout.spinner_item, employeeRegistrationDetail.getDepartmentList());
            employee_register_department.setAdapter(adapter);

            employee_register_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    DepartmentList departmentList = (DepartmentList) adapterView.getAdapter().getItem(position);
                    departmentId = departmentList.getDepartmentId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else {
            // should not happen...
            Log.e(TAG, "Years spinner not found (Activity not initialized yet?).");
        }

        if (employee_register_privilage != null) {

            PrivilageCategoryListAdapter adapter = new PrivilageCategoryListAdapter(getApplicationContext(), R.layout.spinner_item, employeeRegistrationDetail.getPrivilegeCategoryList());
            employee_register_privilage.setAdapter(adapter);

            employee_register_privilage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    PrivilegeCategoryList privilegeCategoryList = (PrivilegeCategoryList) adapterView.getAdapter().getItem(position);
                    privilegeCategoryId = privilegeCategoryList.getPrivilegeCategoryId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            // should not happen...
            Log.e(TAG, "Years spinner not found (Activity not initialized yet?).");
        }
    }

    public void attemptEmployeeRegistration() {

        // Reset errors.
        employee_register_first_name.setError(null);
        employee_register_mobile_number.setError(null);
        employee_register_address.setError(null);
        employee_register_designation.setError(null);
        employee_register_username.setError(null);
        employee_register_password.setError(null);

        // Store values at the time of the login attempt.
        String firstName = employee_register_first_name.getText().toString();
        String mobileNumber = employee_register_mobile_number.getText().toString();
        String address = employee_register_address.getText().toString();
        String designation = employee_register_designation.getText().toString();
        String username = employee_register_username.getText().toString();
        String password = employee_register_password.getText().toString();
        String description = employee_register_description.getText().toString();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
        if (TextUtils.isEmpty(password)) {
            employee_register_password.setError(getString(R.string.error_field_required));
            focusView = employee_register_password;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            employee_register_password.setError(getString(R.string.error_invalid_password));
            focusView = employee_register_password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            employee_register_username.setError(getString(R.string.error_field_required));
            focusView = employee_register_username;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            employee_register_username.setError(getString(R.string.error_invalid_username));
            focusView = employee_register_username;
            cancel = true;
        }

        if (TextUtils.isEmpty(address)) {
            employee_register_address.setError(getString(R.string.error_field_required));
            focusView = employee_register_address;
            cancel = true;
        }

        if (TextUtils.isEmpty(mobileNumber)) {
            employee_register_mobile_number.setError(getString(R.string.error_field_required));
            focusView = employee_register_mobile_number;
            cancel = true;
        } else if (!isMobileNumberValid(mobileNumber)) {
            employee_register_mobile_number.setError(getString(R.string.error_invalid_mobile_number));
            focusView = employee_register_mobile_number;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            employee_register_first_name.setError(getString(R.string.error_field_required));
            focusView = employee_register_first_name;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);

            if (getIntent().getSerializableExtra("userList") != null) {
                Intent employeeRegisterIntent = new Intent(EmployeeRegisterActivity.this, RestAPIClientService.class);
                User user = new User();
                user.setEmpId(userList.getEmpId());
                user.setEmpName(firstName);
                user.setMob(mobileNumber);
                user.setAddress(address);
                user.setDesignation(designation);
                user.setDescription(description);
                user.setZoneId(PrefUtils.getUser(getApplicationContext()).getZoneId());
                user.setDepartmentId(PrefUtils.getUser(getApplicationContext()).getDepartmentId());
                user.setIsAdmin(userList.getIsAdmin());
                user.setPrivilegeCategoryId(Integer.valueOf(userList.getPrivilegeCategoryId()));
                user.setUserName(username);
                user.setPassword(password);
                User user1 = new User(user);
                employeeRegisterIntent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.
                        UPDATE_REGISRATION);
                employeeRegisterIntent.putExtra(App.USER, user1);
                WakefulIntentService.sendWakefulWork(EmployeeRegisterActivity.this, employeeRegisterIntent);
            } else {
                Intent employeeRegisterIntent = new Intent(EmployeeRegisterActivity.this, RestAPIClientService.class);
                User user = new User();
                user.setEmpName(firstName);
                user.setMob(mobileNumber);
                user.setAddress(address);
                user.setDesignation(designation);
                user.setImage(image);
                user.setDescription(description);
                if (!PrefUtils.getUser(getApplicationContext()).getIsOwner()) {
                    user.setZoneId(PrefUtils.getUser(getApplicationContext()).getZoneId());
                    user.setDepartmentId(PrefUtils.getUser(getApplicationContext()).getDepartmentId());
                } else {
                    user.setZoneId(PrefUtils.getUser(getApplicationContext()).getZoneId());
                    user.setDepartmentId(departmentId);
                }

                Log.v("isChecked()", String.valueOf(employee_register_is_admin.isChecked()));
                if (employee_register_is_admin.isChecked()) {
                    user.setIsAdmin(true);
                    user.setPrivilegeCategoryId(privilegeCategoryId);
                } else {
                    user.setIsAdmin(false);
                    user.setPrivilegeCategoryId(null);
                }
                user.setUserName(username);
                user.setPassword(password);
                User user1 = new User(user);
                employeeRegisterIntent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.EMPLOYEE_REGISRATION);
                employeeRegisterIntent.putExtra(App.USER, user1);
                WakefulIntentService.sendWakefulWork(EmployeeRegisterActivity.this, employeeRegisterIntent);
            }
        }
    }

    public void onEvent(final ResponseResult e) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Log.v(TAG, e.getStatus());
                showProgress(false);

                if (e.getStatus().equals("Success")) {
                    finish();
                } else {
                    employee_register_username.setError(e.getResult());
                    focusView = employee_register_username;
                    focusView.requestFocus();
                }
            }
        });
    }

    private boolean isUsernameValid(String email) {
        return email.length() >= 6 && email.length() <= 20;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 20;
    }

    private boolean isMobileNumberValid(String mobile) {
        return mobile.length() == 10;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mEmployeeRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmployeeRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmployeeRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mEmployeeRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
